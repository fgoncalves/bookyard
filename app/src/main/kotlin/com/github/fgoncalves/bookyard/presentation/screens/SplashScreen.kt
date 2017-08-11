package com.github.fgoncalves.bookyard.presentation.screens

import com.github.fgoncalves.bookyard.R
import com.github.fgoncalves.bookyard.databinding.SplashScreenBinding

class SplashScreen : BaseScreen<SplashScreenBinding>() {
  override val layout: Int = R.layout.splash_screen

  companion object {
    @JvmStatic
    fun newInstance(): SplashScreen = SplashScreen()
  }

  override fun applyBindings(binding: SplashScreenBinding) {

  }
}
