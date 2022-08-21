package com.example.todolistyandex.model

import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

data class TodoList(
    val list: ArrayList<Todo>,
    val revision: Int
) 
