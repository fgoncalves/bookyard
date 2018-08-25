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
import io.reactivex.CompletableEmitter
import io.reactivex.Maybe
import javax.inject.Inject

interface BooksService {
    fun get(searchQuery: String): Maybe<Book>

    fun getDatabaseReference(uid: String): DatabaseReference

    fun delete(uid: String, isbn: String): Completable

    fun add(uid: String, isbn: String): Completable
}

class BooksServiceImpl @Inject constructor(
        private val apiClient: BooksApiClient,
        @UsersDatabase private val databaseReference: DatabaseReference
) : BooksService {
    override fun get(searchQuery: String): Maybe<Book> =
            apiClient.get(searchQuery).filter { it.totalItems != 0 }

    override fun getDatabaseReference(uid: String): DatabaseReference =
            databaseReference.child(uid).child("books")

    override fun delete(uid: String, isbn: String): Completable = Completable.create {
        it.runOnUser(uid) {
            val newUser = it.copy(books = it.books.filter { it != isbn })
            databaseReference.child(uid).setValue(newUser)
        }
    }

    override fun add(uid: String, isbn: String): Completable = Completable.create {
        it.runOnUser(uid) {
            val newUser = it.copy(books = it.books + listOf(isbn))
            databaseReference.child(uid).setValue(newUser)
        }
    }

    private fun CompletableEmitter.runOnUser(uid: String, callback: (User) -> Unit) {
        takeUnless { isDisposed }?.let {
            databaseReference.child(uid).addListenerForSingleValueEvent(
                    object : ValueEventListener {
                        override fun onCancelled(error: DatabaseError) {
                            onError(FirebaseDatabaseException(error))
                        }

                        override fun onDataChange(snapshot: DataSnapshot) {
                            snapshot.getValue(User::class.java)?.let(callback)
                            onComplete()
                        }
                    }
            )
        }
    }
}
