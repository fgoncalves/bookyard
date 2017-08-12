package com.github.fgoncalves.bookyard.presentation.screens

import android.arch.lifecycle.ViewModelProviders
import com.github.fgoncalves.bookyard.R
import com.github.fgoncalves.bookyard.databinding.SplashScreenBinding
import com.github.fgoncalves.bookyard.presentation.viewmodels.SplashScreenViewModel

class SplashScreen : BaseScreen<SplashScreenBinding>() {
  override val layout: Int = R.layout.splash_screen

  companion object {
    @JvmStatic
    fun newInstance(): SplashScreen = SplashScreen()
  }

  override fun applyBindings(binding: SplashScreenBinding) {
    val viewModel = ViewModelProviders.of(this, viewModelFactory)[SplashScreenViewModel::class.java]
    binding.viewModel = viewModel
  }
}
