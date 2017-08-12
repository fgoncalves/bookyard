package com.github.fgoncalves.bookyard.di

import android.content.Context
import com.github.fgoncalves.bookyard.BookYardApplication
import com.github.fgoncalves.bookyard.R
import com.github.fgoncalves.bookyard.data.di.ApiClientsModule
import com.github.fgoncalves.bookyard.di.qualifiers.ApplicationContext
import com.github.fgoncalves.bookyard.domain.di.UseCaseModule
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(includes = arrayOf(
    ApiClientsModule::class,
    ViewModelModule::class,
    UseCaseModule::class))
class ApplicationModule {
  @ApplicationContext
  fun providesApplicationContext(application: BookYardApplication): Context = application

  @Provides
  @Singleton
  fun providesGoogleSignInOptions(
      @ApplicationContext context: BookYardApplication): GoogleSignInOptions = GoogleSignInOptions.Builder(
      GoogleSignInOptions.DEFAULT_SIGN_IN)
      .requestIdToken(context.getString(R.string.default_web_client_id))
      .requestEmail()
      .build()
}
