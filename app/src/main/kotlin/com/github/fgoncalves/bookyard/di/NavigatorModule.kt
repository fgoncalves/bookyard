package com.github.fgoncalves.bookyard.di

import com.github.fgoncalves.bookyard.di.scopes.ActivityScope
import com.github.fgoncalves.bookyard.presentation.utils.ScreenNavigator
import com.github.fgoncalves.bookyard.presentation.utils.ScreenNavigatorImpl
import dagger.Binds
import dagger.Module

@Module
abstract class NavigatorModule {
  @Binds
  @ActivityScope
  abstract fun providesScreenNavigator(navigator: ScreenNavigatorImpl): ScreenNavigator
}
