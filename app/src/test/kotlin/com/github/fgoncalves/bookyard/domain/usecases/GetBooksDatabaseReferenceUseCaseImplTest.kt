package com.github.fgoncalves.bookyard.domain.usecases

import com.github.fgoncalves.bookyard.data.BooksService
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import io.kotlintest.specs.StringSpec

class GetBooksDatabaseReferenceUseCaseImplTest : StringSpec() {
  init {
    val service = mock<BooksService> { }

    "Use case should get the given book database reference" {
      val currentUserMock = mock<FirebaseUser> {
        on { uid } doReturn "foo"
      }
      val firebaseAuth = mock<FirebaseAuth> {
        on { currentUser } doReturn currentUserMock
      }
      val useCase = GetBooksDatabaseReferenceUseCaseImpl(firebaseAuth, service)

      useCase.getBooksDatabaseReference()

      verify(service).getDatabaseReference("foo")
    }

    "Use case should throw illegal state exception when there's no user logged in" {
      val firebaseAuth = mock<FirebaseAuth> {
        on { currentUser } doReturn null as FirebaseUser?
      }
      val useCase = GetBooksDatabaseReferenceUseCaseImpl(firebaseAuth, service)

      try {
        useCase.getBooksDatabaseReference()
      } catch (e: IllegalStateException) {
      }
    }
  }
}
