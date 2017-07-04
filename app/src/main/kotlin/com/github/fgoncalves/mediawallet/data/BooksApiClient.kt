package com.github.fgoncalves.mediawallet.data

import com.github.fgoncalves.mediawallet.config.BOOKS_PATH
import com.github.fgoncalves.mediawallet.data.models.Book
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface BooksApiClient {
  @GET(BOOKS_PATH)
  fun get(@Query("q") query: String?): Single<Book>
}
