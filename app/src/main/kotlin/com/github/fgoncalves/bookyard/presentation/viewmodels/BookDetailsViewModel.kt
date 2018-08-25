package com.github.fgoncalves.bookyard.presentation.viewmodels

import androidx.databinding.ObservableField
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ViewModel
import com.github.fgoncalves.bookyard.data.models.Item
import com.github.fgoncalves.bookyard.presentation.utils.ScreenNavigator
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

    abstract fun onBackClicked()

    fun forModel(book: Item) = apply { this.book = book }
}

class BookDetailsViewModelImpl @Inject constructor(
        private val navigator: ScreenNavigator
) : BookDetailsViewModel() {
    override val bookCover = ObservableField<String>("")

    override val bookTitle = ObservableField<String>("")

    override val bookAuthors = ObservableField<String>("")

    override val bookDescription = ObservableField<String>("")

    override fun onBackClicked() {
        navigator.back()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onScreenResumed() {
        bookCover.set(book.cover)
        bookTitle.set(book.title)
        bookAuthors.set(book.authors)
        bookDescription.set(book.description)
    }
}
