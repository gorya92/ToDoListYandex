package com.example.todoList.di.component

import com.example.todoList.di.module.ApplicationModule
import com.example.todoList.di.module.HttpModule
import com.example.todoList.di.module.RxThreadModule
import com.example.todoList.view.activity.MainActivity
import com.example.todoList.view.fragment.MainFragment
import com.example.todoList.view.fragment.RepoFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [HttpModule::class, ApplicationModule::class, RxThreadModule::class])
interface AppComponent {
    fun inject(fragment: MainFragment)

    fun inject(activity: MainActivity)

    fun inject(fragment: RepoFragment)
}