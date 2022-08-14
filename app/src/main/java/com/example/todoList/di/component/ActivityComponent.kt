package com.example.todoList.di.component

import com.example.todoList.di.PerActivity
import com.example.todoList.di.module.ActivityModule
import dagger.Component

@PerActivity
@Component(modules = arrayOf(ActivityModule::class))
interface ActivityComponent
