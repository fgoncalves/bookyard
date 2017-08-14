package com.github.fgoncalves.bookyard.presentation.viewmodels

import android.arch.lifecycle.ViewModel
import android.util.Log
import android.view.View.OnClickListener
import javax.inject.Inject

abstract class SplashScreenViewModel : ViewModel() {
  abstract fun onScreenStart()

  abstract fun onScreenStop()

  abstract fun onActivityCreated()

  abstract fun onDestroy()

  abstract fun onSignedIn()

  abstract fun googleSignInClickListener(): OnClickListener
}

class SplashScreenViewModelImpl @Inject constructor() : SplashScreenViewModel() {
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

  override fun googleSignInClickListener(): OnClickListener = OnClickListener {
    Log.i("banana", "WORKING")
  }
}
