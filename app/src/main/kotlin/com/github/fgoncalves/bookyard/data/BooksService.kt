package com.github.fgoncalves.bookyard.data

import com.github.fgoncalves.bookyard.data.models.Book
import com.github.fgoncalves.bookyard.di.qualifiers.UsersDatabase
import com.google.firebase.database.DatabaseReference
import io.reactivex.Single
import javax.inject.Inject

interface BooksService {
  fun get(searchQuery: String): Single<Book>

  fun getDatabaseReference(uid: String): DatabaseReference
}

class BooksServiceImpl @Inject constructor(
    val apiClient: BooksApiClient,
    @UsersDatabase val databaseReference: DatabaseReference
) : BooksService {
  override fun get(searchQuery: String): Single<Book>
      = apiClient.get(searchQuery)

  override fun getDatabaseReference(uid: String): DatabaseReference
      = databaseReference.child(uid).child("books")
}
