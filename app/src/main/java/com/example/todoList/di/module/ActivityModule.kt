package com.example.todoList.di.module

import android.app.Activity
import android.content.Context

import com.example.todoList.di.ActivityContext
import com.example.todoList.di.PerActivity

import dagger.Module
import dagger.Provides

@Module
class ActivityModule(private val activity: Activity) {

    @PerActivity
    @Provides
    @ActivityContext
    fun provideContext(): Context = activity
}
