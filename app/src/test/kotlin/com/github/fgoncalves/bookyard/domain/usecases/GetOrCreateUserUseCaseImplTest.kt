package com.github.fgoncalves.bookyard.domain.usecases

import com.github.fgoncalves.bookyard.data.UserService
import com.github.fgoncalves.bookyard.data.models.User
import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import io.kotlintest.specs.StringSpec
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.observers.TestObserver
import io.reactivex.schedulers.Schedulers

class GetOrCreateUserUseCaseImplTest : StringSpec() {

  init {
    val user = User("foo@bar.com", "1.0.0", "uuid")
    val testObserver = TestObserver<User>()

    "Should create user when one doesn't exist" {
      val service = mock<UserService> {
        on { get("uuid") } doReturn listOf<Maybe<User>>(Maybe.empty(), Maybe.just(user))
        on { createOrUpdate(user) } doReturn Completable.complete()
      }
      val useCase = GetOrCreateUserUseCaseImpl(service)

      useCase.getOrCreateUser(user)
          .subscribeOn(Schedulers.trampoline())
          .observeOn(Schedulers.trampoline())
          .subscribe(testObserver)

      testObserver.assertValue(user)
      verify(service).createOrUpdate(user)
    }
  }
}
