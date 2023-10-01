package com.bbbrbetss.comebbrabettt.app.di

import androidx.lifecycle.ViewModel
import com.bbbrbetss.comebbrabettt.app.screens.game.screen.GameScreenViewModel
import com.bbbrbetss.comebbrabettt.app.screens.levels.LevelsScreenViewModel
import com.bbbrbetss.comebbrabettt.app.screens.main.MainScreenViewModel
import com.bbbrbetss.comebbrabettt.app.screens.splash.SplashScreenViewModel
import dagger.Binds
import dagger.MapKey
import dagger.Module
import dagger.multibindings.IntoMap
import kotlin.reflect.KClass

@Retention(AnnotationRetention.RUNTIME)
@MapKey
annotation class ViewModelKey(val value: KClass<out ViewModel>)

@Module
interface ViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(SplashScreenViewModel::class)
    fun bindSplashScreenViewModel(viewModel: SplashScreenViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(MainScreenViewModel::class)
    fun bindMainScreenViewModel(viewModel: MainScreenViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(LevelsScreenViewModel::class)
    fun bindLevelsScreenViewModel(viewModel: LevelsScreenViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(GameScreenViewModel::class)
    fun bindGameScreenViewModel(viewModel: GameScreenViewModel): ViewModel
}
