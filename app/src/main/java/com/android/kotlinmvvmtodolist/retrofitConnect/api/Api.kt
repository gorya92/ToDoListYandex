package com.android.kotlinmvvmtodolist.retrofitConnect.api

import androidx.lifecycle.LiveData
import com.android.kotlinmvvmtodolist.retrofitConnect.model.element
import com.example.todolistyandex.model.TodoList
import retrofit2.Response
import retrofit2.http.*

interface Api {
    @GET("list")
    suspend fun getList(
    ): Response<TodoList>

    @GET("list")
    suspend fun getItem(
        @Header("X-Last-Known-Revision") revision: String,
        @Query("id") id: String,
    ): Response<element>

    @DELETE("list/{id}")
    suspend fun deleteItem(
        @Header("X-Last-Known-Revision") revision: String,
        @Path("id") id: String
    ): Response<element>

    @PUT("list/{id}")
    suspend fun putItem(
        @Header("X-Last-Known-Revision") revision: String,
        @Path("id") id: String,
        @Body elementModel: element
    ): Response<element>

    @POST("list")
    suspend fun postElement(
        @Header("X-Last-Known-Revision") revision: String,
        @Body elementModel: element
    ): Response<element>

    @PATCH("list")
    suspend fun patchElement(
        @Header("X-Last-Known-Revision") revision: String,
        @Body elementModel: TodoList
    ): Response<TodoList>
}