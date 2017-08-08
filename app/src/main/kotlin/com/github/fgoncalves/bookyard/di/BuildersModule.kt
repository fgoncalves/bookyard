package com.github.fgoncalves.bookyard.di

import com.github.fgoncalves.bookyard.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class BuildersModule {
  @ContributesAndroidInjector(modules = arrayOf(MainActivityModule::class))
  abstract fun contributesMainActivity(): MainActivity
}
