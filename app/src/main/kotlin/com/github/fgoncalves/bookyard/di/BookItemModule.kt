package com.github.fgoncalves.bookyard.di

import com.github.fgoncalves.bookyard.presentation.viewmodels.BookItemViewModel
import com.github.fgoncalves.bookyard.presentation.viewmodels.BookItemViewModelImpl
import dagger.Module
import dagger.Provides

@Module
object BookItemModule {
  @Provides
  @JvmStatic
  fun providesBookItemViewModel(viewModel: BookItemViewModelImpl): BookItemViewModel = viewModel
}
