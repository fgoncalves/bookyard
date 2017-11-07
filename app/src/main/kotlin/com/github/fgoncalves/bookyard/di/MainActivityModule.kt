package com.github.fgoncalves.bookyard.di

import android.support.v4.app.FragmentManager
import com.github.fgoncalves.bookyard.MainActivity
import com.github.fgoncalves.bookyard.di.scopes.ActivityScope
import dagger.Module
import dagger.Provides

@Module(includes = arrayOf(
        ViewModelModule::class,
        NavigatorModule::class))
object MainActivityModule {
    @Provides
    @ActivityScope
    @JvmStatic
    fun providesFragmentManager(
            activity: MainActivity): FragmentManager = activity.supportFragmentManager
}
