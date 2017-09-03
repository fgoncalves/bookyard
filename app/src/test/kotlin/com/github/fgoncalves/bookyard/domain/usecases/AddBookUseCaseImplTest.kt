package com.github.fgoncalves.bookyard.domain.usecases

import com.github.fgoncalves.bookyard.data.BooksService
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import io.kotlintest.specs.StringSpec

class AddBookUseCaseImplTest : StringSpec() {
  init {
    "Add should call the underlying service" {
      val currentUserMock = mock<FirebaseUser> {
        on { uid } doReturn "foo"
      }
      val firebaseAuth = mock<FirebaseAuth> {
        on { currentUser } doReturn currentUserMock
      }
      val service = mock<BooksService>()
      val addBookUseCase = AddBookUseCaseImpl(firebaseAuth, service)

      addBookUseCase.add("isbn")

      verify(service).add("foo", "isbn")
    }
  }
}
