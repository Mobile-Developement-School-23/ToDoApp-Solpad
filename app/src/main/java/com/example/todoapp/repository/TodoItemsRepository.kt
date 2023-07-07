package com.example.todoapp.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.todoapp.db.ItemRoomDao
import com.example.todoapp.model.Resource
import com.example.todoapp.model.TodoItem
import com.example.todoapp.network.RevisionService
import com.example.todoapp.network.TodoApiRequest
import com.example.todoapp.network.model.GetListItemsNetwork
import com.example.todoapp.network.model.SetItemRequest
import com.example.todoapp.network.model.SetItemResponse
import com.example.todoapp.network.model.SetItemsRequest
import com.example.todoapp.network.model.TodoItemNetwork
import com.example.todoapp.network.model.TodoItemNetwork.Companion.mapToTodoItemNetwork
import com.example.todoapp.util.CheckInternetConnection
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext
import retrofit2.Response
import java.io.IOException
import java.net.UnknownHostException
import java.util.UUID
import javax.inject.Inject

class TodoItemsRepository @Inject constructor(
    private val todoApiNetwork: TodoApiRequest,
    private val todoApiDatabase: ItemRoomDao,
    private val checkInternetConnect: CheckInternetConnection
) {


    val mTodoItemsLiveData: MutableLiveData<List<TodoItem>> = MutableLiveData()
    val todoItemsLiveData: LiveData<List<TodoItem>> = mTodoItemsLiveData

    private val mResourseRequest = MutableLiveData<Resource>()
    val resourseRequest: LiveData<Resource> = mResourseRequest


    @Inject
    lateinit var revisionService: RevisionService

    @Inject
    lateinit var converterRepository: ConverterRepository


    private fun setTodoItemsLiveData(todoItems: List<TodoItem>) {
        mTodoItemsLiveData.postValue(todoItems)
    }


    //
    // Блок кода, отвечающий за добавление нового элемента на сервер
    //

    suspend fun addTodoItems(todoItem: TodoItem, id: String?) {
        if (checkInternetConnect.checkInternet()) {
            addTodoItemNetwork(todoItem, id)
            addTodoItemDatabase(todoItem, id)
        } else {
            addTodoItemDatabase(todoItem, id)
            setTodoItemsLiveData(todoApiDatabase.getAll())
        }
    }

    suspend fun addTodoItemDatabase(todoItem: TodoItem, id: String?) {
        val addItem: TodoItem = if (id != null) todoItem
        else todoItem.copy(id = makingId())
        todoApiDatabase.addTodoItem(addItem)
        revisionService.setRevisionDatabase(revisionService.getRevisionNetwork() + 1)
    }

    suspend fun addTodoItemNetwork(todoItem: TodoItem, id: String?) {
        addTodoItemFlow(todoItem, id).catch {
            mResourseRequest.postValue(Resource.Error(mapErrors(it)))
        }.collect {
            setTodoItemsLiveData(it)
            mResourseRequest.postValue(Resource.Success())
        }
    }


    private suspend fun addTodoItemFlow(
        todoItem: TodoItem,
        id: String?
    ): Flow<List<TodoItem>> {

        val addItemNetwork: TodoItemNetwork = if (id != null) {
            todoItem.mapToTodoItemNetwork(id)
        } else todoItem.mapToTodoItemNetwork(makingId())
        val setItemRequest = SetItemRequest(todoItemNetwork = addItemNetwork)
        return flow {
            val response = checkInternetConnect
                .checkInternetSet(todoApiNetwork.addTodoItem(setItemRequest))
            if (checkerResponse(response))
                emit(converterRepository.converterAddTodoItem(response.body()!!, todoItemsLiveData))
        }.flowOn(Dispatchers.IO)
    }


    //
    // Блок кода, отвечающий за получения списка с сервера.
    //

    suspend fun getTodoItems() {
        if (checkInternetConnect.checkInternet()) {
            getTodoItemsNetwork()
            synchronizationData(todoItemsLiveData.value ?: listOf<TodoItem>())
        } else {
            getTodoItemsDatabase()
            synchronizationData(todoItemsLiveData.value ?: listOf<TodoItem>())

        }
    }

    suspend fun getTodoItemsDatabase() {
        getTodoItemDatabaseFlow().catch {
            mResourseRequest.postValue(Resource.Error(mapErrors(it)))
        }.collect {
            setTodoItemsLiveData(it)
            mResourseRequest.postValue(Resource.Success())
        }
    }

    private suspend fun getTodoItemDatabaseFlow(): Flow<List<TodoItem>> {
        return flow {
            emit(todoApiDatabase.getAll())
        }
    }


    suspend fun getTodoItemsNetwork() {
        getTodoItemsNetworkFlow().catch {
            mResourseRequest.postValue(Resource.Error(mapErrors(it)))
        }.collect {
            setTodoItemsLiveData(it)
            mResourseRequest.postValue(Resource.Success())
        }
    }

    private suspend fun getTodoItemsNetworkFlow(): Flow<List<TodoItem>> {
        return flow {
            val response = checkInternetConnect
                .checkInternetGet(todoApiNetwork.getListTodoItems())
            if (checkerGetResponse(response)) {
                revisionService.setRevisionNetwork(response.body()!!.revision)
                emit(response.body()!!.listItemsNetwork.map { it.mapToTodoItem() })
            }
        }.flowOn(Dispatchers.IO)
    }

    //
    // Блок кода отвечающий за удаление элемента с сервера.
    //

    suspend fun deleteTodoItem(todoItem: TodoItem, id: String) {
        if (checkInternetConnect.checkInternet()) {
            deleteTodoItemNetwork(id)
            deleteTodoItemDatabase(todoItem)
        } else {
            deleteTodoItemDatabase(todoItem)
            setTodoItemsLiveData(todoApiDatabase.getAll())
        }
    }

    suspend fun deleteTodoItemDatabase(todoItem: TodoItem) {
        todoApiDatabase.deleteTodoItem(todoItem)
        revisionService.setRevisionDatabase(revisionService.getRevisionNetwork() + 1)

    }

    suspend fun deleteTodoItemNetwork(id: String) {
        deleteTodoItemFlow(id).catch {
            mResourseRequest.postValue(Resource.Error(mapErrors(it)))
        }.collect {
            setTodoItemsLiveData(it)
            mResourseRequest.postValue(Resource.Success())
        }
    }

    private suspend fun deleteTodoItemFlow(id: String): Flow<List<TodoItem>> {
        return flow {
            val response = todoApiNetwork.deleteTodoItem(id)

            if (checkerResponse(response))
                emit( converterRepository.converterDeleteTodoItem( response.body()!!, todoItemsLiveData   )
                )
        }.flowOn(Dispatchers.IO)
    }


    //
    // Блок кода, отвечающий за редактирование элемента на сервере.
    //

    suspend fun editTodoItem(todoItem: TodoItem) {
        if (checkInternetConnect.checkInternet()) {
            editTodoItemNetwork(todoItem)
            editTodoItemDatabase(todoItem)
        } else {
            editTodoItemDatabase(todoItem)
            setTodoItemsLiveData(todoApiDatabase.getAll())
        }
    }

    suspend fun editTodoItemDatabase(todoItem: TodoItem) {
        todoApiDatabase.editTodoItem(todoItem)
        revisionService.setRevisionDatabase(revisionService.getRevisionNetwork() + 1)
    }

    suspend fun editTodoItemNetwork(item: TodoItem) {
        editTodoItemFlow(item).catch {
            mResourseRequest.postValue(Resource.Error(mapErrors(it)))
        }.collect {
            setTodoItemsLiveData(it)
            mResourseRequest.postValue(Resource.Success())
        }
    }

    private suspend fun editTodoItemFlow(
        todoItem: TodoItem
    ): Flow<List<TodoItem>> {

        val todoItemNetwork: TodoItemNetwork = todoItem.mapToTodoItemNetwork(todoItem.id)
        val setItemRequest = SetItemRequest(todoItemNetwork = todoItemNetwork)
        return flow {

            val response = checkInternetConnect
                .checkInternetSet(todoApiNetwork.editTodoItem(todoItem.id, setItemRequest))

            if (checkerResponse(response))
                emit(
                    converterRepository.converterEditTodoItem(
                        response.body()!!,
                        todoItemsLiveData
                    )
                )

        }.flowOn(Dispatchers.IO)
    }


    //
    // Блок кода, обрабатывающий patch
    //

    suspend fun patchTodoItemNetwork(items: List<TodoItem>) {
        patchTodoItemFlow(items).catch {
            mResourseRequest.postValue(Resource.Error(mapErrors(it)))
        }.collect {
            setTodoItemsLiveData(it)
            mResourseRequest.postValue(Resource.Success())
        }
    }

    private suspend fun patchTodoItemFlow(
        todoItems: List<TodoItem>
    ): Flow<List<TodoItem>> {

        val todoItemsNetwork = todoItems.map { it.mapToTodoItemNetwork(it.id) }
        var setItemsRequest = SetItemsRequest(todoItemsNetwork = todoItemsNetwork)

        return flow {
            val revision = revisionService.getRevisionDatabase() - 1
            val response = if (checkInternetConnect.checkInternet())
                todoApiNetwork.patchTodoItem(revision.toString(), setItemsRequest)
            else throw IOException("Нет интернет соединения")

            if (checkerGetResponse(response))
                emit(response.body()!!.listItemsNetwork.map { it.mapToTodoItem() })

        }.flowOn(Dispatchers.IO)
    }


    private fun makingId(): String = UUID.randomUUID().toString()

    private fun mapErrors(e: Throwable) = when (e) {
        is UnknownHostException -> "Ошибка"
        is IOException -> "Отстутствует интернет"
        else -> "Ошибка"
    }

    suspend fun <T> checkerResponse(
        response: Response<SetItemResponse>,
        call: suspend () -> (T)
    ): T {
        if (response.isSuccessful) {
            val body = response.body()
            if (body != null) {
                return call()
            } else throw UnknownHostException("Тело запроса - null")
        } else throw UnknownHostException(response.message())
    }

    private fun checkerResponse(response: Response<SetItemResponse>): Boolean {
        if (response.isSuccessful) {
            val body = response.body()
            if (body != null) {
                return true
            } else throw UnknownHostException("Тело запроса - null")
        } else throw UnknownHostException(response.message())
    }

    private fun checkerGetResponse(response: Response<GetListItemsNetwork>): Boolean {
        if (response.isSuccessful) {
            val body = response.body()
            if (body != null) {
                return true
            } else throw UnknownHostException("Тело запроса - null")
        } else throw UnknownHostException(response.message())
    }


    fun checkInternetConnection() = checkInternetConnect.checkInternet()

    suspend fun synchronizationData(todoItemsNetwork: List<TodoItem>) {

        val revisionNetwork = revisionService.getRevisionNetwork()
        val revisionDatabase = revisionService.getRevisionDatabase()

        if (revisionNetwork > revisionDatabase) {
            synchronizationDatabase(todoItemsNetwork, todoApiDatabase.getAll())
        } else {
            synchronizationNetwork(todoApiDatabase.getAll())
        }
    }

    suspend fun synchronizationDatabase(
        todoItemsDatabase: List<TodoItem>,
        todoItemsNetwork: List<TodoItem>
    ) {
        if (todoItemsDatabase.isNotEmpty()) todoApiDatabase.deleteAll()
        todoApiDatabase.addAllTodoItems(todoItemsNetwork)

    }

    suspend fun synchronizationNetwork(todoItemsDatabase: List<TodoItem>) =
        withContext(Dispatchers.IO) {
            patchTodoItemNetwork(todoItemsDatabase)
        }
}
