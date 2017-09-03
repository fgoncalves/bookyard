package com.github.fgoncalves.bookyard.data

import com.github.fgoncalves.bookyard.data.models.Book
import com.github.fgoncalves.bookyard.data.models.FirebaseDatabaseException
import com.github.fgoncalves.bookyard.data.models.User
import com.github.fgoncalves.bookyard.di.qualifiers.UsersDatabase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject

interface BooksService {
  fun get(searchQuery: String): Single<Book>

  fun getDatabaseReference(uid: String): DatabaseReference

  fun delete(uid: String, isbn: String): Completable

  fun add(uid: String, isbn: String): Completable
}

class BooksServiceImpl @Inject constructor(
    val apiClient: BooksApiClient,
    @UsersDatabase val databaseReference: DatabaseReference
) : BooksService {
  override fun get(searchQuery: String): Single<Book>
      = apiClient.get(searchQuery)

  override fun getDatabaseReference(uid: String): DatabaseReference
      = databaseReference.child(uid).child("books")

  override fun delete(uid: String, isbn: String): Completable
      = Completable.fromCallable {
    databaseReference.child(uid).child("books").equalTo(isbn).ref.removeValue()
  }

  override fun add(uid: String, isbn: String): Completable
      = Completable.create {
    if (it.isDisposed) return@create

    databaseReference.child(uid).addListenerForSingleValueEvent(
        object : ValueEventListener {
          override fun onCancelled(error: DatabaseError?) {
            if (error == null) throw RuntimeException("Could not add book to user's collection")
            it.onError(FirebaseDatabaseException(error))
          }

          override fun onDataChange(snapshot: DataSnapshot?) {
            snapshot?.let {
              val user = snapshot.getValue(User::class.java) ?: return@let
              val newUser = user.copy(books = user.books + listOf(isbn))
              databaseReference.child(uid).setValue(newUser)
            }
            it.onComplete()
          }
        })
  }
}
