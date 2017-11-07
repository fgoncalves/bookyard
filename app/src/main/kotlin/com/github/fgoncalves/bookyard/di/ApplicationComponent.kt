package com.github.fgoncalves.bookyard.di

import com.github.fgoncalves.bookyard.BookYardApplication
import com.github.fgoncalves.bookyard.di.qualifiers.ScreenContainerId
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(
        AndroidSupportInjectionModule::class,
        AndroidInjectionModule::class,
        ApplicationModule::class,
        BuildersModule::class))
interface ApplicationComponent : AndroidInjector<BookYardApplication> {
    @dagger.Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: BookYardApplication): Builder

        @BindsInstance
        fun screenContainerId(@ScreenContainerId screenContainerId: Int): Builder

        fun build(): ApplicationComponent
    }

    fun plus(module: BookItemModule): BooksItemComponent
}
