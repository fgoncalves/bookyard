package com.github.fgoncalves.bookyard.presentation.viewmodels

import android.arch.lifecycle.Lifecycle.Event.ON_DESTROY
import android.arch.lifecycle.Lifecycle.Event.ON_START
import android.arch.lifecycle.Lifecycle.Event.ON_STOP
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import android.arch.lifecycle.ViewModel
import android.util.Log
import android.view.View.OnClickListener
import com.github.fgoncalves.bookyard.MainActivity
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import javax.inject.Inject

abstract class SplashScreenViewModel : ViewModel(), LifecycleObserver {
  abstract fun onScreenStart()

  abstract fun onScreenStop()

  abstract fun onActivityCreated()

  abstract fun onDestroy()

  abstract fun onSignedIn()

  abstract fun googleSignInClickListener(): OnClickListener
}

class SplashScreenViewModelImpl @Inject constructor(
    val fragmentActivity: MainActivity,
    val googleOps: GoogleSignInOptions,
    val firebaseAuth: FirebaseAuth) : SplashScreenViewModel() {

  @OnLifecycleEvent(ON_START)
  override fun onScreenStart() {
    TODO(
        "Make this lifecycle")
  }

  @OnLifecycleEvent(ON_STOP)
  override fun onScreenStop() {
    TODO(
        "Make this lifecycle")
  }

  @OnLifecycleEvent(ON_DESTROY)
  override fun onDestroy() {
    TODO(
        "Make this lifecycle")
  }

  override fun onActivityCreated() {
    TODO(
        "This doesn't work with lifecycle yet")
  }

  override fun onSignedIn() {
    TODO(
        "Not implemented")
  }

  override fun googleSignInClickListener(): OnClickListener = OnClickListener {
    Log.i("banana", "WORKING")
  }
}
