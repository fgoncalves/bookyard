package com.github.fgoncalves.bookyard.domain.di

import com.github.fgoncalves.bookyard.domain.usecases.*
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

    @Binds
    @Singleton
    abstract fun providesAddBookUseCase(
            useCase: AddBookUseCaseImpl): AddBookUseCase

    @Binds
    @Singleton
    abstract fun providesGetCurrentUserUseCase(
            useCase: GetCurrentUserUseCaseImpl): GetCurrentUserUseCase
}
