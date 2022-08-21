package com.android.kotlinmvvmtodolist.usecases

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.android.kotlinmvvmtodolist.database.TaskEntry
import com.android.kotlinmvvmtodolist.repository.DataBaseRepository
import com.android.kotlinmvvmtodolist.repository.RetrofitRepository
import com.example.todolistyandex.model.Todo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AllUseCasesRoom(application: Application) {
    private val deleteRepeatUseCase : DeleteRepeatUseCase
    private val insertUseCases : InsertUseCases
    private val updateUseCases : UpdateUseCases
    private val deleteUseCases : DeleteUseCases
    private val getListUseCases : GetListUsecases

    init {
        getListUseCases = GetListUsecases(application)
        deleteUseCases = DeleteUseCases(application)
        updateUseCases =  UpdateUseCases(application)
        insertUseCases = InsertUseCases(application)
        deleteRepeatUseCase = DeleteRepeatUseCase(application)
    }
    fun getAllDone(): LiveData<List<TaskEntry>> {
    return getListUseCases.getDoneList()
    }
    fun getList()  : LiveData<List<TaskEntry>>{
      return  getListUseCases.getList()
    }
    suspend fun deleteRepeat() {
            deleteRepeatUseCase.deleteRepeat()
    }
    suspend fun insert(taskEntry: TaskEntry){
        insertUseCases.insert(taskEntry = taskEntry)
    }
    suspend fun update(taskEntry: TaskEntry){
        updateUseCases.update(taskEntry = taskEntry)
    }
    suspend fun delete(taskEntry: TaskEntry){
        deleteUseCases.delete(taskEntry)
    }
}