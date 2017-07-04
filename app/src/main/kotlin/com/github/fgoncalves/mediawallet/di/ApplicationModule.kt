package com.github.fgoncalves.mediawallet.di

import android.content.Context
import com.github.fgoncalves.mediawallet.data.di.ApiClientsModule
import com.github.fgoncalves.mediawallet.di.qualifiers.ApplicationContext
import dagger.Module

@Module(includes = arrayOf(ApiClientsModule::class))
class ApplicationModule(val context: Context) {
  @ApplicationContext
  fun providesApplicationContext(): Context = context
}
