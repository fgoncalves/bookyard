package com.github.fgoncalves.bookyard.di

import android.support.v4.app.FragmentManager
import com.github.fgoncalves.bookyard.di.qualifiers.ScreenContainerId
import com.github.fgoncalves.bookyard.di.scopes.ActivityScope
import com.github.fgoncalves.pathmanager.ScreenNavigator
import com.github.fgoncalves.pathmanager.ScreenNavigatorImpl
import dagger.Module
import dagger.Provides

@Module
object NavigatorModule {
  @Provides
  @ActivityScope
  @JvmStatic
  fun providesScreenNavigator(
      fragmentManager: FragmentManager,
      @ScreenContainerId container: Int): ScreenNavigator
      = ScreenNavigatorImpl(fragmentManager, container)
}
