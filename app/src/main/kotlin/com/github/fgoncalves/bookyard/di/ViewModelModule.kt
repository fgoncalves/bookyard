package com.github.fgoncalves.bookyard.di

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.github.fgoncalves.bookyard.di.keys.ViewModelKey
import com.github.fgoncalves.bookyard.presentation.base.ViewModelFactory
import com.github.fgoncalves.bookyard.presentation.viewmodels.SplashScreenViewModel
import com.github.fgoncalves.bookyard.presentation.viewmodels.SplashScreenViewModelImpl
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {
  @Binds
  abstract fun providesViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

  @Binds
  @IntoMap
  @ViewModelKey(SplashScreenViewModel::class)
  abstract fun providesSplashScreenViewModel(viewModel: SplashScreenViewModelImpl): ViewModel
}
