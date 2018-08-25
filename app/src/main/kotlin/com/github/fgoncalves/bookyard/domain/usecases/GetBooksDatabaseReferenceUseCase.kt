package com.github.fgoncalves.bookyard.domain.usecases

import com.github.fgoncalves.bookyard.data.BooksService
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import javax.inject.Inject

interface GetBooksDatabaseReferenceUseCase {
    fun getBooksDatabaseReference(): DatabaseReference
}

class GetBooksDatabaseReferenceUseCaseImpl @Inject constructor(
        private val firebaseAuth: FirebaseAuth,
        private val booksService: BooksService
) : GetBooksDatabaseReferenceUseCase {

    override fun getBooksDatabaseReference(): DatabaseReference = booksService.getDatabaseReference(
            firebaseAuth.currentUser?.uid
                    ?: throw IllegalStateException("No current logged in user"))
}
