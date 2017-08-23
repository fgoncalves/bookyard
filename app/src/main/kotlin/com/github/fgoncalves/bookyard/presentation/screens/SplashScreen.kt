package com.github.fgoncalves.bookyard.presentation.screens

import android.arch.lifecycle.LifecycleRegistry
import android.arch.lifecycle.LifecycleRegistryOwner
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import com.github.fgoncalves.bookyard.R
import com.github.fgoncalves.bookyard.databinding.SplashScreenBinding
import com.github.fgoncalves.bookyard.presentation.viewmodels.SplashScreenViewModel
import com.github.fgoncalves.pathmanager.ScreenNavigator
import com.google.android.gms.auth.api.Auth
import javax.inject.Inject
import kotlin.properties.Delegates


class SplashScreen : BaseScreen<SplashScreenBinding>(), LifecycleRegistryOwner {
  override val layout: Int = R.layout.splash_screen
  private val lifecycleRegistry = LifecycleRegistry(this)
  private var viewModel: SplashScreenViewModel by Delegates.notNull()

  @Inject lateinit var navigator: ScreenNavigator

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

    viewModel.onSignInWithGoogle {
      val signInIntent = Auth.GoogleSignInApi.getSignInIntent(it)
      startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    viewModel.onError {
      AlertDialog.Builder(context)
          .setTitle(getString(R.string.error_dialog_title))
          .setMessage(it)
          .setPositiveButton(R.string.ok) { _, _ -> }
          .show()
    }

    viewModel.onTransitionToHomeScreen {
      navigator.single(HomeScreen.newInstance())
    }
  }

  override fun onActivityCreated(savedInstanceState: Bundle?) {
    super.onActivityCreated(savedInstanceState)
    viewModel.onActivityCreated()
  }

  override fun getLifecycle(): LifecycleRegistry = lifecycleRegistry

  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    super.onActivityResult(requestCode, resultCode, data)
    val result = Auth.GoogleSignInApi.getSignInResultFromIntent(data)
    if (result.isSuccess) {
      val account = result.signInAccount
      viewModel.onSignedIn(account)
      return
    }

    viewModel.onSignedFailed()
  }
}
