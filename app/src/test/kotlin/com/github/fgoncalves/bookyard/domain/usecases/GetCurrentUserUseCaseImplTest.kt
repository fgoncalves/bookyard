package com.github.fgoncalves.bookyard.domain.usecases

import com.github.fgoncalves.bookyard.data.UserService
import com.github.fgoncalves.bookyard.data.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import io.kotlintest.specs.StringSpec
import io.reactivex.Maybe
import io.reactivex.observers.TestObserver
import io.reactivex.schedulers.Schedulers

class GetCurrentUserUseCaseImplTest : StringSpec() {

  init {

    "Should return empty if there's no user logged in" {
      val testObserver = TestObserver<User>()
      val service = mock<UserService> {
        on { get("uuid") } doReturn Maybe.empty()
      }
      val firebaseAuth = mock<FirebaseAuth> {
        on { currentUser } doReturn null as FirebaseUser?
      }
      val useCase = GetCurrentUserUseCaseImpl(firebaseAuth, service)

      useCase.get()
          .subscribeOn(Schedulers.trampoline())
          .observeOn(Schedulers.trampoline())
          .subscribe(testObserver)

      testObserver.assertComplete()
      testObserver.assertNoValues()
    }

    "Should return current user" {
      val testObserver = TestObserver<User>()
      val user = User("foo")
      val service = mock<UserService> {
        on { get("uuid") } doReturn Maybe.just(user)
      }
      val firebaseUser = mock<FirebaseUser> {
        on { uid } doReturn "uuid"
      }
      val firebaseAuth = mock<FirebaseAuth> {
        on { currentUser } doReturn firebaseUser
      }
      val useCase = GetCurrentUserUseCaseImpl(firebaseAuth, service)

      useCase.get()
          .subscribeOn(Schedulers.trampoline())
          .observeOn(Schedulers.trampoline())
          .subscribe(testObserver)

      testObserver.assertComplete()
      testObserver.assertValue(user)
    }
  }
}
