package com.example.todolistyandex.model

import android.os.Parcel
import android.os.Parcelable
import com.example.yandextask.model.TodoItem

class TodoItemsRepository() : ArrayList<TodoItem>(), Parcelable {
    constructor(parcel: Parcel) : this() {
    }


    override fun writeToParcel(parcel: Parcel, flags: Int) {

    }


    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<TodoItemsRepository> {
        override fun createFromParcel(parcel: Parcel): TodoItemsRepository {
            return TodoItemsRepository(parcel)
        }

        override fun newArray(size: Int): Array<TodoItemsRepository?> {
            return arrayOfNulls(size)
        }
    }
}
