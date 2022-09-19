package com.android.kotlinmvvmtodolist.usecases

import android.app.Application
import com.android.kotlinmvvmtodolist.database.TaskDatabase
import com.android.kotlinmvvmtodolist.repository.DataBaseRepository

class DeleteRepeatUseCase(application: Application) {

    private val taskDao = TaskDatabase.getDatabase(application).taskDao()
    private val repository: DataBaseRepository

    init {
        repository = DataBaseRepository(taskDao)
    }

    fun deleteRepeat() {
        repository.deletes()
    }
}