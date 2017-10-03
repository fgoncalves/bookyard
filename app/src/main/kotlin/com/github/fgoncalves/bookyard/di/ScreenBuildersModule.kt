package com.github.fgoncalves.bookyard.di

import com.github.fgoncalves.bookyard.presentation.screens.BookDetailsScreen
import com.github.fgoncalves.bookyard.presentation.screens.HomeScreen
import com.github.fgoncalves.bookyard.presentation.screens.SplashScreen
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ScreenBuildersModule {
    @ContributesAndroidInjector
    abstract fun contributesSplashScreen(): SplashScreen

    @ContributesAndroidInjector
    abstract fun contributesHomeScreen(): HomeScreen

    @ContributesAndroidInjector
    abstract fun contributesBookDetailsScreen(): BookDetailsScreen
}
