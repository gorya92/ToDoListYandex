package com.android.kotlinmvvmtodolist.repository

import androidx.lifecycle.LiveData
import com.android.kotlinmvvmtodolist.database.TaskDao
import com.android.kotlinmvvmtodolist.database.TaskEntry

class DataBaseRepository(val taskDao: TaskDao) {

    suspend fun insert(todo: TaskEntry) = taskDao.insert(todo)

    suspend fun insertList(todoList: List<TaskEntry>) = taskDao.insertList(todoList)

    fun getAllTaskList() : List<TaskEntry> = taskDao.getAllTasksList()
    fun getAllDoneTaskList() : List<TaskEntry> = taskDao.getAllDoneTasksList()

    suspend fun updateData(todo: TaskEntry) = taskDao.update(todo)

    suspend fun updateDataList(todoList: List<TaskEntry>) = taskDao.updateList(todoList)

    suspend fun deleteItem(todo: TaskEntry) = taskDao.delete(todo)

    suspend fun deleteAll() {
        taskDao.deleteAll()
    }

    suspend fun finishTask(uuid: Int) {
        taskDao.finishTask(uuid)
    }

    fun deletes() {
        taskDao.deletes()
    }

    fun getAllTasks(): LiveData<List<TaskEntry>> = taskDao.getAllTasks()

    fun getAllPriorityTasks(): LiveData<List<TaskEntry>> = taskDao.getAllPriorityTasks()

    fun getAllDone(): LiveData<List<TaskEntry>> = taskDao.getAllDoneTasks()

    fun searchDatabase(searchQuery: String): LiveData<List<TaskEntry>> {
        return taskDao.searchDatabase(searchQuery)
    }
}