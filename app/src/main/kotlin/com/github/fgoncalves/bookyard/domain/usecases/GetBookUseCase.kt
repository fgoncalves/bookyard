package com.github.fgoncalves.bookyard.domain.usecases

import com.github.fgoncalves.bookyard.data.BooksService
import com.github.fgoncalves.bookyard.data.models.Book
import io.reactivex.Maybe
import javax.inject.Inject

interface GetBookUseCase {
    fun get(isbn: String): Maybe<Book>
}

class GetBookUseCaseImpl @Inject constructor(
        private val service: BooksService
) : GetBookUseCase {
    override fun get(isbn: String): Maybe<Book> = service.get("isbn:$isbn")
}
