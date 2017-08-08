package com.github.fgoncalves.bookyard.di

import com.github.fgoncalves.bookyard.BookYardApplication
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.support.AndroidSupportInjectionModule

@Component(modules = arrayOf(
    AndroidSupportInjectionModule::class,
    AndroidInjectionModule::class,
    ApplicationModule::class,
    BuildersModule::class))
interface ApplicationComponent {
  @dagger.Component.Builder
  interface Builder {
    @BindsInstance
    fun application(application: BookYardApplication): Builder

    fun build(): ApplicationComponent
  }
}
