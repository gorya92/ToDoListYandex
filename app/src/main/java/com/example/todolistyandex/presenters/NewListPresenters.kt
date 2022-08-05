package com.example.todolistyandex.presenters

import android.os.Bundle
import androidx.constraintlayout.motion.utils.ViewState
import com.example.todolistyandex.model.TodoItemRepository
import com.example.todolistyandex.views.NewItemView
import com.example.yandextask.model.TodoItem
import moxy.InjectViewState
import moxy.MvpPresenter
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList

@InjectViewState()
class NewListPresenters : MvpPresenter<NewItemView>() {
    var cal: Calendar = Calendar.getInstance() // Календарь
    var startArr: ArrayList<TodoItem> = arrayListOf()
    var changeOrAddItem = TodoItem(
        id = "0",
        "большой текст большой текстм большой текст большой т",
        "basic",
        deadline = "27/07/2022",
        done = false,
        created_at = ""

    )

    fun calendarGet() {
        val myFormat = "dd/MM/yyyy" // mention the format you need
        val sdf = SimpleDateFormat(myFormat, Locale.FRANCE)
        var PeriodDate = cal.time
        viewState.setDate(PeriodDate?.let { sdf.format(it) })
    }


    fun startArr() {
        startArr = TodoItemRepository.todoList
    }

    fun newItem(id: String, dealText: String, important: String, date: String) {
        changeOrAddItem.id = id
        changeOrAddItem.deadline = date
        changeOrAddItem.title = dealText
        changeOrAddItem.deadline = date
        changeOrAddItem.created_at = LocalDateTime.now()
            .format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
        TodoItemRepository.todoList.add(changeOrAddItem)

    }

    fun changeItem(id: String, dealText: String, important: String, date: String) {
        changeOrAddItem = TodoItemRepository.todoList[id.toInt()]
        changeOrAddItem.deadline = date
        changeOrAddItem.title = dealText
        changeOrAddItem.deadline = date
        changeOrAddItem.changed_at = LocalDateTime.now()
            .format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))

        TodoItemRepository.todoList[id.toInt()] = changeOrAddItem
    }

    fun removeItem(id: Int) {
        TodoItemRepository.todoList.removeAt(id)
    }

    fun close() {
        TodoItemRepository.todoList = startArr
    }
}
