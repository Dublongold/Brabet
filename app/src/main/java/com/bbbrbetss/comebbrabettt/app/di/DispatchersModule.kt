package com.bbbrbetss.comebbrabettt.app.di

import com.bbbrbetss.comebbrabettt.app.core.coroutines.DispatchersProvider
import dagger.Module
import dagger.Provides
import dagger.Reusable
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

@Module
class DispatchersModule {
    @Provides
    @Reusable
    fun provideDispatchersProvider(): DispatchersProvider {
        return object : DispatchersProvider {
            override val main: CoroutineDispatcher = Dispatchers.Main
            override val io: CoroutineDispatcher = Dispatchers.IO
            override val default: CoroutineDispatcher = Dispatchers.Default
            override val unconfined: CoroutineDispatcher = Dispatchers.Unconfined
        }
    }
}
