package com.android.kotlinmvvmtodolist.usecases

import android.app.Application
import com.android.kotlinmvvmtodolist.database.TaskDatabase
import com.android.kotlinmvvmtodolist.repository.DataBaseRepository

class FinishTaskUseCase(application: Application) {
    private val taskDao = TaskDatabase.getDatabase(application).taskDao()
    private val repository: DataBaseRepository
    private val getRevisionUseCase: GetRevisionUseCase

    init {
        repository = DataBaseRepository(taskDao)
        getRevisionUseCase = GetRevisionUseCase()
    }

    suspend fun finish(id: Int) {
        repository.finishTask(id)
    }
}