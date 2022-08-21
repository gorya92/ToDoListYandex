package com.android.kotlinmvvmtodolist.usecases

import android.app.Application
import androidx.lifecycle.viewModelScope
import com.android.kotlinmvvmtodolist.database.TaskDatabase
import com.android.kotlinmvvmtodolist.database.TaskEntry
import com.android.kotlinmvvmtodolist.repository.DataBaseRepository
import com.android.kotlinmvvmtodolist.repository.RetrofitRepository
import com.android.kotlinmvvmtodolist.retrofitConnect.RetrofitConstants
import com.android.kotlinmvvmtodolist.retrofitConnect.model.element
import com.example.todolistyandex.model.Todo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class InsertUseCases(application: Application) {
    private val taskDao = TaskDatabase.getDatabase(application).taskDao()
    private val repository: DataBaseRepository
    private val retrofitRepository: RetrofitRepository
    private val getRevisionUseCase : GetRevisionUseCase
    init {
        getRevisionUseCase = GetRevisionUseCase()
        retrofitRepository = RetrofitRepository()
        repository = DataBaseRepository(taskDao)
    }
    suspend fun insert(taskEntry: TaskEntry) {
            repository.insert(taskEntry)

    }
    suspend fun insertRetrofit(taskEntry: TaskEntry){
        retrofitRepository.postItem(toElement(taskEntry))
        getRevisionUseCase.getRevision()
    }
   private fun toElement(todo: TaskEntry): element {
        return element(
            Todo(
                changed_at = todo.changed_at,
                created_at = todo.created_at,
                id = todo.id,
                text = todo.text,
                deadline = todo.deadline,
                done = todo.done,
                last_updated_by = todo.last_updated_by,
                importance = todo.importance,
                color = todo.color
            ), RetrofitConstants.REVISION.toString()
        )
    }
}