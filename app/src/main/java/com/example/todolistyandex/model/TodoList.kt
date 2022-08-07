package com.example.todolistyandex.model

import com.example.yandextask.model.TodoItem
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

data class TodoList(
    val list: ArrayList<Todo>,
    val revision: Int,
) {

    fun convertLongToTime(time: Long): String {
        val sdf = SimpleDateFormat("dd/MM/yyyy")
        val date = java.util.Date(time * 1000)
        return sdf.format(date)
    }

     fun todoListToRepository(list: List<Todo>): List<TodoItem> {
        var arr: ArrayList<TodoItem> = TodoItemRepository.todoList
        arr.clear()
        for (i in 0 until list.size) {

            var id = list[i].id
            var title = list[i].text
            var importance: String = list[i].importance
            var deadline: String = ""
            if (list[i].deadline != 0.toLong()) {
                deadline = convertLongToTime(list[i].deadline)
            }
            val done: Boolean = list[i].done
            var created_at: String = convertLongToTime(list[i].created_at)
            var changed_at: String = convertLongToTime(list[i].changed_at)
            arr.add(
                TodoItem(
                    id = id,
                    title = title,
                    importance = importance,
                    deadline = deadline,
                    done = done,
                    created_at = created_at,
                    changed_at = changed_at,
                    lastUpdated = list[i].last_updated_by
                )
            )
        }
        return arr
    }


}
