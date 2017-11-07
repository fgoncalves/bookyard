package com.github.fgoncalves.bookyard.domain.usecases

import com.github.fgoncalves.bookyard.data.BooksService
import com.google.firebase.auth.FirebaseAuth
import io.reactivex.Completable
import javax.inject.Inject

/**
 * Add the given book to the current logged in user
 */
interface AddBookUseCase {
    /**
     * Add the given isbn to the current logged in code
     *
     * @param isbn The isbn to add
     */
    fun add(isbn: String): Completable
}

class AddBookUseCaseImpl @Inject constructor(
        private val firebaseAuth: FirebaseAuth,
        private val booksService: BooksService
) : AddBookUseCase {
    override fun add(isbn: String): Completable
            = booksService.add(
            firebaseAuth.currentUser?.uid ?: throw IllegalArgumentException("No user logged in"),
            isbn)
}
