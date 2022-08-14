package com.example.todoList.di.component

import com.example.todoList.di.MockHttpModule
import com.example.todoList.di.module.ApplicationModule
import com.example.todoList.di.module.RxThreadModule
import com.example.todoList.view.activity.MainActivityTest
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [(MockHttpModule::class), (ApplicationModule::class),
    (AndroidModule::class), (RxThreadModule::class)]
)
interface TestComponent : AppComponent {
    fun inject(activity: MainActivityTest)
}