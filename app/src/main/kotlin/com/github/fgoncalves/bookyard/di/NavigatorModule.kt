package com.github.fgoncalves.bookyard.di

import androidx.fragment.app.FragmentManager
import com.github.fgoncalves.bookyard.di.qualifiers.ScreenContainerId
import com.github.fgoncalves.bookyard.di.scopes.ActivityScope
import com.github.fgoncalves.bookyard.presentation.utils.ScreenNavigator
import com.github.fgoncalves.bookyard.presentation.utils.ScreenNavigatorImpl
import dagger.Module
import dagger.Provides

@Module
object NavigatorModule {
    @Provides
    @ActivityScope
    @JvmStatic
    fun providesScreenNavigator(
            fragmentManager: FragmentManager,
            @ScreenContainerId container: Int): ScreenNavigator = ScreenNavigatorImpl(fragmentManager, container)
}
