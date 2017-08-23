package com.github.fgoncalves.bookyard.presentation.screens

import android.arch.lifecycle.LifecycleRegistry
import android.arch.lifecycle.LifecycleRegistryOwner
import android.arch.lifecycle.ViewModelProviders
import com.github.fgoncalves.bookyard.R
import com.github.fgoncalves.bookyard.databinding.HomeBinding
import com.github.fgoncalves.bookyard.presentation.viewmodels.HomeViewModel
import kotlin.properties.Delegates


class HomeScreen : BaseScreen<HomeBinding>(), LifecycleRegistryOwner {
  override val layout = R.layout.home

  private val lifecycleRegistry = LifecycleRegistry(this)
  private var viewModel: HomeViewModel by Delegates.notNull()

  companion object {
    @JvmStatic
    fun newInstance(): HomeScreen = HomeScreen()
  }

  override fun getLifecycle(): LifecycleRegistry = lifecycleRegistry

  override fun applyBindings(viewDataBinding: HomeBinding) {
    viewModel = ViewModelProviders.of(this, viewModelFactory)[HomeViewModel::class.java]
    viewDataBinding.viewModel = viewModel

    lifecycle.addObserver(viewModel)
  }
}
