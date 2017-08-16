package com.github.fgoncalves.bookyard.presentation.screens

import android.arch.lifecycle.LifecycleRegistry
import android.arch.lifecycle.LifecycleRegistryOwner
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import com.github.fgoncalves.bookyard.R
import com.github.fgoncalves.bookyard.databinding.SplashScreenBinding
import com.github.fgoncalves.bookyard.presentation.viewmodels.SplashScreenViewModel

class SplashScreen : BaseScreen<SplashScreenBinding>(), LifecycleRegistryOwner {
  override val layout: Int = R.layout.splash_screen
  private val lifecycleRegistry = LifecycleRegistry(this)
  private var viewModel: SplashScreenViewModel? = null

  companion object {
    @JvmStatic
    fun newInstance(): SplashScreen = SplashScreen()
  }

  override fun applyBindings(viewDataBinding: SplashScreenBinding) {
    viewModel = ViewModelProviders.of(this, viewModelFactory)[SplashScreenViewModel::class.java]
    viewDataBinding.viewModel = viewModel
    lifecycle.addObserver(viewModel)
  }

  override fun onActivityCreated(savedInstanceState: Bundle?) {
    super.onActivityCreated(savedInstanceState)
    viewModel?.onActivityCreated()
  }

  override fun getLifecycle(): LifecycleRegistry = lifecycleRegistry
}
