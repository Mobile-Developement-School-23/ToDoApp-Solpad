package com.example.todoapp.network

import com.example.todoapp.model.TodoItem
import com.example.todoapp.network.model.GetListItemsNetwork
import com.example.todoapp.network.model.SetItemRequest
import com.example.todoapp.network.model.SetItemResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface TodoApiRequest {

    @GET("list")
    suspend fun getListTodoItems(): Response<GetListItemsNetwork>

    @POST("list")
    suspend fun addTodoItem(
        @Body
        postItemRequest: SetItemRequest
    ): Response<SetItemResponse>

    @DELETE("list/{id}")
    suspend fun deleteTodoItem(
        @Path("id")
        id: String
    ): Response<SetItemResponse>

    @PUT("list/{id}")
    suspend fun editTodoItem(
        @Path("id")
        id: String,
        @Body
        todoItem: SetItemRequest
    ): Response<SetItemResponse>
}