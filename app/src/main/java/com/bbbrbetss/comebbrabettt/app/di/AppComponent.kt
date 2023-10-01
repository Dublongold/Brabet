package com.bbbrbetss.comebbrabettt.app.di

import android.content.Context
import com.bbbrbetss.comebbrabettt.app.MainActivity
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Component(modules = [DispatchersModule::class, ViewModelModule::class])
@Singleton
interface AppComponent {

    fun inject(activity: MainActivity)

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance appContext: Context): AppComponent
    }
}
