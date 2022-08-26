package com.android.kotlinmvvmtodolist.usecases

import android.app.Application
import androidx.lifecycle.LiveData
import com.android.kotlinmvvmtodolist.database.TaskEntry

class AllUseCasesRoom(application: Application) {
    private val deleteRepeatUseCase: DeleteRepeatUseCase
    private val insertUseCases: InsertUseCases
    private val updateUseCases: UpdateUseCases
    private val deleteUseCases: DeleteUseCases
    private val getListUseCases: GetListUsecases
    private val finishTaskUseCase: FinishTaskUseCase

    init {
        finishTaskUseCase = FinishTaskUseCase(application)
        getListUseCases = GetListUsecases(application)
        deleteUseCases = DeleteUseCases(application)
        updateUseCases = UpdateUseCases(application)
        insertUseCases = InsertUseCases(application)
        deleteRepeatUseCase = DeleteRepeatUseCase(application)
    }

    suspend fun finishTask(id: Int) {
        finishTaskUseCase.finish(id)
    }

    fun getAllDone(): LiveData<List<TaskEntry>> {
        return getListUseCases.getDoneList()
    }
    fun getAllPriorityTask(): LiveData<List<TaskEntry>> {
        return getListUseCases.getAllPriorityTask()
    }

    fun getList(): LiveData<List<TaskEntry>> {
        return getListUseCases.getList()
    }

    suspend fun deleteRepeat() {
        deleteRepeatUseCase.deleteRepeat()
    }

    suspend fun insert(taskEntry: TaskEntry) {
        insertUseCases.insert(taskEntry = taskEntry)
    }

    suspend fun update(taskEntry: TaskEntry) {
        updateUseCases.update(taskEntry = taskEntry)
    }

    suspend fun delete(taskEntry: TaskEntry) {
        deleteUseCases.delete(taskEntry)
    }
}