package com.github.fgoncalves.bookyard.domain.usecases

import com.github.fgoncalves.bookyard.data.BooksService
import com.github.fgoncalves.bookyard.data.models.Book
import io.reactivex.Single
import javax.inject.Inject

interface GetBookUseCase {
  fun get(isbn: String): Single<Book>
}

class GetBookUseCaseImpl @Inject constructor(val service: BooksService) : GetBookUseCase {
  override fun get(isbn: String): Single<Book> {
    return service.get(isbn)
  }
}
