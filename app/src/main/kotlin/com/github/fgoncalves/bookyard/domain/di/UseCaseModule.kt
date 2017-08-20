package com.github.fgoncalves.bookyard.domain.di

import com.github.fgoncalves.bookyard.domain.usecases.GetBookUseCase
import com.github.fgoncalves.bookyard.domain.usecases.GetBookUseCaseImpl
import com.github.fgoncalves.bookyard.domain.usecases.GetOrCreateUserUseCaseImpl
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
abstract class UseCaseModule {
  @Binds
  @Singleton
  abstract fun providesGetBookUseCase(useCase: GetBookUseCaseImpl): GetBookUseCase

  @Binds
  @Singleton
  abstract fun providesGetOrCreateUserUseCase(
      useCase: GetOrCreateUserUseCaseImpl): GetOrCreateUserUseCaseImpl
}
