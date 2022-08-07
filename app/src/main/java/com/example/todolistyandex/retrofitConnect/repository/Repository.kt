package com.bignerdranch.android.testing.retrofitConnect.repository

import com.bignerdranch.android.testing.retrofitConnect.RetrofitConstants
import com.bignerdranch.android.testing.retrofitConnect.api.RetrofitInstance
import com.example.todolistyandex.model.Todo
import com.example.todolistyandex.model.TodoList
import com.example.todolistyandex.model.element
import retrofit2.Response

class Repository {
    /** /list Get **/
    suspend fun getList() : Response<TodoList> {
        return RetrofitInstance.api.getList()
    }

    /** /Item Get **/
    suspend fun getItem(id:String) : Response<element> {
        return RetrofitInstance.api.getItem(RetrofitConstants.REVISION.toString(),id)
    }

    /** /Item Delete **/
    suspend fun deleteItem (id:String) : Response<element> {
        return RetrofitInstance.api.deleteItem(RetrofitConstants.REVISION.toString(),id)
    }

    /** /Item Put **/
    suspend fun putItem (id:String,element: element) : Response<element> {
        return RetrofitInstance.api.putItem(RetrofitConstants.REVISION.toString(),id,element)
    }

    /** /list Post **/
    suspend fun postItem(Item: element) : Response<element> {
        return RetrofitInstance.api.postElement(RetrofitConstants.REVISION.toString(),Item)
    }

    /** /list Patch **/
    suspend fun patchList(Item: TodoList) : Response<TodoList> {
        return RetrofitInstance.api.patchElement(RetrofitConstants.REVISION.toString(),Item)
    }


}