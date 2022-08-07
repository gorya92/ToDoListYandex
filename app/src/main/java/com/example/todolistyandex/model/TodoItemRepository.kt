package com.example.todolistyandex.model

import com.example.yandextask.model.TodoItem
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

object TodoItemRepository {
    var todoList : ArrayList<TodoItem> = arrayListOf()
    lateinit var todoListRequest : TodoList
    var todoListVisibility : ArrayList<TodoItem> = arrayListOf()


}