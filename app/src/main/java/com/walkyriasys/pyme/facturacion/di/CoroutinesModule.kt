package com.dayoneapp.dayone.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Qualifier

@Module
@InstallIn(SingletonComponent::class)
object CoroutinesModule {
    @Provides
    @MainThreadDispatcher
    fun mainDispatcher(): CoroutineDispatcher = Dispatchers.Main

    @Provides
    @CPUThreadDispatcher
    fun defaultDispatcher(): CoroutineDispatcher = Dispatchers.Default

    @Provides
    @IOThreadDispatcher
    fun ioDispatcher(): CoroutineDispatcher = Dispatchers.IO
}

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class MainThreadDispatcher

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class CPUThreadDispatcher

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class IOThreadDispatcher
