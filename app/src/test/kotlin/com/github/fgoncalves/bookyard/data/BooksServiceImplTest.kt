package com.github.fgoncalves.bookyard.data

import com.github.fgoncalves.bookyard.data.models.Book
import com.google.firebase.database.DatabaseReference
import com.nhaarman.mockito_kotlin.any
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
      val service = BooksServiceImpl(apiClient, mock<DatabaseReference> {})
      val testObserver = TestObserver<Book>()

      service.get("isbn")
          .subscribeOn(Schedulers.trampoline())
          .observeOn(Schedulers.trampoline())
          .subscribe(testObserver)

      testObserver.assertValue(Book("book", 0, emptyList()))
    }

    "get books database reference calls proper database reference" {
      val booksMock = mock<DatabaseReference>()
      val databaseReference = mock<DatabaseReference> {
        on { child(any()) } doReturn booksMock
      }
      val service = BooksServiceImpl(mock<BooksApiClient> {}, databaseReference)

      service.getDatabaseReference("foo")

      verify(databaseReference).child("foo")
      verify(booksMock).child("books")
    }
  }
}
