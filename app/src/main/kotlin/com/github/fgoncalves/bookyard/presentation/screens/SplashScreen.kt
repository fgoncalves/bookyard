package com.github.fgoncalves.bookyard.presentation.screens

import com.github.fgoncalves.bookyard.R
import com.github.fgoncalves.bookyard.databinding.SplashScreenBinding
import com.github.fgoncalves.bookyard.presentation.utils.LayoutResource

@LayoutResource(R.layout.splash_screen)
class SplashScreen : BaseScreen<SplashScreenBinding>() {
  companion object {
    @JvmStatic
    fun newInstance(): SplashScreen = SplashScreen()
  }

  override fun applyBindings(binding: SplashScreenBinding) {

  }
}
