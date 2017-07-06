package com.github.fgoncalves.bookyard.di

import android.content.Context
import com.github.fgoncalves.bookyard.data.di.ApiClientsModule
import com.github.fgoncalves.bookyard.di.qualifiers.ApplicationContext
import dagger.Module

@Module(includes = arrayOf(ApiClientsModule::class))
class ApplicationModule(val context: Context) {
  @ApplicationContext
  fun providesApplicationContext(): Context = context
}
