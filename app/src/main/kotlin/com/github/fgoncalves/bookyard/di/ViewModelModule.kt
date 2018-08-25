package com.github.fgoncalves.bookyard.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.github.fgoncalves.bookyard.di.keys.ViewModelKey
import com.github.fgoncalves.bookyard.di.scopes.ActivityScope
import com.github.fgoncalves.bookyard.presentation.BooksRecyclerViewAdapter
import com.github.fgoncalves.bookyard.presentation.BooksRecyclerViewAdapterImpl
import com.github.fgoncalves.bookyard.presentation.base.ViewModelFactory
import com.github.fgoncalves.bookyard.presentation.viewmodels.*
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {
    @Binds
    @ActivityScope
    abstract fun providesBooksRecyclerViewAdapter(
            adapter: BooksRecyclerViewAdapterImpl): BooksRecyclerViewAdapter

    @Binds
    @ActivityScope
    abstract fun providesViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @ActivityScope
    @IntoMap
    @ViewModelKey(SplashScreenViewModel::class)
    abstract fun providesSplashScreenViewModel(viewModel: SplashScreenViewModelImpl): ViewModel

    @Binds
    @ActivityScope
    @IntoMap
    @ViewModelKey(HomeViewModel::class)
    abstract fun providesHomeViewModel(viewModel: HomeViewModelImpl): ViewModel

    @Binds
    @ActivityScope
    @IntoMap
    @ViewModelKey(ProfileViewModel::class)
    abstract fun providesProfileViewModel(viewModel: ProfileViewModelImpl): ViewModel

    @Binds
    @ActivityScope
    @IntoMap
    @ViewModelKey(BookDetailsViewModel::class)
    abstract fun providesBookDetailsViewModel(viewModel: BookDetailsViewModelImpl): ViewModel
}
