package com.github.fgoncalves.bookyard.presentation.screens

import com.github.fgoncalves.bookyard.R
import com.github.fgoncalves.bookyard.databinding.HomeBinding


class HomeScreen : BaseScreen<HomeBinding>() {
  override val layout = R.layout.home

  companion object {
    @JvmStatic
    fun newInstance(): HomeScreen = HomeScreen()
  }
}
