package com.missapps.brabet.di

import android.content.Context
import com.missapps.brabet.MainActivity
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
