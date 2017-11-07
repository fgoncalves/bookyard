package com.github.fgoncalves.bookyard.di

import com.github.fgoncalves.bookyard.data.BooksService
import com.github.fgoncalves.bookyard.data.BooksServiceImpl
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

    @Binds
    @Singleton
    abstract fun providesBooksService(service: BooksServiceImpl): BooksService
}
