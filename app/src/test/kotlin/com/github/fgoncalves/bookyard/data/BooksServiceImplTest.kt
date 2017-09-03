package com.github.fgoncalves.bookyard.data

import com.github.fgoncalves.bookyard.data.models.Book
import com.google.firebase.database.DatabaseReference
import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import io.kotlintest.specs.StringSpec
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
      val books = mock<DatabaseReference> {
        on { child("isbn") } doReturn toRemove
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
  }
}
