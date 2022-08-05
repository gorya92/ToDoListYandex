package com.example.todolistyandex.presenters

import android.opengl.Visibility
import android.provider.Settings
import android.util.Log
import com.example.todolistyandex.model.TodoItemRepository
import com.example.todolistyandex.views.TodoListView
import com.example.yandextask.model.TodoItem
import moxy.InjectViewState
import moxy.MvpPresenter
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


@InjectViewState()
class TodoListPresenter : MvpPresenter<TodoListView>() {
    var todoListVisible : ArrayList<TodoItem> = arrayListOf()
    private var visibility: Boolean = false
    var start: Boolean = true

    fun initializeAllLists() {

        if (start) {
            initializeTodoList()
            start = !start
        }
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
            created_at = dateTime
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
                    created_at = dateTime
                )
            )
        }
    }

    private fun initializeVisibilityList() {
        /** Список только с невыполненными делами **/
        todoListVisible.clear()

        for (i in 0 until TodoItemRepository.todoList.size) {
            if (!TodoItemRepository.todoList[i].done) {
                todoListVisible.add(TodoItemRepository.todoList[i])
            }
        }
    }

    fun changeCompleted() {
        viewState.setCompletedText(TodoItemRepository.todoList.size - todoListVisible.size)
    }

    fun changeVisibility() {
        if (!visibility) {
            viewState.setVisibility()
            visibility = true
        } else {
            viewState.setNoVisibility()
            visibility = false
        }

    }

    fun changeMenuVisibility() {
        viewState.changeMenuVisibility(visibility)

    }
}