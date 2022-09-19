package com.android.kotlinmvvmtodolist.usecases

import android.app.Application
import androidx.lifecycle.LiveData
import com.android.kotlinmvvmtodolist.database.TaskDatabase
import com.android.kotlinmvvmtodolist.database.TaskEntry
import com.android.kotlinmvvmtodolist.repository.DataBaseRepository
import com.android.kotlinmvvmtodolist.repository.RetrofitRepository
import com.android.kotlinmvvmtodolist.retrofitConnect.RetrofitConstants
import com.example.todolistyandex.model.Todo
import com.example.todolistyandex.model.TodoList

class GetListUsecases(application: Application) {
    private val taskDao = TaskDatabase.getDatabase(application).taskDao()
    private val repository: DataBaseRepository
    private val retrofitRepository: RetrofitRepository

    init {
        retrofitRepository = RetrofitRepository()
        repository = DataBaseRepository(taskDao)
    }

    fun getDoneList(): LiveData<List<TaskEntry>> {
        return repository.getAllDone()
    }

    fun getList(): LiveData<List<TaskEntry>> {
        return repository.getAllTasks()
    }
    fun getAllPriorityTask(): LiveData<List<TaskEntry>>{
        return repository.getAllPriorityTasks()
    }

    suspend fun getListRetrofit() {
        val response = retrofitRepository.getList()
        RetrofitConstants.REVISION = response.body()!!.revision
        repository.insertList(toTaskEntryList(response.body()))
    }

    private fun totaskEntry(todo: Todo): TaskEntry {
        return TaskEntry(
            ids = 0,
            changed_at = todo.changed_at,
            created_at = todo.created_at,
            id = todo.id,
            text = todo.text,
            deadline = todo.deadline,
            done = todo.done,
            last_updated_by = todo.last_updated_by,
            importance = todo.importance,
            color = todo.color
        )
    }

    private fun toTaskEntryList(todoList: TodoList?): List<TaskEntry> {
        var list: MutableList<TaskEntry> = mutableListOf()
        if (todoList != null) {
            for (i in 0 until todoList.list.size) {
                list.add(totaskEntry(todoList.list[i]))
            }
        }
        return list
    }
}