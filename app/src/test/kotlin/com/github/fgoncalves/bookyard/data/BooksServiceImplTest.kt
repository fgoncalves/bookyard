package com.github.fgoncalves.bookyard.data

import com.github.fgoncalves.bookyard.data.models.Book
import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
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
      val service = BooksServiceImpl(apiClient)
      val testObserver = TestObserver<Book>()

      service.get("isbn")
          .subscribeOn(Schedulers.trampoline())
          .observeOn(Schedulers.trampoline())
          .subscribe(testObserver)

      testObserver.assertValue(Book("book", 0, emptyList()))
    }
  }
}
