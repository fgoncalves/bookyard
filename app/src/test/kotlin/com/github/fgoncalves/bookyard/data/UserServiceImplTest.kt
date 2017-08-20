package com.github.fgoncalves.bookyard.data

import com.github.fgoncalves.bookyard.data.models.FirebaseDatabaseException
import com.github.fgoncalves.bookyard.data.models.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.doAnswer
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

    "get should return empty if user doesn't exist" {
      val snapshot = mock<DataSnapshot> {
        on { getValue(User::class.java) } doReturn null as User?
      }
      val foobarDatabaseReference = mock<DatabaseReference> {
        on { addListenerForSingleValueEvent(any()) } doAnswer {
          invocationOnMock ->
          (invocationOnMock.arguments[0] as ValueEventListener)
              .onDataChange(snapshot)
        }
      }
      val usersDatabaseReference = mock<DatabaseReference> {
        on { child("uuid") } doReturn foobarDatabaseReference
      }

      val service = UserServiceImpl(usersDatabaseReference)
      val testObserver = TestObserver<Any>()

      service.get("uuid")
          .subscribeOn(Schedulers.trampoline())
          .observeOn(Schedulers.trampoline())
          .subscribe(testObserver)

      testObserver.assertNoValues()
      testObserver.assertComplete()
      testObserver.assertNoErrors()
    }

    "get should error if error callback is called" {
      val error = mock<DatabaseError> { }
      val foobarDatabaseReference = mock<DatabaseReference> {
        on { addListenerForSingleValueEvent(any()) } doAnswer {
          invocationOnMock ->
          (invocationOnMock.arguments[0] as ValueEventListener)
              .onCancelled(error)
        }
      }
      val usersDatabaseReference = mock<DatabaseReference> {
        on { child("uuid") } doReturn foobarDatabaseReference
      }

      val service = UserServiceImpl(usersDatabaseReference)
      val testObserver = TestObserver<Any>()

      service.get("uuid")
          .subscribeOn(Schedulers.trampoline())
          .observeOn(Schedulers.trampoline())
          .subscribe(testObserver)

      testObserver.assertError(FirebaseDatabaseException(error))
    }

    "get should error if error callback is called even if the error is null" {
      val foobarDatabaseReference = mock<DatabaseReference> {
        on { addListenerForSingleValueEvent(any()) } doAnswer {
          invocationOnMock ->
          (invocationOnMock.arguments[0] as ValueEventListener)
              .onCancelled(null)
        }
      }
      val usersDatabaseReference = mock<DatabaseReference> {
        on { child("uuid") } doReturn foobarDatabaseReference
      }

      val service = UserServiceImpl(usersDatabaseReference)
      val testObserver = TestObserver<Any>()

      service.get("uuid")
          .subscribeOn(Schedulers.trampoline())
          .observeOn(Schedulers.trampoline())
          .subscribe(testObserver)

      testObserver.assertError(RuntimeException::class.java)
    }

    "get should error if success callback is called but snapshot is null" {
      val foobarDatabaseReference = mock<DatabaseReference> {
        on { addListenerForSingleValueEvent(any()) } doAnswer {
          invocationOnMock ->
          (invocationOnMock.arguments[0] as ValueEventListener)
              .onDataChange(null)
        }
      }
      val usersDatabaseReference = mock<DatabaseReference> {
        on { child("uuid") } doReturn foobarDatabaseReference
      }

      val service = UserServiceImpl(usersDatabaseReference)
      val testObserver = TestObserver<Any>()

      service.get("uuid")
          .subscribeOn(Schedulers.trampoline())
          .observeOn(Schedulers.trampoline())
          .subscribe(testObserver)

      testObserver.assertError(RuntimeException::class.java)
    }

    "get should return user when there's one in firebase" {
      val user = User("foo@bar.com", "1.0.0", "uuid")
      val snapshot = mock<DataSnapshot> {
        on { getValue(User::class.java) } doReturn user
      }
      val foobarDatabaseReference = mock<DatabaseReference> {
        on { addListenerForSingleValueEvent(any()) } doAnswer {
          invocationOnMock ->
          (invocationOnMock.arguments[0] as ValueEventListener)
              .onDataChange(snapshot)
        }
      }
      val usersDatabaseReference = mock<DatabaseReference> {
        on { child("uuid") } doReturn foobarDatabaseReference
      }

      val service = UserServiceImpl(usersDatabaseReference)
      val testObserver = TestObserver<Any>()

      service.get("uuid")
          .subscribeOn(Schedulers.trampoline())
          .observeOn(Schedulers.trampoline())
          .subscribe(testObserver)

      testObserver.assertValue(user)
      testObserver.assertComplete()
      testObserver.assertNoErrors()
    }
  }
}
