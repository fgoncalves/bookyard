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
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.GoogleApiClient.Builder
import com.google.firebase.auth.FirebaseAuth
import javax.inject.Inject
import kotlin.properties.Delegates

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

  private var googleApiClient: GoogleApiClient by Delegates.notNull()

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
    googleApiClient = Builder(fragmentActivity)
        .enableAutoManage(fragmentActivity) {
          TODO(
              "not implemented") //To change body of created functions use File | Settings | File Templates.
        }
        .addApi(Auth.GOOGLE_SIGN_IN_API, googleOps)
        .build()

  }

  override fun onSignedIn() {
    TODO(
        "Not implemented")
  }

  override fun googleSignInClickListener(): OnClickListener = OnClickListener {
    Log.i("banana", "WORKING")
  }
}
