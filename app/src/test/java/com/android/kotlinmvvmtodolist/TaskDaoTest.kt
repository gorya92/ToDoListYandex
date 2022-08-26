package com.android.kotlinm

import com.android.kotlinmvvmtodolist.DataProvider


import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.android.kotlinmvvmtodolist.database.TaskDatabase
import com.android.kotlinmvvmtodolist.database.TaskEntry
import com.android.kotlinmvvmtodolist.repository.DataBaseRepository
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@Config(manifest = Config.NONE)
@RunWith(RobolectricTestRunner::class)
class TaskDaoTest {
    private lateinit var db: TaskDatabase

    private lateinit var repository: DataBaseRepository

    @JvmField
    @Rule
    val instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        val context: Context = ApplicationProvider.getApplicationContext()

        db = Room.inMemoryDatabaseBuilder(context, TaskDatabase::class.java).allowMainThreadQueries()
            .build()

        repository = DataBaseRepository(db.taskDao())
    }

    @Test
    fun insert() {
        runBlocking {
            val testProducts = DataProvider.getProducts(0)
            repository.insertList(testProducts)

            val cacheProducts = repository.getAllTaskList()


                assertEquals(cacheProducts[0].text, testProducts[0].text)


        }
    }

    @Test
    fun equalDones() {
        runBlocking {
            val testProducts = DataProvider.getProducts(5)
            repository.insertList(testProducts)

            val expectedImportant  : ArrayList<TaskEntry> = arrayListOf()
            testProducts.forEach {
                if (it.done)
                expectedImportant.add(it)}

            val done = repository.getAllDoneTaskList()

            for (i in 0 until expectedImportant.size) {
                assertEquals(done[i].done, expectedImportant[i].done)
            }


        }
    }
    @Test
    fun delete(){
        runBlocking {
            val testProducts = DataProvider.getProducts(5)
            repository.insertList(testProducts)

            val expectedImportant  : ArrayList<TaskEntry> = arrayListOf()
            testProducts.forEach {
                    expectedImportant.add(it)}
            repository.deleteItem(expectedImportant[0])
            expectedImportant.remove(expectedImportant[0])

            val done = repository.getAllTaskList()

            assertEquals(done[0].id, expectedImportant.reversed()[0].id)

        }
    }

    @After
    fun tearDown() {
        db.close()
    }



}