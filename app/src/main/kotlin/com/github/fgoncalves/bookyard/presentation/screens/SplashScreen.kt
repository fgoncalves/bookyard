package com.github.fgoncalves.bookyard.presentation.screens

import android.arch.lifecycle.LifecycleRegistry
import android.arch.lifecycle.LifecycleRegistryOwner
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import com.github.fgoncalves.bookyard.R
import com.github.fgoncalves.bookyard.databinding.SplashScreenBinding
import com.github.fgoncalves.bookyard.presentation.viewmodels.SplashScreenViewModel
import com.google.android.gms.auth.api.Auth




class SplashScreen : BaseScreen<SplashScreenBinding>(), LifecycleRegistryOwner {
  override val layout: Int = R.layout.splash_screen
  private val lifecycleRegistry = LifecycleRegistry(this)
  private var viewModel: SplashScreenViewModel? = null

  companion object {
    @JvmStatic
    fun newInstance(): SplashScreen = SplashScreen()

    @JvmField
    val RC_SIGN_IN = 0xbeef
  }

  override fun applyBindings(viewDataBinding: SplashScreenBinding) {
    viewModel = ViewModelProviders.of(this, viewModelFactory)[SplashScreenViewModel::class.java]
    viewDataBinding.viewModel = viewModel
    lifecycle.addObserver(viewModel)

    viewModel?.onSignInWithGoogle {
      val signInIntent = Auth.GoogleSignInApi.getSignInIntent(it)
      startActivityForResult(signInIntent, RC_SIGN_IN)
    }
  }

  override fun onActivityCreated(savedInstanceState: Bundle?) {
    super.onActivityCreated(savedInstanceState)
    viewModel?.onActivityCreated()
  }

  override fun getLifecycle(): LifecycleRegistry = lifecycleRegistry

  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    super.onActivityResult(requestCode, resultCode, data)
//    val result = Auth.GoogleSignInApi.getSignInResultFromIntent(data)
//    if (result.isSuccess) {
//      val account = result.signInAccount
//      loginToFireBaseWithGoogle(account)
//    } else {
//      if (onLoginErrorListener != null) onLoginErrorListener.onFailedToLoginToGoogle()
//    }
  }
}
