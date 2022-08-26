package com.android.kotlinmvvmtodolist.worker

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.android.kotlinmvvmtodolist.database.TaskDatabase
import com.android.kotlinmvvmtodolist.database.TaskEntry
import com.android.kotlinmvvmtodolist.repository.DataBaseRepository
import com.android.kotlinmvvmtodolist.repository.RetrofitRepository
import com.android.kotlinmvvmtodolist.retrofitConnect.RetrofitConstants
import com.example.todolistyandex.model.Todo
import com.example.todolistyandex.model.TodoList

class Worker(context: Context, parameters: WorkerParameters) :
    CoroutineWorker(context, parameters) {
    var repository: RetrofitRepository
    private val taskDao = TaskDatabase.getDatabase(context).taskDao()
    private val repositoryBD: DataBaseRepository

    init {
        repositoryBD = DataBaseRepository(taskDao)
        repository = RetrofitRepository()
    }

    override suspend fun doWork(): Result {
        return try {
            try {
                Log.d("MyWorker", "Run work manager")
                //Do Your task here
                doYourTask()
                Result.success()
            } catch (e: Exception) {
                Log.d("MyWorker", "exception in doWork ${e.message}")
                Result.failure()
            }
        } catch (e: Exception) {
            Log.d("MyWorker", "exception in doWork ${e.message}")
            Result.failure()
        }
    }

    private suspend fun doYourTask() {
        val response = repository.getList()
        RetrofitConstants.REVISION = response.body()!!.revision
        repositoryBD.insertList(toTaskEntryList(response.body()))
        repositoryBD.deletes()
    }

    fun totaskEntry(todo: Todo): TaskEntry {
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

    fun toTaskEntryList(todoList: TodoList?): List<TaskEntry> {
        var list: MutableList<TaskEntry> = mutableListOf()
        if (todoList != null) {
            for (i in 0 until todoList.list.size) {
                list.add(totaskEntry(todoList.list[i]))
            }
        }
        return list
    }
}