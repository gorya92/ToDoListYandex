package com.example.todolistyandex.model

import com.google.gson.annotations.SerializedName

data class Todo(
    @SerializedName("changed_at")val changed_at: Long=0,
    @SerializedName("color") val color: String = "",
    @SerializedName("created_at")val created_at: Long,
    @SerializedName("deadline")val deadline: Long = 0,
    @SerializedName("done") var done: Boolean = false,
    @SerializedName("id") val id: String,
    @SerializedName("importance")val importance: String,
    @SerializedName("last_updated_by")val last_updated_by: String,
    @SerializedName("text")val text: String
)