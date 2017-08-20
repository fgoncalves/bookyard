package com.github.fgoncalves.bookyard.domain.usecases

import com.github.fgoncalves.bookyard.data.BooksService
import com.github.fgoncalves.bookyard.data.models.Book
import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import io.kotlintest.specs.StringSpec
import io.reactivex.Single
import io.reactivex.observers.TestObserver
import io.reactivex.schedulers.Schedulers

class GetBookUseCaseImplTest : StringSpec() {
  init {
    val service = mock<BooksService> {
      on { get("foo") } doReturn Single.just(Book("book", 0, emptyList()))
    }
    val testObserver = TestObserver<Book>()
    val useCase = GetBookUseCaseImpl(service)

    "Use case should get the given book" {
      useCase.get("foo")
          .subscribeOn(Schedulers.trampoline())
          .observeOn(Schedulers.trampoline())
          .subscribe(testObserver)

      testObserver.assertValue(Book("book", 0, emptyList()))
    }
  }
}
