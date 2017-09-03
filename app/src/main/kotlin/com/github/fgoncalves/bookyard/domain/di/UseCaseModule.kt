package com.github.fgoncalves.bookyard.domain.di

import com.github.fgoncalves.bookyard.domain.usecases.DeleteBookUseCase
import com.github.fgoncalves.bookyard.domain.usecases.DeleteBookUseCaseImpl
import com.github.fgoncalves.bookyard.domain.usecases.GetBookUseCase
import com.github.fgoncalves.bookyard.domain.usecases.GetBookUseCaseImpl
import com.github.fgoncalves.bookyard.domain.usecases.GetBooksDatabaseReferenceUseCase
import com.github.fgoncalves.bookyard.domain.usecases.GetBooksDatabaseReferenceUseCaseImpl
import com.github.fgoncalves.bookyard.domain.usecases.GetOrCreateUserUseCase
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
      useCase: GetOrCreateUserUseCaseImpl): GetOrCreateUserUseCase

  @Binds
  @Singleton
  abstract fun providesGetBooksDatabaseReference(
      useCase: GetBooksDatabaseReferenceUseCaseImpl): GetBooksDatabaseReferenceUseCase

  @Binds
  @Singleton
  abstract fun providesDeleteBookUseCase(
      useCase: DeleteBookUseCaseImpl): DeleteBookUseCase
}
