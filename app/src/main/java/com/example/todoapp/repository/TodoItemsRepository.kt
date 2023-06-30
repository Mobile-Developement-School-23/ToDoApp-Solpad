package com.example.todoapp.repository

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.todoapp.db.TodoItemsDatabase
import com.example.todoapp.model.Resourse
import com.example.todoapp.model.TodoItem
import com.example.todoapp.network.RetrofitRepository
import com.example.todoapp.network.RevisionService
import com.example.todoapp.network.model.GetListItemsNetwork
import com.example.todoapp.network.model.SetItemRequest
import com.example.todoapp.network.model.SetItemResponse
import com.example.todoapp.network.model.TodoItemNetwork
import com.example.todoapp.network.model.TodoItemNetwork.Companion.mapToTodoItemNetwork
import com.example.todoapp.util.CheckInternetConnection
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import retrofit2.Response
import java.io.IOException
import java.net.UnknownHostException
import java.util.UUID

class TodoItemsRepository(
    val context: Context,
    val database: TodoItemsDatabase
) {


    private val mTodoItemsLiveData: MutableLiveData<List<TodoItem>> = MutableLiveData()
    val todoItemsLiveData: LiveData<List<TodoItem>> = mTodoItemsLiveData

    private val mResourseRequest = MutableLiveData<Resourse>()
    val resourseRequest: LiveData<Resourse> = mResourseRequest

    private val checkInternetConnectionGet = CheckInternetConnection()
    private val checkInternetConnectionSet = CheckInternetConnection()

    private val revisionService = RevisionService(context)
    private val converterRepository = ConverterRepository(revisionService)


    private fun setTodoItemsLiveData(todoItems: List<TodoItem>) {
        mTodoItemsLiveData.postValue(todoItems)
        Log.e("set",todoItems.toString())

    }


    //
    // Блок кода, отвечающий за добавление нового элемента на сервер
    //

    suspend fun addTodoItems(context: Context, todoItem: TodoItem, id: String?) {
        if (checkInternetConnectionGet.checkInternet(context)) {
            addTodoItemNetwork(context,todoItem, id)
            addTodoItemDatabase(todoItem,id)
        } else {
            addTodoItemDatabase(todoItem,id)
            setTodoItemsLiveData(database.getDao().getAll())
        }
    }

    suspend fun addTodoItemDatabase(todoItem: TodoItem, id: String?) {
        val addItem: TodoItem = if (id != null) todoItem
        else todoItem.copy(id = makingId())
        database.getDao().addTodoItem(addItem)
    }

    suspend fun addTodoItemNetwork(context: Context, todoItem: TodoItem, id: String?) {
        addTodoItemFlow(context, todoItem, id).catch {
            mResourseRequest.postValue(Resourse.Error(mapErrors(it)))
        }.collect {
            setTodoItemsLiveData(it)
            mResourseRequest.postValue(Resourse.Success())
        }
    }


    private suspend fun addTodoItemFlow(
        context: Context,
        todoItem: TodoItem,
        id: String?
    ): Flow<List<TodoItem>> {

        val addItemNetwork: TodoItemNetwork = if (id != null) {
            todoItem.mapToTodoItemNetwork(id)
        } else todoItem.mapToTodoItemNetwork(makingId())

        val setItemRequest = SetItemRequest(todoItemNetwork = addItemNetwork)

        return flow {
            val response = checkInternetConnectionSet
                .checkingInternetConnectionSet(
                    context,
                    RetrofitRepository.getRetrofit(context).addTodoItem(setItemRequest)
                )

            if (checkerResponse(response))
                emit(converterRepository.converterAddTodoItem(response.body()!!, todoItemsLiveData))
        }.flowOn(Dispatchers.IO)
    }


    //
    // Блок кода, отвечающий за получения списка с сервера.
    //

    suspend fun getTodoItems() {
        if (checkInternetConnectionGet.checkInternet(context)) {
            getTodoItemsNetwork(context)
        } else {
            getTodoItemsDatabase()

        }
    }

    suspend fun getTodoItemsDatabase() {
        getTodoItemDatabaseFlow().catch {
            mResourseRequest.postValue(Resourse.Error(mapErrors(it)))
        }.collect {
            setTodoItemsLiveData(it)
            mResourseRequest.postValue(Resourse.Success())
        }
    }

    private suspend fun getTodoItemDatabaseFlow(): Flow<List<TodoItem>> {
        return flow {
            emit(database.getDao().getAll())
        }
    }


    suspend fun getTodoItemsNetwork(context: Context) {
        getTodoItemsNetworkFlow(context).collect {
            setTodoItemsLiveData(it)
        }
    }

    private suspend fun getTodoItemsNetworkFlow(context: Context): Flow<List<TodoItem>> {
        return flow {
            val response = checkInternetConnectionGet
                .checkingInternetConnectionGet(
                    context,
                    RetrofitRepository.getRetrofit(context).getListTodoItems()
                )

            if (checkerGetResponse(response))
                emit(response.body()!!.listItemsNetwork.map { it.mapToTodoItem() })

        }.flowOn(Dispatchers.IO)
    }

    //
    // Блок кода отвечающий за удаление элемента с сервера.
    //

    suspend fun deleteTodoItem(todoItem: TodoItem,id:String){
        if (checkInternetConnectionGet.checkInternet(context)) {
            deleteTodoItemNetwork(context,id)
            deleteTodoItemDatabase(todoItem)
        } else {
            deleteTodoItemDatabase(todoItem)
            setTodoItemsLiveData(database.getDao().getAll())
        }
    }
    suspend fun deleteTodoItemDatabase(todoItem: TodoItem) {
        database.getDao().deleteTodoItem(todoItem)
        //setTodoItemsLiveData(database.getDao().getAll())
    }

    suspend fun deleteTodoItemNetwork(context: Context, id: String) {
        deleteTodoItemFlow(context, id).catch {
            mResourseRequest.postValue(Resourse.Error(mapErrors(it)))
        }.collect {
            setTodoItemsLiveData(it)
            mResourseRequest.postValue(Resourse.Success())
        }
    }

    private suspend fun deleteTodoItemFlow(context: Context, id: String): Flow<List<TodoItem>> {
        return flow {
            val response = checkInternetConnectionSet
                .checkingInternetConnectionSet(
                    context,
                    RetrofitRepository.getRetrofit(context).deleteTodoItem(id)
                )

            if (checkerResponse(response))
                emit(converterRepository.converterDeleteTodoItem( response.body()!!, todoItemsLiveData ))

            emit(
                checkerResponse(response) {
                    converterRepository.converterDeleteTodoItem(
                        response.body()!!,
                        todoItemsLiveData
                    )
                })
        }.flowOn(Dispatchers.IO)
    }


    //
    // Блок кода, отвечающий за редактирование элемента на сервере.
    //

    suspend fun editTodoItem(context: Context,todoItem: TodoItem) {
        if (checkInternetConnectionSet.checkInternet(context)) {
            editTodoItemNetwork(context,todoItem)
            editTodoItemDatabase(todoItem)
        } else {
            editTodoItemDatabase(todoItem)
            setTodoItemsLiveData(database.getDao().getAll())
        }
    }
    suspend fun editTodoItemDatabase(todoItem: TodoItem) {
        database.getDao().editTodoItem(todoItem)
        //setTodoItemsLiveData(database.getDao().getAll())

    }

    suspend fun editTodoItemNetwork(context: Context, item: TodoItem) {
        editTodoItemFlow(context, item).catch {
            mResourseRequest.postValue(Resourse.Error(mapErrors(it)))
        }.collect {
            setTodoItemsLiveData(it)
            mResourseRequest.postValue(Resourse.Success())
        }
    }

    private suspend fun editTodoItemFlow(
        context: Context,
        todoItem: TodoItem
    ): Flow<List<TodoItem>> {

        val todoItemNetwork: TodoItemNetwork = todoItem.mapToTodoItemNetwork(todoItem.id)
        val setItemRequest = SetItemRequest(todoItemNetwork = todoItemNetwork)
        return flow {

            val response = checkInternetConnectionSet
                .checkingInternetConnectionSet(
                    context,
                    RetrofitRepository.getRetrofit(context)
                        .editTodoItem(todoItem.id, setItemRequest)
                )

            if (checkerResponse(response))
                emit(
                    converterRepository.converterEditTodoItem(
                        response.body()!!,
                        todoItemsLiveData
                    )
                )

        }.flowOn(Dispatchers.IO)
    }


    private fun makingId(): String = UUID.randomUUID().toString()

    private fun mapErrors(e: Throwable) = when (e) {
        is UnknownHostException -> "wrong"
        is IOException -> "no internet"
        else -> "sthm"
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

}
