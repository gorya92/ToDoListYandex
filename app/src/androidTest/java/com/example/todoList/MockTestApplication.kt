package com.example.todoList

import com.example.todoList.di.MockHttpModule
import com.example.todoList.di.component.DaggerTestComponent
import com.example.todoList.di.module.ApplicationModule

class MockTestApplication : MainApplication() {

    override fun createComponent() {
        component=  DaggerTestComponent.builder()
                .applicationModule(ApplicationModule(this))
                .mockHttpModule(MockHttpModule())
                .build()
    }
}