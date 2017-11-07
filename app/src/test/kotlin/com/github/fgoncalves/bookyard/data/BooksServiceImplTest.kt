package com.github.fgoncalves.bookyard.data

import com.github.fgoncalves.bookyard.data.models.Book
import com.github.fgoncalves.bookyard.data.models.FirebaseDatabaseException
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.nhaarman.mockito_kotlin.*
import io.kotlintest.specs.StringSpec
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

class BooksServiceImplTest : StringSpec() {

    init {
        "service calls the api client and doesn't filter book" {
            val apiClient = mock<BooksApiClient> {
                on { get("isbn") } doReturn Single.just(Book("book", 12, emptyList()))
            }
            val service = BooksServiceImpl(apiClient, mock())

            service.get("isbn")
                    .subscribeOn(Schedulers.trampoline())
                    .observeOn(Schedulers.trampoline())
                    .test()
                    .assertValue(Book("book", 12, emptyList()))
        }

        "service calls the api client and filters books" {
            val apiClient = mock<BooksApiClient> {
                on { get("isbn") } doReturn Single.just(Book("book", 0, emptyList()))
            }
            val service = BooksServiceImpl(apiClient, mock())

            service.get("isbn")
                    .subscribeOn(Schedulers.trampoline())
                    .observeOn(Schedulers.trampoline())
                    .test()
                    .assertNoValues()
        }

        "get books database reference calls proper database reference" {
            val booksMock = mock<DatabaseReference> {
                on { child("books") } doReturn mock<DatabaseReference>()
            }
            val databaseReference = mock<DatabaseReference> {
                on { child("foo") } doReturn booksMock
            }
            val service = BooksServiceImpl(mock(), databaseReference)

            service.getDatabaseReference("foo")

            verify(databaseReference).child("foo")
            verify(booksMock).child("books")
        }

        "delete should remove the given book from the list" {
            val booksMock = mock<DatabaseReference> {
                on { addListenerForSingleValueEvent(any()) } doAnswer {
                    (it.arguments[0] as ValueEventListener).onDataChange(mock())
                }
            }
            val databaseReference = mock<DatabaseReference> {
                on { child("foo") } doReturn booksMock
            }
            val service = BooksServiceImpl(mock(), databaseReference)

            service.delete("foo", "isbn")
                    .subscribeOn(Schedulers.trampoline())
                    .observeOn(Schedulers.trampoline())
                    .test()
                    .assertNoErrors()
                    .assertComplete()
        }

        "Add should add book to the user's collection when successful" {
            val booksMock = mock<DatabaseReference> {
                on { addListenerForSingleValueEvent(any()) } doAnswer {
                    (it.arguments[0] as ValueEventListener).onDataChange(mock())
                }
            }
            val databaseReference = mock<DatabaseReference> {
                on { child("foo") } doReturn booksMock
            }
            val service = BooksServiceImpl(mock(), databaseReference)

            service.add("foo", "isbn")
                    .subscribeOn(Schedulers.trampoline())
                    .observeOn(Schedulers.trampoline())
                    .test()
                    .assertNoErrors()
                    .assertComplete()
        }

        "Add should error with a FirebaseDatabaseException when book is not added to collection due to a database error" {
            val booksMock = mock<DatabaseReference> {
                on { addListenerForSingleValueEvent(any()) } doAnswer {
                    (it.arguments[0] as ValueEventListener).onCancelled(mock())
                }
            }
            val databaseReference = mock<DatabaseReference> {
                on { child("foo") } doReturn booksMock
            }
            val service = BooksServiceImpl(mock(), databaseReference)

            service.add("foo", "isbn")
                    .subscribeOn(Schedulers.trampoline())
                    .observeOn(Schedulers.trampoline())
                    .test()
                    .assertError(FirebaseDatabaseException::class.java)
        }

        "Add should error with a RuntimeException when book is not added to collection due to an unknown error" {
            val booksMock = mock<DatabaseReference> {
                on { addListenerForSingleValueEvent(any()) } doAnswer {
                    (it.arguments[0] as ValueEventListener).onCancelled(null)
                }
            }
            val databaseReference = mock<DatabaseReference> {
                on { child("foo") } doReturn booksMock
            }
            val service = BooksServiceImpl(mock(), databaseReference)

            service.add("foo", "isbn")
                    .subscribeOn(Schedulers.trampoline())
                    .observeOn(Schedulers.trampoline())
                    .test()
                    .assertError(RuntimeException::class.java)
        }
    }
}
