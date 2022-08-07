package com.example.todolistyandex.presenters

import android.util.Log
import androidx.work.ListenableWorker.Result.retry
import com.example.todolistyandex.model.TodoItemRepository
import com.example.todolistyandex.views.TodoListView
import com.example.todolistyandex.model.Todo
import com.example.todolistyandex.model.TodoList
import com.example.yandextask.model.TodoItem
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import moxy.InjectViewState
import moxy.MvpPresenter
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import kotlin.collections.ArrayList
import kotlin.coroutines.CoroutineContext


@InjectViewState()
class TodoListPresenter : MvpPresenter<TodoListView>() {
    var todoListVisible: ArrayList<TodoItem> = arrayListOf()
    var visibility: Boolean = false
    var start: Boolean = true



    fun initializeAllLists() {


        initializeVisibilityList()
        viewState.startApp()

    }


    private fun initializeTodoList() {

        val dateTime = LocalDateTime.now()
            .format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
        Log.d("Date", dateTime)

        var todoItem3 = TodoItem(
            id = "0",
            "большой текст большой текстм большой текст большой т",
            "basic",
            deadline = "27/07/2022",
            done = false,
            created_at = dateTime,
            lastUpdated = ""
        )

        TodoItemRepository.todoList.add(todoItem3)

        for (i in 0 until 30) {
            var importance = "basic"
            var done = false
            if ((0..8).random() == 1) {
                importance = "important"
            } else
                if ((0..6).random() == 1) {
                    importance = "low"
                }
            if ((0..3).random() == 1) {
                done = true
            }
            var deadline = ""
            if (i % 3 == 0) {
                deadline = "27/09/2022"
            }
            TodoItemRepository.todoList.add(
                TodoItem(
                    id = (i + 1).toString(),
                    "blablabla",
                    importance = importance,
                    done = done,
                    deadline = deadline,
                    created_at = dateTime,
                    lastUpdated = ""
                )
            )
        }
    }

    private fun initializeVisibilityList() {

        /** Список только с невыполненными делами **/
        todoListVisible.clear()
        Log.d("error", TodoItemRepository.todoList.toString())

        for (i in 0 until TodoItemRepository.todoList.size) {
            if (!TodoItemRepository.todoList[i].done) {
                todoListVisible.add(TodoItemRepository.todoList[i])
            }
        }
        Log.d("errors", todoListVisible.toString())
        Log.d("errors", TodoItemRepository.todoList.toString())
        TodoItemRepository.todoListVisibility = todoListVisible
        changeCompleted()

    }

    fun changeCompleted() {
        viewState.setCompletedText(TodoItemRepository.todoList.size - todoListVisible.size)
    }

    fun changeVisibility() {
        if (!visibility) {
            visibility = true
            viewState.setVisibility()

        } else {
            visibility = false
            viewState.setNoVisibility()
        }

    }

    fun changeMenuVisibility() {
        viewState.changeMenuVisibility(visibility)
    }


    fun toTodoList(): TodoList {
        var list: ArrayList<Todo> = TodoItemRepository.todoListRequest.list

        for (i in 0 until TodoItemRepository.todoList.size) {

            var id = TodoItemRepository.todoList[i].id
            var title = TodoItemRepository.todoList[i].title
            var importance: String = TodoItemRepository.todoList[i].importance
            var deadline: Long = 0
            if (TodoItemRepository.todoList[i].deadline != "") {
                deadline = convertDateToLong(TodoItemRepository.todoList[i].deadline)
            }
            val done: Boolean
            var created_at: Long
            done = TodoItemRepository.todoList[i].done.toString() == "true"
            if (TodoItemRepository.todoList[i].created_at != "")
                created_at = convertDateToLong(TodoItemRepository.todoList[i].created_at)
            else
                created_at = 0

            list.add(
                Todo(
                    id = id,
                    text = title,
                    importance = importance,
                    deadline = deadline,
                    done = done,
                    created_at = created_at,
                    changed_at = System.currentTimeMillis(),
                    last_updated_by = list[i].last_updated_by
                )
            )

        }

        return (TodoList(list, 0))
    }

    fun convertDateToLong(date: String): Long {
        val l = LocalDate.parse(date, DateTimeFormatter.ofPattern("dd/MM/yyy"))

        val unix = l.atStartOfDay(ZoneId.systemDefault()).toInstant().epochSecond
        return unix
    }
}