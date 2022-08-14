package com.example.todoList.di

import androidx.browser.customtabs.CustomTabsIntent

import javax.inject.Singleton

import dagger.Module
import dagger.Provides
import org.mockito.Mockito.mock

@Module
class MockAndroidModule {

    @Provides
    @Singleton
    fun provideCustomTabsBuilder(): CustomTabsIntent.Builder = mock(CustomTabsIntent.Builder::class.java)

    @Provides
    @Singleton
    fun provideCustomTabsIntent(): CustomTabsIntent = mock(CustomTabsIntent::class.java)

}
