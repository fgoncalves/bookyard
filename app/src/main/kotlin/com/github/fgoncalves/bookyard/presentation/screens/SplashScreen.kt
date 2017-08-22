package com.github.fgoncalves.bookyard.presentation.screens

import android.arch.lifecycle.LifecycleRegistry
import android.arch.lifecycle.LifecycleRegistryOwner
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.v4.view.ViewCompat
import android.support.v7.app.AlertDialog
import android.transition.TransitionInflater
import com.github.fgoncalves.bookyard.R
import com.github.fgoncalves.bookyard.databinding.SplashScreenBinding
import com.github.fgoncalves.bookyard.presentation.viewmodels.SplashScreenViewModel
import com.github.fgoncalves.pathmanager.ScreenNavigator
import com.google.android.gms.auth.api.Auth
import javax.inject.Inject


class SplashScreen : BaseScreen<SplashScreenBinding>(), LifecycleRegistryOwner {
  override val layout: Int = R.layout.splash_screen
  private val lifecycleRegistry = LifecycleRegistry(this)
  private var viewModel: SplashScreenViewModel? = null

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

    viewModel?.onSignInWithGoogle {
      val signInIntent = Auth.GoogleSignInApi.getSignInIntent(it)
      startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    viewModel?.onError {
      AlertDialog.Builder(context)
          .setTitle(getString(R.string.error_dialog_title))
          .setMessage(it)
          .setPositiveButton(R.string.ok) { _, _ -> }
          .show()
    }

    viewModel?.onTransitionToHomeScreen {
      // TODO: Needs to be single with animation
      // TODO: Put the if inside the navigator shit too
      val to = HomeScreen.newInstance()
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
        navigator.go(
            to = to,
            from = this,
            sharedElement = viewDataBinding.root.findViewById(R.id.transition_view),
            sharedElementTransactionName = ViewCompat.getTransitionName(viewDataBinding.root),
            enterSharedTransition = TransitionInflater.from(context)
                .inflateTransition(R.transition.splash_to_home_transition)
        )
        return@onTransitionToHomeScreen
      }
      navigator.single(to)
    }
  }

  override fun onActivityCreated(savedInstanceState: Bundle?) {
    super.onActivityCreated(savedInstanceState)
    viewModel?.onActivityCreated()
  }

  override fun getLifecycle(): LifecycleRegistry = lifecycleRegistry

  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    super.onActivityResult(requestCode, resultCode, data)
    val result = Auth.GoogleSignInApi.getSignInResultFromIntent(data)
    if (result.isSuccess) {
      val account = result.signInAccount
      viewModel?.onSignedIn(account)
      return
    }

    viewModel?.onSignedFailed()
  }
}
