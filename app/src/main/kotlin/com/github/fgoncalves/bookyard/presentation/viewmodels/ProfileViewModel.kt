package com.github.fgoncalves.bookyard.presentation.viewmodels

import android.net.Uri
import androidx.databinding.ObservableField
import androidx.lifecycle.Lifecycle.Event.ON_START
import androidx.lifecycle.Lifecycle.Event.ON_STOP
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import javax.inject.Inject

/**
 * View model for the profile in the drawer
 */
abstract class ProfileViewModel : ViewModel(), LifecycleObserver {
    abstract val name: ObservableField<String>

    abstract val emailField: ObservableField<String>

    abstract val profilePicture: ObservableField<Uri?>
}

// TODO: Put this behind a use case GetProfile
class ProfileViewModelImpl @Inject constructor(
        private val firebaseAuth: FirebaseAuth
) : ProfileViewModel() {
    override val name = ObservableField<String>("")
    override val emailField = ObservableField<String>("")
    override val profilePicture = ObservableField<Uri?>()

    private val firebaseAuthListener = FirebaseAuth.AuthStateListener {
        firebaseAuth.currentUser?.run {
            name.set(displayName)
            emailField.set(email)
            profilePicture.set(photoUrl)
        }
    }

    @OnLifecycleEvent(ON_START)
    fun onStart() = firebaseAuth.addAuthStateListener(firebaseAuthListener)

    @OnLifecycleEvent(ON_STOP)
    fun onStop() = firebaseAuth.removeAuthStateListener(firebaseAuthListener)
}
