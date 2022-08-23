package com.android.kotlinmvvmtodolist.usecases

import androidx.lifecycle.viewModelScope
import com.android.kotlinmvvmtodolist.database.TaskDatabase
import com.android.kotlinmvvmtodolist.database.TaskEntry
import com.android.kotlinmvvmtodolist.repository.DataBaseRepository
import com.android.kotlinmvvmtodolist.repository.RetrofitRepository
import com.android.kotlinmvvmtodolist.retrofitConnect.RetrofitConstants
import com.example.todolistyandex.model.Todo
import com.example.todolistyandex.model.TodoList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PatchUseCase {

    private val retrofitRepository: RetrofitRepository
    private val getRevisionUseCase : GetRevisionUseCase
    init {
        getRevisionUseCase = GetRevisionUseCase()
        retrofitRepository = RetrofitRepository()
    }

    suspend fun patchList(list: List<TaskEntry>) {
        retrofitRepository.patchList(toTodoList(list))
          getRevisionUseCase.getRevision()
    }
    fun toTodoList(getAllTasks: List<TaskEntry>): TodoList {
        var arr: ArrayList<Todo> = arrayListOf()
        for (element in getAllTasks) {
            arr.add(toTodo(element))
        }
        return TodoList(arr, RetrofitConstants.REVISION)
    }

    fun toTodo(todo: TaskEntry): Todo {
        return Todo(
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
}