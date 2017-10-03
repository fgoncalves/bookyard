package com.github.fgoncalves.bookyard.presentation.viewmodels

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import android.arch.lifecycle.ViewModel
import android.databinding.ObservableField
import com.github.fgoncalves.bookyard.data.models.Item
import javax.inject.Inject

/**
 * Details view model for a given book
 */
abstract class BookDetailsViewModel : ViewModel(), LifecycleObserver {
    protected lateinit var book: Item

    abstract val bookCover: ObservableField<String>

    abstract val bookTitle: ObservableField<String>

    abstract val bookAuthors: ObservableField<String>

    abstract val bookDescription: ObservableField<String>

    fun forModel(book: Item) = apply { this.book = book }
}

class BookDetailsViewModelImpl @Inject constructor() : BookDetailsViewModel() {
    override val bookCover = ObservableField<String>("")

    override val bookTitle = ObservableField<String>("")

    override val bookAuthors = ObservableField<String>("")

    override val bookDescription = ObservableField<String>("")

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onScreenResumed() {
        bookCover.set(book.cover)
        bookTitle.set(book.title)
        bookAuthors.set(book.authors)
        bookDescription.set(book.description)
    }
}
