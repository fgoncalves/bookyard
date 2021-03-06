package com.github.fgoncalves.bookyard.presentation.viewmodels

import android.view.View.*
import androidx.annotation.StringRes
import androidx.databinding.ObservableInt
import androidx.lifecycle.Lifecycle.Event.*
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ViewModel
import com.github.fgoncalves.bookyard.MainActivity
import com.github.fgoncalves.bookyard.R
import com.github.fgoncalves.bookyard.config.SCHEMA_VERSION
import com.github.fgoncalves.bookyard.data.models.User
import com.github.fgoncalves.bookyard.domain.usecases.GetOrCreateUserUseCase
import com.github.fgoncalves.bookyard.presentation.utils.addTo
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.ConnectionResult.SUCCESS
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.GoogleApiClient.Builder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject
import kotlin.properties.Delegates

typealias OnSignInWithGoogle = (GoogleApiClient) -> Unit
typealias OnErrorCallback = (String) -> Unit

abstract class SplashScreenViewModel : ViewModel(), LifecycleObserver {
    abstract val signInButtonVisibility: ObservableInt
    abstract val progressBarVisibility: ObservableInt
    var signInWithGoogle: OnSignInWithGoogle? = null
    var onError: OnErrorCallback? = null
    var onTransitionToHomeScreenCallback: (() -> Unit)? = null
    var onGooglePlayServicesUnavailableCallback: ((error: Int) -> Unit)? = null

    abstract fun onActivityCreated()

    abstract fun googleSignInClickListener(): OnClickListener

    abstract fun onSignedIn(account: GoogleSignInAccount?)

    abstract fun onSignedFailed()

    fun onTransitionToHomeScreen(onTransitionToHomeScreenCallback: () -> Unit) = apply {
        this.onTransitionToHomeScreenCallback = onTransitionToHomeScreenCallback
    }

    fun onError(onError: OnErrorCallback?) = apply {
        this.onError = onError
    }

    fun onSignInWithGoogle(onSignInWithGoogle: OnSignInWithGoogle?) = apply {
        signInWithGoogle = onSignInWithGoogle
    }

    fun onGooglePlayServicesUnavailable(callback: ((error: Int) -> Unit)?) = apply {
        onGooglePlayServicesUnavailableCallback = callback
    }
}

class SplashScreenViewModelImpl @Inject constructor(
        // TODO: Make this a weak reference to avoid leaking the activity
        private val fragmentActivity: MainActivity,
        private val googleOps: GoogleSignInOptions,
        private val firebaseAuth: FirebaseAuth,
        private val getOrCreateUserUseCase: GetOrCreateUserUseCase
) : SplashScreenViewModel() {

    override val signInButtonVisibility = ObservableInt(GONE)
    override val progressBarVisibility = ObservableInt(VISIBLE)

    private var googleApiClient: GoogleApiClient by Delegates.notNull()
    private val authListener = FirebaseAuth.AuthStateListener {
        firebaseAuth.currentUser?.let {
            getOrCreateUserInFirebase()
        } ?: stopLoading()
    }
    private val disposables = CompositeDisposable()

    @OnLifecycleEvent(ON_START)
    fun onScreenStarted() {
        startLoading()

        val result = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(fragmentActivity)

        if (result != SUCCESS) {
            stopLoading()

            if (GoogleApiAvailability.getInstance().isUserResolvableError(result)) {
                onGooglePlayServicesUnavailableCallback?.invoke(result)
                return
            }
            onError?.invoke(fragmentActivity.getString(R.string.unrecoverable_error_from_google_play_services))
            return
        }

        firebaseAuth.addAuthStateListener(authListener)
    }

    @OnLifecycleEvent(ON_STOP)
    fun onScreenPaused() {
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
        startLoading()
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
        firebaseAuth.currentUser?.run {
            getOrCreateUserUseCase.getOrCreateUser(User(email ?: "", SCHEMA_VERSION, uid))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            { onTransitionToHomeScreenCallback?.invoke() },
                            {
                                Timber.e(it, "Failed to create user in firebase database")
                                errorMessage(R.string.failed_to_get_or_create_user)
                            })
                    .addTo(disposables)
        } ?: errorMessage(R.string.no_current_user)
    }

    private fun startLoading() {
        progressBarVisibility.set(VISIBLE)
        signInButtonVisibility.set(GONE)
    }

    private fun stopLoading() {
        progressBarVisibility.set(GONE)
        signInButtonVisibility.set(VISIBLE)
    }
}
