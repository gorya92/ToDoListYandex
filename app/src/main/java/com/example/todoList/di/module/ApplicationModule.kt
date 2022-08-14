package com.example.todoList.di.module


import android.app.Application
import com.example.todoList.di.ApplicationContext
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ApplicationModule(private val application: Application) {

    @Provides
    @Singleton
    @ApplicationContext
    fun provideContext(): Application = this.application




}
