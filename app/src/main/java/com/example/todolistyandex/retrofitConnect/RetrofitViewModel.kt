package com.bignerdranch.android.testing.retrofitConnect

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bignerdranch.android.testing.retrofitConnect.repository.Repository
import com.example.todolistyandex.model.Todo
import com.example.todolistyandex.model.TodoList
import com.example.todolistyandex.model.element
import com.example.yandextask.model.TodoItem
import kotlinx.coroutines.launch
import retrofit2.Response

class RetrofitViewModel(private val repository: Repository): ViewModel() {

    /** /list **/
    val ListResponse: MutableLiveData<Response<TodoList>> = MutableLiveData()

    fun getList(    ) {
        viewModelScope.launch {
            val response: Response<TodoList> = repository.getList()
            ListResponse.value = response
        }
    }
    /** /list **/
    val Item: MutableLiveData<Response<element>> = MutableLiveData()

    fun getItem( string: String   ) {
        viewModelScope.launch {
            val response: Response<element> = repository.getItem(string)
            Item.value = response
        }
    }

    /** /list Delete **/
    val deleteItem: MutableLiveData<Response<element>> = MutableLiveData()

    fun deleteItem( id : String  ) {
        viewModelScope.launch {
            val response: Response<element> = repository.deleteItem(id)
            deleteItem.value = response
        }
    }

    /** /Item Put **/
    val putItem: MutableLiveData<Response<element>> = MutableLiveData()

    fun putItem( id : String ,element: element ) {
        viewModelScope.launch {
            val response: Response<element> = repository.putItem(id,element)
            putItem.value = response
        }
    }


    /** /list Put**/
    val putItemResponse: MutableLiveData<Response<element>> = MutableLiveData()

    fun postList(Item: element) {
        viewModelScope.launch {
            val response: Response<element> = repository.postItem(Item)
            putItemResponse.value = response
        }
    }

    /** /list Patch**/
    val patchListResponse: MutableLiveData<Response<TodoList>> = MutableLiveData()

    fun patchList(Item: TodoList) {
        viewModelScope.launch {
            val response: Response<TodoList> = repository.patchList(Item)
            patchListResponse.value = response
        }
    }


    }
