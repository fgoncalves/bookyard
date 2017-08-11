package com.github.fgoncalves.bookyard.presentation.viewmodels

import android.arch.lifecycle.ViewModel
import android.view.View.OnClickListener

abstract class SplashScreenViewModel : ViewModel() {
  abstract fun onScreenStart()

  abstract fun onScreenStop()

  abstract fun onActivityCreated()

  abstract fun onDestroy()

  abstract fun onSignedIn()

  abstract fun googleSignInClickListener(): OnClickListener
}

class SplashScreenViewModelImpl : SplashScreenViewModel() {
  override fun onScreenStart() {
    TODO(
        "not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun onScreenStop() {
    TODO(
        "not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun onActivityCreated() {
    TODO(
        "not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun onDestroy() {
    TODO(
        "not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun onSignedIn() {
    TODO(
        "not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun googleSignInClickListener(): OnClickListener {
    TODO(
        "not implemented") //To change body of created functions use File | Settings | File Templates.
  }
}
