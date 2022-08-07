package com.example.yandextask.model

import android.os.Parcel
import android.os.Parcelable
import android.util.Log
import com.example.todolistyandex.model.Todo
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

data class TodoItem(
    var id: String,
    var title: String,
    var importance: String,
    var deadline: String = "01/01/2200",
    var done: Boolean,
    var created_at: String,
    var changed_at: String = "",
    val lastUpdated: String
) {

    fun todoItemToTodo(TodoItem: TodoItem): Todo {
        var id = TodoItem.id
        var title = TodoItem.title
        var importance: String = TodoItem.importance
        var deadline: Long = 0
        Log.d("DATES", TodoItem.deadline)
        if (TodoItem.deadline != "") {
            deadline = convertDateToLong(TodoItem.deadline)
        }

        val done: Boolean = TodoItem.done
        var created_at: Long = 0
        if (TodoItem.created_at != "")
            created_at = convertDateToLong(TodoItem.created_at)
        var changed_at: Long = convertDateToLong(
            LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
        )

        return Todo(
            id = id,
            text = title,
            importance = importance,
            deadline = deadline,
            done = done,
            created_at = created_at,
            color = "",
            changed_at = System.currentTimeMillis(),
            last_updated_by = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
        )

    }

    fun convertDateToLong(date: String): Long {
        val l = LocalDate.parse(date, DateTimeFormatter.ofPattern("dd/MM/yyy"))

        val unix = l.atStartOfDay(ZoneId.systemDefault()).toInstant().epochSecond
        return unix
    }

    override fun equals(other: Any?): Boolean {

        if (javaClass != other?.javaClass) {
            return false
        }
        other as TodoItem
        if (id.toInt() != other.id.toInt()) {
            return false
        }
        if (title != other.title) {
            return false
        }
        if (importance != importance) {
            return false
        }
        if (deadline != other.deadline) {
            return false
        }
        if (done != other.done) {
            return false
        }
        if (created_at != other.created_at) {
            return false
        }
        if (changed_at != other.changed_at) {
            return false
        }


        return true
    }
}