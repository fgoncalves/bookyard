package com.github.fgoncalves.bookyard.di

import com.github.fgoncalves.bookyard.MainActivity
import com.github.fgoncalves.bookyard.di.scopes.ActivityScope
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class BuildersModule {
    @ActivityScope
    @ContributesAndroidInjector(modules = arrayOf(
            MainActivityModule::class,
            ScreenBuildersModule::class))
    abstract fun contributesMainActivity(): MainActivity
}
