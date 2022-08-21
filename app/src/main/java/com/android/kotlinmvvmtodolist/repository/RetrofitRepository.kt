package com.android.kotlinmvvmtodolist.repository

import com.android.kotlinmvvmtodolist.retrofitConnect.RetrofitConstants
import com.android.kotlinmvvmtodolist.retrofitConnect.api.Api
import com.android.kotlinmvvmtodolist.retrofitConnect.api.RetrofitInstance
import com.android.kotlinmvvmtodolist.retrofitConnect.api.RetrofitInstance.api
import com.android.kotlinmvvmtodolist.retrofitConnect.model.element
import com.example.todolistyandex.model.TodoList
import retrofit2.Call
import retrofit2.Response


class RetrofitRepository () {
    /** /list Get **/
    suspend fun getList() : Response<TodoList> {
        return api.getList()
    }

    /** /Item Get **/
    suspend fun getItem(id:String) : Response<element> {
        return api.getItem(RetrofitConstants.REVISION.toString(),id)
    }

    /** /Item Delete **/
    suspend fun deleteItem (id:String) : Response<element> {
        return api.deleteItem(RetrofitConstants.REVISION.toString(),id)
    }

    /** /Item Put **/
    suspend fun putItem (id:String,element: element) : Response<element> {
        return api.putItem(RetrofitConstants.REVISION.toString(),id,element)
    }

    /** /list Post **/
    suspend fun postItem(Item: element) : Response<element> {
        return api.postElement(RetrofitConstants.REVISION.toString(),Item)
    }

    /** /list Patch **/
    suspend fun patchList(Item: TodoList) : Response<TodoList> {
        return api.patchElement(RetrofitConstants.REVISION.toString(),Item)
    }
}