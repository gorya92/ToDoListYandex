package com.bignerdranch.android.testing.retrofitConnect.api

import com.bignerdranch.android.testing.retrofitConnect.RetrofitConstants
import com.example.todolistyandex.model.Todo
import com.example.todolistyandex.model.TodoList
import com.example.todolistyandex.model.element
import org.json.JSONObject
import retrofit2.http.*

interface TodoListApi {

    @GET("list")
    suspend fun getList(
    ): retrofit2.Response<TodoList>

    @GET("list")
    suspend fun getItem(
        @Header("X-Last-Known-Revision") revision: String,
        @Query("id") id: String,
    ): retrofit2.Response<element>

    @DELETE("list/{id}")
    suspend fun deleteItem(
        @Header("X-Last-Known-Revision") revision: String,
        @Path("id") id: String
    ): retrofit2.Response<element>

    @PUT("list/{id}")
    suspend fun putItem(
        @Header("X-Last-Known-Revision") revision: String,
        @Path("id") id: String,
        @Body elementModel: element
    ): retrofit2.Response<element>

    @POST("list")
    suspend fun postElement(
        @Header("X-Last-Known-Revision") revision: String,
        @Body elementModel: element
    ): retrofit2.Response<element>

    @PATCH("list")
    suspend fun patchElement(
        @Header("X-Last-Known-Revision") revision: String,
        @Body elementModel: TodoList
    ): retrofit2.Response<TodoList>


}