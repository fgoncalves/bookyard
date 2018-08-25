package com.github.fgoncalves.bookyard.domain.usecases

import com.github.fgoncalves.bookyard.data.BooksService
import com.google.firebase.auth.FirebaseAuth
import io.reactivex.Completable
import javax.inject.Inject

/**
 * Delete a book of the current user's collection
 */
interface DeleteBookUseCase {
    /**
     * The isbn of the book to delete
     */
    fun delete(isbn: String): Completable
}

class DeleteBookUseCaseImpl @Inject constructor(
        private val firebaseAuth: FirebaseAuth,
        private val booksService: BooksService
) : DeleteBookUseCase {
    override fun delete(isbn: String): Completable = booksService.delete(
            firebaseAuth.currentUser?.uid
                    ?: throw IllegalStateException("No current logged in user"),
            isbn)
}
