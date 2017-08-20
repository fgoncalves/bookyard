package com.github.fgoncalves.bookyard.di

import com.github.fgoncalves.bookyard.di.qualifiers.UsersDatabase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
object FirebaseModule {
  @Provides
  @Singleton
  @JvmStatic
  fun providesFireBaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

  @Provides
  @Singleton
  @JvmStatic
  fun providesRealTimeDatabase(): FirebaseDatabase = FirebaseDatabase.getInstance()

  @Provides
  @Singleton
  @JvmStatic
  @UsersDatabase
  fun providesUserDatabaseReference(firebaseDatabase: FirebaseDatabase): DatabaseReference
      = firebaseDatabase.getReference("users")
}
