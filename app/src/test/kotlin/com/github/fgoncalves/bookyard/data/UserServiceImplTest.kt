package com.github.fgoncalves.bookyard.data

import com.github.fgoncalves.bookyard.data.models.User
import com.google.firebase.database.DatabaseReference
import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import io.kotlintest.specs.StringSpec
import io.reactivex.observers.TestObserver
import io.reactivex.schedulers.Schedulers

class UserServiceImplTest : StringSpec() {

  init {
    "create or update updates firebase with the correct user info" {
      val user = User("foo@bar.com", "1.0.0", "uuid")
      val foobarDatabaseReference = mock<DatabaseReference> {}
      val usersDatabaseReference = mock<DatabaseReference> {
        on { child(user.uuid) } doReturn foobarDatabaseReference
      }
      val service = UserServiceImpl(usersDatabaseReference)
      val testObserver = TestObserver<Any>()

      service.createOrUpdate(user)
          .subscribeOn(Schedulers.trampoline())
          .observeOn(Schedulers.trampoline())
          .subscribe(testObserver)

      verify(foobarDatabaseReference).setValue(user)
      testObserver.assertComplete()
      testObserver.assertNoErrors()
    }
  }
}
