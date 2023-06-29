package com.example.todoapp.repository

import android.content.Context
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
    val context: Context
) {


    private val mTodoItemsLiveData: MutableLiveData<List<TodoItem>> = MutableLiveData()
    val todoItemsLiveData: LiveData<List<TodoItem>> = mTodoItemsLiveData

    private val mResourseRequest = MutableLiveData<Resourse>()
    val resourseRequest: LiveData<Resourse> = mResourseRequest

    private val checkInternetConnectionGet = CheckInternetConnection<GetListItemsNetwork>()
    private val checkInternetConnectionSet = CheckInternetConnection<SetItemResponse>()

    private val revisionService = RevisionService(context)
    private val converterRepository = ConverterRepository(revisionService)


    private fun setTodoItemsLiveData(todoItems: List<TodoItem>) =
        mTodoItemsLiveData.postValue(todoItems)


    //
    // Блок кода, отвечающий за добавление нового элемента на сервер
    //
    suspend fun addTodoItemNetwork(context: Context, todoItem: TodoItem, id: String?) {
        addTodoItemFlow(context, todoItem, id).catch {
            mResourseRequest.postValue(Resourse.Error(mapErrors(it)))
        }.collect {
            setTodoItemsLiveData(it)
            mResourseRequest.postValue(Resourse.Success())

        }
    }

    private suspend fun addTodoItemFlow( context: Context,todoItem: TodoItem,id: String?): Flow<List<TodoItem>> {

        val addItemNetwork: TodoItemNetwork = if (id != null) {
            todoItem.mapToTodoItemNetwork(id)
        } else todoItem.mapToTodoItemNetwork(makingId())

        val setItemRequest = SetItemRequest(todoItemNetwork = addItemNetwork)

        return flow {
            val response = checkInternetConnectionSet
                .checkingInternetConnection(context,RetrofitRepository.getRetrofit(context).addTodoItem(setItemRequest))

            if (checkerResponse(response))
                emit(converterRepository.converterAddTodoItem(response.body()!!, todoItemsLiveData))
        }.flowOn(Dispatchers.IO)
    }


    //
    // Блок кода, отвечающий за получения списка с сервера.
    //
    suspend fun getTodoItemsNetwork(context: Context) {
        getTodoItemsNetworkFlow(context).collect {
            setTodoItemsLiveData(it)
        }
    }

    private suspend fun getTodoItemsNetworkFlow(context: Context): Flow<List<TodoItem>> {
        return flow {
            val response = checkInternetConnectionGet
                .checkingInternetConnection(context, RetrofitRepository.getRetrofit(context).getListTodoItems() )

            if (checkerGetResponse(response))
                emit( response.body()!!.listItemsNetwork.map { it.mapToTodoItem() })

        }.flowOn(Dispatchers.IO)
    }

    //
    // Блок кода отвечающий за удаление элемента с сервера.
    //
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
                .checkingInternetConnection(context,RetrofitRepository.getRetrofit(context).deleteTodoItem(id))

            if (checkerResponse(response))
                emit( converterRepository.converterDeleteTodoItem(response.body()!!, todoItemsLiveData))
        }.flowOn(Dispatchers.IO)
    }


    //
    // Блок кода, отвечающий за редактирование элемента на сервере.
    //

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
                .checkingInternetConnection(
                    context,
                    RetrofitRepository.getRetrofit(context)
                        .editTodoItem(todoItem.id, setItemRequest)
                )

            if (checkerResponse(response))
                emit( converterRepository.converterEditTodoItem(response.body()!!, todoItemsLiveData))

        }.flowOn(Dispatchers.IO)
    }


    private fun makingId(): String = UUID.randomUUID().toString()

    private fun mapErrors(e: Throwable) = when (e) {
        is UnknownHostException -> "wrong"
        is IOException -> "no internet"
        else -> "sthm"
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
