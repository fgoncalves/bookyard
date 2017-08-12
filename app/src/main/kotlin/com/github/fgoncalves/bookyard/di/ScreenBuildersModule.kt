package com.github.fgoncalves.bookyard.di

import com.github.fgoncalves.bookyard.presentation.screens.SplashScreen
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ScreenBuildersModule {
  @ContributesAndroidInjector
  abstract fun contributesSplashScreen(): SplashScreen
}
