package com.android.kotlinmvvmtodolist

import android.os.Build
import com.android.kotlinmvvmtodolist.database.TaskEntry
import java.util.*
import java.util.concurrent.ThreadLocalRandom

object DataProvider {

    fun getProducts(count: Int): List<TaskEntry> {

        val products = mutableListOf<TaskEntry>()
        for (i in 0..count) {
            products.add(getProduct(id = i))
        }
        return products
    }

    fun getProduct(id: Int): TaskEntry = TaskEntry(
        ids = 0,
        text = df.randomString(),
        importance = "low",
        deadline = df.randomInt().toLong(),
        created_at = (id+1).toLong(),
        color = "",
        changed_at = df.randomInt().toLong(),
        done = df.randomDone(),
        id = id.toString(),
        last_updated_by = df.randomString()
    )
}
object DataFactory {

    fun randomString(): String {
        return UUID.randomUUID().toString()
    }
    fun randomDone():Boolean
    {
        return randomInt()%2==0
    }

    fun randomInt(): Int {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ThreadLocalRandom.current().nextInt(0, 1000 + 1)
        } else {
            Random().nextInt(1000 + 1)
        }
    }

    fun randomDouble(): Double {
        return randomInt().toDouble()
    }
}

typealias df = DataFactory
