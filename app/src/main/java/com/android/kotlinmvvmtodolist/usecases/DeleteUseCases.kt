package com.android.kotlinmvvmtodolist.usecases

import android.app.Application
import com.android.kotlinmvvmtodolist.database.TaskDatabase
import com.android.kotlinmvvmtodolist.database.TaskEntry
import com.android.kotlinmvvmtodolist.repository.DataBaseRepository
import com.android.kotlinmvvmtodolist.repository.RetrofitRepository

class DeleteUseCases(application: Application) {
    private val taskDao = TaskDatabase.getDatabase(application).taskDao()
    private val repository: DataBaseRepository
    private val retrofitRepository: RetrofitRepository
    private val getRevisionUseCase: GetRevisionUseCase

    init {
        retrofitRepository = RetrofitRepository()
        repository = DataBaseRepository(taskDao)
        getRevisionUseCase = GetRevisionUseCase()
    }

    suspend fun delete(taskEntry: TaskEntry) {
        repository.deleteItem(taskEntry)

    }

    suspend fun deleteRetrofit(taskEntry: String) {
        var response = retrofitRepository.deleteItem(taskEntry)
        getRevisionUseCase.getRevision()
    }
}