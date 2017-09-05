package com.github.fgoncalves.bookyard.domain.usecases

import com.github.fgoncalves.bookyard.data.UserService
import com.github.fgoncalves.bookyard.data.models.User
import com.google.firebase.auth.FirebaseAuth
import io.reactivex.Maybe
import javax.inject.Inject

/**
 * Get the current logged in user if there's any
 */
interface GetCurrentUserUseCase {
  /**
   * Get the current logged in user
   */
  fun get(): Maybe<User>
}

class GetCurrentUserUseCaseImpl @Inject constructor(
    val firebaseAuth: FirebaseAuth,
    val service: UserService
) : GetCurrentUserUseCase {
  override fun get(): Maybe<User> {
    val currentUser = firebaseAuth.currentUser
    return if (currentUser == null)
      Maybe.empty()
    else
      service.get(currentUser.uid)
  }
}
