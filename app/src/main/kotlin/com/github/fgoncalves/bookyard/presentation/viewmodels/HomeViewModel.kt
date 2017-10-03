package com.github.fgoncalves.bookyard.presentation.viewmodels

import android.arch.lifecycle.Lifecycle.Event.*
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import android.arch.lifecycle.ViewModel
import android.databinding.ObservableInt
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import com.github.fgoncalves.bookyard.R
import com.github.fgoncalves.bookyard.data.models.User
import com.github.fgoncalves.bookyard.domain.usecases.AddBookUseCase
import com.github.fgoncalves.bookyard.domain.usecases.DeleteBookUseCase
import com.github.fgoncalves.bookyard.domain.usecases.GetBooksDatabaseReferenceUseCase
import com.github.fgoncalves.bookyard.domain.usecases.GetCurrentUserUseCase
import com.github.fgoncalves.bookyard.presentation.BooksRecyclerViewAdapter
import com.github.fgoncalves.bookyard.presentation.screens.BookDetailsScreen
import com.github.fgoncalves.bookyard.presentation.utils.addTo
import com.github.fgoncalves.pathmanager.ScreenNavigator
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

abstract class HomeViewModel : ViewModel(), LifecycleObserver {
    /**
     * Called when there is an error. For now just this, later proper handling...
     */
    var errorCallback: ((Int) -> Unit)? = null
    /**
     * Display a dialog to confirm the deletion of the clicked item. This callback receives a function
     * that should be called when the deletion is confirmed so the view model can delete the book
     */
    var displayDeletionConfirmationDialogCallback: ((isbn: String, confirmed: (isbn: String) -> Unit) -> Unit)? = null

    var onStartCodeScannerCallback: (() -> Unit)? = null

    abstract val recyclerViewVisibility: ObservableInt

    abstract val emptyViewVisibility: ObservableInt

    abstract val booksListAdapter: RecyclerView.Adapter<*>

    abstract val progressBarVisibility: ObservableInt

    abstract fun floatingActionButtonClicked(view: View)

    abstract fun onIsbnScanned(isbn: String)
}

class HomeViewModelImpl @Inject constructor(
        private val getCurrentUserUseCase: GetCurrentUserUseCase,
        private val getBooksDatabaseReferenceUseCase: GetBooksDatabaseReferenceUseCase,
        private val deleteBookUseCase: DeleteBookUseCase,
        private val addBookUseCase: AddBookUseCase,
        private val recyclerViewAdapter: BooksRecyclerViewAdapter,
        private val navigator: ScreenNavigator
) : HomeViewModel() {
    override val recyclerViewVisibility = ObservableInt(GONE)

    override val emptyViewVisibility = ObservableInt(GONE)

    override val booksListAdapter: RecyclerView.Adapter<*> = recyclerViewAdapter

    override val progressBarVisibility: ObservableInt = ObservableInt(VISIBLE)

    override fun floatingActionButtonClicked(view: View) {
        onStartCodeScannerCallback?.invoke()
    }

    private val booksEventListener = object : ChildEventListener {
        override fun onCancelled(databaseError: DatabaseError?) {
            if (databaseError != null)
                Timber.e(databaseError.toException(), "Failed to get books from firebase")
            errorCallback?.invoke(R.string.failed_to_get_books_from_firebase)
        }

        override fun onChildMoved(dataSnapshot: DataSnapshot?, p1: String?) {
            // FIXME: Have no clue what to do here
        }

        override fun onChildChanged(dataSnapshot: DataSnapshot?, previousItem: String?) {
            val isbn = dataSnapshot?.getValue(String::class.java)
            if (isbn != null) {
                val position = recyclerViewAdapter.itemPosition(previousItem)?.plus(1) ?: 0
                recyclerViewAdapter.replace(isbn, position)
            }
        }

        override fun onChildAdded(dataSnapshot: DataSnapshot?, p1: String?) {
            val isbn = dataSnapshot?.getValue(String::class.java)
            if (isbn != null) recyclerViewAdapter.add(isbn)
            toggleLayoutVisibility()
        }

        override fun onChildRemoved(dataSnapshot: DataSnapshot?) {
            val isbn = dataSnapshot?.getValue(String::class.java)
            if (isbn != null) recyclerViewAdapter.remove(isbn)
            toggleLayoutVisibility()
        }
    }
    private var booksDatabaseReference: DatabaseReference? = null
    private val disposables = CompositeDisposable()

    override fun onIsbnScanned(isbn: String) {
        addBookUseCase.add(isbn)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        {},
                        { Timber.e(it, "Failed to add book $isbn") })
                .addTo(disposables)
    }

    @OnLifecycleEvent(ON_CREATE)
    fun onScreenCreated() {
        booksDatabaseReference = getBooksDatabaseReferenceUseCase.getBooksDatabaseReference()
    }

    @OnLifecycleEvent(ON_RESUME)
    fun onScreenResumed() {
        showDataLoading()
        recyclerViewAdapter.onDeleteItemClickListener = {
            displayDeletionConfirmationDialogCallback?.invoke(it) {
                deleteBookUseCase.delete(it)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                {
                                    Timber.d("Finished deleting book $it")
                                },
                                {
                                    Timber.e(it, "Failed to delete book")
                                    errorCallback?.invoke(R.string.failed_to_deletebook)
                                })
                        .addTo(disposables)
            }
        }
        recyclerViewAdapter.onItemClickListener = {
            it?.let { navigator.go(BookDetailsScreen.newInstance().display(it)) }
        }
        getCurrentUserUseCase.get()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        {
                            if (it.hasNoBooks()) {
                                progressBarVisibility.set(GONE)
                                recyclerViewVisibility.set(GONE)
                                emptyViewVisibility.set(VISIBLE)
                            }
                            booksDatabaseReference?.addChildEventListener(booksEventListener)
                        },
                        { Timber.e(it, "Failed to get current user") })
                .addTo(disposables)
    }

    @OnLifecycleEvent(ON_PAUSE)
    fun onScreenPaused() {
        disposables.clear()
        booksDatabaseReference?.removeEventListener(booksEventListener)
        recyclerViewAdapter.onDeleteItemClickListener = null
    }

    private fun showDataLoading() {
        progressBarVisibility.set(VISIBLE)
        recyclerViewVisibility.set(GONE)
        emptyViewVisibility.set(GONE)
    }

    /**
     * Assume that loading is done and hide the loading views. Then assess if we should show the
     * list or the empty view based on the number of items in the adapter.
     */
    private fun toggleLayoutVisibility() {
        progressBarVisibility.set(GONE)
        if (recyclerViewAdapter.isEmpty()) {
            recyclerViewVisibility.set(GONE)
            emptyViewVisibility.set(VISIBLE)
        } else {
            recyclerViewVisibility.set(VISIBLE)
            emptyViewVisibility.set(GONE)
        }
    }

    private fun RecyclerView.Adapter<*>.isEmpty() = itemCount == 0

    private fun User.hasNoBooks(): Boolean = books.isEmpty()
}
