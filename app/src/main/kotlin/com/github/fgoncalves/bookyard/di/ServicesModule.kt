package com.github.fgoncalves.bookyard.di

import com.github.fgoncalves.bookyard.data.UserService
import com.github.fgoncalves.bookyard.data.UserServiceImpl
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
abstract class ServicesModule {
  @Binds
  @Singleton
  abstract fun providesUserService(service: UserServiceImpl): UserService
}
