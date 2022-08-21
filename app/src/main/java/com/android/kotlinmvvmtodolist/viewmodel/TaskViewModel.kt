package com.android.kotlinmvvmtodolist.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.android.kotlinmvvmtodolist.database.TaskDatabase
import com.android.kotlinmvvmtodolist.database.TaskEntry
import com.android.kotlinmvvmtodolist.repository.DataBaseRepository
import com.android.kotlinmvvmtodolist.repository.RetrofitRepository
import com.android.kotlinmvvmtodolist.retrofitConnect.RetrofitConstants
import com.android.kotlinmvvmtodolist.retrofitConnect.model.element
import com.android.kotlinmvvmtodolist.usecases.*
import com.android.kotlinmvvmtodolist.util.NetworkUtil
import com.example.todolistyandex.model.Todo
import com.example.todolistyandex.model.TodoList
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TaskViewModel(application: Application) : AndroidViewModel(application) {

    private val allUseCasesRetrofit : AllUseCasesRetrofit
    private val allUseCasesRoom : AllUseCasesRoom

    var getAllTasks: LiveData<List<TaskEntry>>
    var getAllDoneTasks : LiveData<List<TaskEntry>>
    var visibility: Boolean = false


    init {
        allUseCasesRetrofit = AllUseCasesRetrofit(application)
        allUseCasesRoom = AllUseCasesRoom(application)
        getAllDoneTasks = allUseCasesRoom.getAllDone()
        getAllTasks = allUseCasesRoom.getList()

    }

    fun getList()  {
        if (NetworkUtil.getConnectivityStatus(context = getApplication())) {
            viewModelScope.launch(Dispatchers.IO) {
                allUseCasesRetrofit.getList()
            }
        }
    }

    fun deleteRepeat() {
        viewModelScope.launch(Dispatchers.IO) {
            allUseCasesRoom.deleteRepeat()
        }
    }

    fun insert(taskEntry: TaskEntry) {
        viewModelScope.launch(Dispatchers.IO) {
            allUseCasesRoom.insert(taskEntry)
            if (NetworkUtil.getConnectivityStatus(context = getApplication())) {
                allUseCasesRetrofit.insert(taskEntry)
            }
        }
    }

    fun patchList(list: List<TaskEntry>) {
        if (NetworkUtil.getConnectivityStatus(context = getApplication())) {
            viewModelScope.launch(Dispatchers.IO) {
                allUseCasesRetrofit.patch(list)
            }
        }
    }



    fun delete(taskEntry: TaskEntry) {
        viewModelScope.launch(Dispatchers.IO) {
            allUseCasesRoom.delete(taskEntry)
            if (NetworkUtil.getConnectivityStatus(context = getApplication())) {
                allUseCasesRetrofit.delete(taskEntry.id)
            }
        }
    }

    fun update(taskEntry: TaskEntry) {
        viewModelScope.launch(Dispatchers.IO) {
            allUseCasesRoom.update(taskEntry)
            if (NetworkUtil.getConnectivityStatus(context = getApplication())) {
                allUseCasesRetrofit.update(taskEntry)
            }
        }
    }

}