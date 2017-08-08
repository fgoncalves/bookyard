package com.github.fgoncalves.bookyard.di

import android.content.Context
import com.github.fgoncalves.bookyard.BookYardApplication
import com.github.fgoncalves.bookyard.data.di.ApiClientsModule
import com.github.fgoncalves.bookyard.di.qualifiers.ApplicationContext
import com.github.fgoncalves.bookyard.domain.di.UseCaseModule
import dagger.Module

@Module(includes = arrayOf(
    ApiClientsModule::class,
    UseCaseModule::class))
class ApplicationModule() {
  @ApplicationContext
  fun providesApplicationContext(application: BookYardApplication): Context = application
}
