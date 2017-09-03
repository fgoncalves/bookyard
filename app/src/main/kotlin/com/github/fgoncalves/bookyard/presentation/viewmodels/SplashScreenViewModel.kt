package com.github.fgoncalves.bookyard.presentation.viewmodels

import android.arch.lifecycle.Lifecycle.Event.ON_DESTROY
import android.arch.lifecycle.Lifecycle.Event.ON_PAUSE
import android.arch.lifecycle.Lifecycle.Event.ON_RESUME
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import android.arch.lifecycle.ViewModel
import android.databinding.ObservableInt
import android.support.annotation.StringRes
import android.view.View.GONE
import android.view.View.OnClickListener
import android.view.View.VISIBLE
import com.github.fgoncalves.bookyard.MainActivity
import com.github.fgoncalves.bookyard.R
import com.github.fgoncalves.bookyard.config.SCHEMA_VERSION
import com.github.fgoncalves.bookyard.data.models.User
import com.github.fgoncalves.bookyard.di.qualifiers.NetworkSchedulerTransformer
import com.github.fgoncalves.bookyard.domain.usecases.GetOrCreateUserUseCase
import com.github.fgoncalves.rx_schedulers.SchedulerTransformer
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.GoogleApiClient.Builder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import io.reactivex.disposables.CompositeDisposable
import timber.log.Timber
import javax.inject.Inject
import kotlin.properties.Delegates

typealias OnSignInWithGoogle = (GoogleApiClient) -> Unit
typealias OnErrorCallback = (String) -> Unit

abstract class SplashScreenViewModel : ViewModel(), LifecycleObserver {
  val signInButtonVisibility = ObservableInt(GONE)
  val progressBarVisibility = ObservableInt(VISIBLE)
  var signInWithGoogle: OnSignInWithGoogle? = null
  var onError: OnErrorCallback? = null
  var onTransitionToHomeScreenCallback: (() -> Unit)? = null

  abstract fun onActivityCreated()

  abstract fun googleSignInClickListener(): OnClickListener

  abstract fun onSignedIn(account: GoogleSignInAccount?)

  abstract fun onSignedFailed()

  fun onTransitionToHomeScreen(onTransitionToHomeScreenCallback: () -> Unit) {
    this.onTransitionToHomeScreenCallback = onTransitionToHomeScreenCallback
  }

  fun onError(onError: OnErrorCallback?) {
    this.onError = onError
  }

  fun onSignInWithGoogle(onSignInWithGoogle: OnSignInWithGoogle?) = apply {
    signInWithGoogle = onSignInWithGoogle
  }
}

class SplashScreenViewModelImpl @Inject constructor(
    val fragmentActivity: MainActivity,
    val googleOps: GoogleSignInOptions,
    val firebaseAuth: FirebaseAuth,
    val getOrCreateUserUseCase: GetOrCreateUserUseCase,
    @NetworkSchedulerTransformer
    val schedulerTransformer: SchedulerTransformer) : SplashScreenViewModel() {

  private var googleApiClient: GoogleApiClient by Delegates.notNull()
  private val authListener = FirebaseAuth.AuthStateListener {
    val user = firebaseAuth.currentUser
    if (user == null) {
      progressBarVisibility.set(GONE)
      signInButtonVisibility.set(VISIBLE)
      return@AuthStateListener
    }

    getOrCreateUserInFirebase()
  }
  private val disposables = CompositeDisposable()

  @OnLifecycleEvent(ON_RESUME)
  fun onScreenStart() {
    progressBarVisibility.set(VISIBLE)
    signInButtonVisibility.set(GONE)
    firebaseAuth.addAuthStateListener(authListener)
  }

  @OnLifecycleEvent(ON_PAUSE)
  fun onScreenStop() {
    disposables.clear()
    firebaseAuth.removeAuthStateListener(authListener)
  }

  @OnLifecycleEvent(ON_DESTROY)
  fun onDestroy() {
    googleApiClient.stopAutoManage(fragmentActivity)
    googleApiClient.disconnect()
  }

  override fun onActivityCreated() {
    googleApiClient = Builder(fragmentActivity)
        .enableAutoManage(fragmentActivity) {
          if (!it.isSuccess) {
            signInButtonVisibility.set(VISIBLE)
            progressBarVisibility.set(GONE)
            errorMessage(R.string.failed_to_create_google_api_client)
          }
        }
        .addApi(Auth.GOOGLE_SIGN_IN_API, googleOps)
        .build()
  }

  override fun googleSignInClickListener(): OnClickListener = OnClickListener {
    progressBarVisibility.set(VISIBLE)
    signInButtonVisibility.set(GONE)
    signInWithGoogle?.invoke(googleApiClient)
  }

  override fun onSignedIn(account: GoogleSignInAccount?) {
    if (account == null) {
      errorMessage(R.string.failed_to_get_account_from_google)
      return
    }

    val credentials = GoogleAuthProvider.getCredential(account.idToken, null)
    firebaseAuth.signInWithCredential(credentials).addOnCompleteListener(fragmentActivity) {
      if (it.isSuccessful) {
        getOrCreateUserInFirebase()
        return@addOnCompleteListener
      }

      errorMessage(R.string.failed_to_signIn_to_firebase)
    }
  }

  override fun onSignedFailed() {
    errorMessage(R.string.failed_to_signIn_to_google)
  }

  private fun errorMessage(@StringRes stringResource: Int) {
    val errorMessage = fragmentActivity.getString(stringResource)
    onError?.invoke(errorMessage)
  }

  private fun getOrCreateUserInFirebase() {
    val currentUser = firebaseAuth.currentUser
    if (currentUser == null) {
      errorMessage(R.string.no_current_user)
      return
    }

    val user = User(currentUser.email ?: "", SCHEMA_VERSION, currentUser.uid)
    val disposable = getOrCreateUserUseCase.getOrCreateUser(user)
        .compose(schedulerTransformer.applySingleSchedulers())
        .subscribe(
            { onTransitionToHomeScreenCallback?.invoke() },
            {
              Timber.e(it, "Failed to create user in firebase database")
              errorMessage(R.string.failed_to_get_or_create_user)
            })
    disposables.add(disposable)
  }
}
