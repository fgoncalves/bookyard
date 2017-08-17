package com.github.fgoncalves.bookyard.presentation.viewmodels

import android.arch.lifecycle.Lifecycle.Event.ON_DESTROY
import android.arch.lifecycle.Lifecycle.Event.ON_START
import android.arch.lifecycle.Lifecycle.Event.ON_STOP
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import android.arch.lifecycle.ViewModel
import android.databinding.ObservableInt
import android.view.View.GONE
import android.view.View.OnClickListener
import android.view.View.VISIBLE
import com.github.fgoncalves.bookyard.MainActivity
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.GoogleApiClient.Builder
import com.google.firebase.auth.FirebaseAuth
import timber.log.Timber
import javax.inject.Inject
import kotlin.properties.Delegates

typealias onSignInWithGoogle = (GoogleApiClient) -> Any

abstract class SplashScreenViewModel : ViewModel(), LifecycleObserver {
  val signInButtonVisibility = ObservableInt(GONE)
  val progressBarVisibility = ObservableInt(VISIBLE)
  var signInWithGoogle: onSignInWithGoogle? = null

  abstract fun onScreenStart()

  abstract fun onScreenStop()

  abstract fun onActivityCreated()

  abstract fun onDestroy()

  abstract fun googleSignInClickListener(): OnClickListener

  abstract fun onSignedIn()

  fun onSignInWithGoogle(onSignInWithGoogle: onSignInWithGoogle?) = apply {
    signInWithGoogle = onSignInWithGoogle
  }
}

class SplashScreenViewModelImpl @Inject constructor(
    val fragmentActivity: MainActivity,
    val googleOps: GoogleSignInOptions,
    val firebaseAuth: FirebaseAuth) : SplashScreenViewModel() {

  private var googleApiClient: GoogleApiClient by Delegates.notNull()
  private val authListener = FirebaseAuth.AuthStateListener {
    val user = firebaseAuth.currentUser
    if (user == null) {
      progressBarVisibility.set(GONE)
      signInButtonVisibility.set(VISIBLE)
      return@AuthStateListener
    }

    // TODO: create or get user
    Timber.d("Successful logged in")
  }

  @OnLifecycleEvent(ON_START)
  override fun onScreenStart() {
    progressBarVisibility.set(VISIBLE)
    signInButtonVisibility.set(GONE)
    firebaseAuth.addAuthStateListener(authListener)
  }

  @OnLifecycleEvent(ON_STOP)
  override fun onScreenStop() {
    firebaseAuth.removeAuthStateListener(authListener)
  }

  @OnLifecycleEvent(ON_DESTROY)
  override fun onDestroy() {
    googleApiClient.stopAutoManage(fragmentActivity)
    googleApiClient.disconnect()
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

  override fun googleSignInClickListener(): OnClickListener = OnClickListener {
    progressBarVisibility.set(VISIBLE)
    signInButtonVisibility.set(GONE)
    signInWithGoogle?.invoke(googleApiClient)
  }

  override fun onSignedIn() {
    Timber.d("Everything's ok")
  }
}
