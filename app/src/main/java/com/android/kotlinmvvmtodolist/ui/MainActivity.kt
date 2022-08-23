package com.android.kotlinmvvmtodolist.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import com.android.kotlinmvvmtodolist.R
import com.android.kotlinmvvmtodolist.worker.WorkManagerScheduler


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        startWorkManager()
    }
    private fun startWorkManager() {
         WorkManagerScheduler.refreshPeriodicWork(this)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}