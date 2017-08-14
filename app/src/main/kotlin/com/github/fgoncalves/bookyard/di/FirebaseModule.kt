package com.github.fgoncalves.bookyard.di

import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
object FirebaseModule {
  @Provides
  @Singleton
  @JvmStatic
  fun providesFireBaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()
}
