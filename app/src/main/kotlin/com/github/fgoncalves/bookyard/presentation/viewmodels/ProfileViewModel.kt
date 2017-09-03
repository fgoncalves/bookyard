package com.github.fgoncalves.bookyard.presentation.viewmodels

import android.arch.lifecycle.Lifecycle.Event.ON_START
import android.arch.lifecycle.Lifecycle.Event.ON_STOP
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import android.arch.lifecycle.ViewModel
import android.databinding.ObservableField
import android.net.Uri
import com.google.firebase.auth.FirebaseAuth
import javax.inject.Inject

/**
 * View model for the profile in the drawer
 */
abstract class ProfileViewModel : ViewModel(), LifecycleObserver {
  abstract val name: ObservableField<String>

  abstract val email: ObservableField<String>

  abstract val profilePicture: ObservableField<Uri?>
}

class ProfileViewModelImpl @Inject constructor(
    val firebaseAuth: FirebaseAuth
) : ProfileViewModel() {
  override val name = ObservableField<String>("")
  override val email = ObservableField<String>("")
  override val profilePicture = ObservableField<Uri?>()

  private val firebaseAuthListener = FirebaseAuth.AuthStateListener {
    val user = firebaseAuth.currentUser
    user?.let {
      name.set(user.displayName)
      email.set(user.email)
      profilePicture.set(user.photoUrl)
    }
  }

  @OnLifecycleEvent(ON_START)
  fun onStart() {
    firebaseAuth.addAuthStateListener(firebaseAuthListener)
  }

  @OnLifecycleEvent(ON_STOP)
  fun onStop() {
    firebaseAuth.removeAuthStateListener(firebaseAuthListener)
  }
}
