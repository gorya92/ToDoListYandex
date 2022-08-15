package com.example.todoList.api

import com.example.todolistyandex.model.TodoList
import com.example.todoList.di.module.element
import io.reactivex.Observable
import retrofit2.http.*

interface GithubAPI {

    @GET("list")
    fun getItem(
        @Header("X-Last-Known-Revision") revision: String,
        @Query("id") id: String,
    ): Observable<element>

    @DELETE("list/{id}")
    fun deleteItem(
        @Header("X-Last-Known-Revision") revision: String,
        @Path("id") id: String
    ): Observable<element>

    @PUT("list/{id}")
    fun putItem(
        @Header("X-Last-Known-Revision") revision: String,
        @Path("id") id: String,
        @Body elementModel: element
    ): Observable<element>

    @POST("list")
    fun postElement(
        @Header("X-Last-Known-Revision") revision: String,
        @Body elementModel: element
    ): Observable<element>

    @PATCH("list")
    fun patchElement(
        @Header("X-Last-Known-Revision") revision: String,
        @Body elementModel: TodoList
    ): Observable<TodoList>

    @GET("list")
    fun getList(
    ): Observable<TodoList>

}
