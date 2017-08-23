package com.github.fgoncalves.bookyard.di

import com.github.fgoncalves.bookyard.presentation.viewmodels.BookItemViewModel
import com.github.fgoncalves.bookyard.presentation.viewmodels.BookItemViewModelImpl
import dagger.Binds
import dagger.Module

@Module
abstract class BookItemModule {
  @Binds
  abstract fun providesBookItemViewModel(viewModel: BookItemViewModelImpl): BookItemViewModel
}
