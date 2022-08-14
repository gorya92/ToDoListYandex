package com.example.todoList

import android.app.Application
import com.example.todoList.di.component.AppComponent
import com.example.todoList.di.component.DaggerAppComponent
import com.example.todoList.di.module.ApplicationModule
import com.example.todoList.di.module.HttpModule

open class MainApplication : Application() {

    lateinit var component: AppComponent

    override fun onCreate() {
        super.onCreate()

        createComponent()
    }

    protected open fun createComponent() {
        component = DaggerAppComponent.builder()
                .applicationModule(ApplicationModule(this))
                .httpModule(HttpModule())
                .build()
    }
}

