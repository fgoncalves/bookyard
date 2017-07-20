package com.github.fgoncalves.bookyard.data

import com.github.fgoncalves.bookyard.data.models.Book
import io.reactivex.Single
import javax.inject.Inject

interface BooksService {
  fun get(searchQuery: String): Single<Book>
}

class BooksServiceImpl @Inject constructor(val apiClient: BooksApiClient) : BooksService {
  override fun get(searchQuery: String): Single<Book> = apiClient.get(searchQuery)
}
