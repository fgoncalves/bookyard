package com.github.fgoncalves.bookyard.data

import com.github.fgoncalves.bookyard.data.models.Book
import com.github.fgoncalves.bookyard.data.models.FirebaseDatabaseException
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.doAnswer
import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import io.kotlintest.specs.StringSpec
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.observers.TestObserver
import io.reactivex.schedulers.Schedulers

class BooksServiceImplTest : StringSpec() {

  init {
    "service calls the api client" {
      val apiClient = mock<BooksApiClient> {
        on { get("isbn") } doReturn Single.just(Book("book", 0, emptyList()))
      }
      val service = BooksServiceImpl(apiClient, mock<DatabaseReference>())
      val testObserver = TestObserver<Book>()

      service.get("isbn")
          .subscribeOn(Schedulers.trampoline())
          .observeOn(Schedulers.trampoline())
          .subscribe(testObserver)

      testObserver.assertValue(Book("book", 0, emptyList()))
    }

    "get books database reference calls proper database reference" {
      val booksMock = mock<DatabaseReference> {
        on { child("books") } doReturn mock<DatabaseReference>()
      }
      val databaseReference = mock<DatabaseReference> {
        on { child("foo") } doReturn booksMock
      }
      val service = BooksServiceImpl(mock<BooksApiClient>(), databaseReference)

      service.getDatabaseReference("foo")

      verify(databaseReference).child("foo")
      verify(booksMock).child("books")
    }

    "delete should remove the given book from the list" {
      val toRemove = mock<DatabaseReference>()
      val query = mock<Query> {
        on { ref } doReturn toRemove
      }
      val books = mock<DatabaseReference> {
        on { equalTo("isbn") } doReturn query
      }
      val booksMock = mock<DatabaseReference> {
        on { child("books") } doReturn books
      }
      val databaseReference = mock<DatabaseReference> {
        on { child("foo") } doReturn booksMock
      }
      val service = BooksServiceImpl(mock<BooksApiClient>(), databaseReference)
      val testObserver = TestObserver<Any>()

      service.delete("foo", "isbn")
          .subscribeOn(Schedulers.trampoline())
          .observeOn(Schedulers.trampoline())
          .subscribe(testObserver)

      verify(toRemove).removeValue()
      testObserver.assertComplete()
      testObserver.assertNoErrors()
    }

    "Add should add book to the user's collection when successful" {
      val booksMock = mock<DatabaseReference> {
        on { addListenerForSingleValueEvent(any()) } doAnswer {
          (it.arguments[0] as ValueEventListener).onDataChange(mock<DataSnapshot>())
        }
      }
      val databaseReference = mock<DatabaseReference> {
        on { child("foo") } doReturn booksMock
      }
      val service = BooksServiceImpl(mock<BooksApiClient>(), databaseReference)
      val testObserver = TestObserver<Completable>()

      service.add("foo", "isbn")
          .subscribeOn(Schedulers.trampoline())
          .observeOn(Schedulers.trampoline())
          .subscribe(testObserver)

      testObserver.assertNoErrors()
      testObserver.assertComplete()
    }

    "Add should error with a FirebaseDatabaseException when book is not added to collection due to a database error" {
      val booksMock = mock<DatabaseReference> {
        on { addListenerForSingleValueEvent(any()) } doAnswer {
          (it.arguments[0] as ValueEventListener).onCancelled(mock<DatabaseError>())
        }
      }
      val databaseReference = mock<DatabaseReference> {
        on { child("foo") } doReturn booksMock
      }
      val service = BooksServiceImpl(mock<BooksApiClient>(), databaseReference)
      val testObserver = TestObserver<Completable>()

      service.add("foo", "isbn")
          .subscribeOn(Schedulers.trampoline())
          .observeOn(Schedulers.trampoline())
          .subscribe(testObserver)

      testObserver.assertError(FirebaseDatabaseException::class.java)
    }

    "Add should error with a RuntimeException when book is not added to collection due to an unknown error" {
      val booksMock = mock<DatabaseReference> {
        on { addListenerForSingleValueEvent(any()) } doAnswer {
          (it.arguments[0] as ValueEventListener).onCancelled(null)
        }
      }
      val databaseReference = mock<DatabaseReference> {
        on { child("foo") } doReturn booksMock
      }
      val service = BooksServiceImpl(mock<BooksApiClient>(), databaseReference)
      val testObserver = TestObserver<Completable>()

      service.add("foo", "isbn")
          .subscribeOn(Schedulers.trampoline())
          .observeOn(Schedulers.trampoline())
          .subscribe(testObserver)

      testObserver.assertError(RuntimeException::class.java)
    }
  }
}
