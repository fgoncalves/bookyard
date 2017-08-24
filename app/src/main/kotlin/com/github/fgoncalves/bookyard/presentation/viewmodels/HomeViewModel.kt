package com.github.fgoncalves.bookyard.presentation.viewmodels

import android.arch.lifecycle.Lifecycle.Event.ON_CREATE
import android.arch.lifecycle.Lifecycle.Event.ON_PAUSE
import android.arch.lifecycle.Lifecycle.Event.ON_RESUME
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import android.arch.lifecycle.ViewModel
import android.databinding.ObservableInt
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import com.github.fgoncalves.bookyard.R
import com.github.fgoncalves.bookyard.domain.usecases.GetBooksDatabaseReferenceUseCase
import com.github.fgoncalves.bookyard.presentation.BooksRecyclerViewAdapter
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import timber.log.Timber
import javax.inject.Inject

abstract class HomeViewModel : ViewModel(), LifecycleObserver {
  protected var errorCallback: ((Int) -> Unit)? = null

  abstract val recyclerViewVisibility: ObservableInt

  abstract val emptyViewVisibility: ObservableInt

  abstract val booksListAdapter: RecyclerView.Adapter<*>

  abstract val progressBarVisibility: ObservableInt

  abstract fun floatingActionButtonClicked(view: View)

  fun onError(errorCallback: ((Int) -> Unit)?) {
    this.errorCallback = errorCallback
  }
}

class HomeViewModelImpl @Inject constructor(
    val getBooksDatabaseReferenceUseCase: GetBooksDatabaseReferenceUseCase,
    val recyclerViewAdapter: BooksRecyclerViewAdapter
) : HomeViewModel() {
  override val recyclerViewVisibility = ObservableInt(GONE)

  override val emptyViewVisibility = ObservableInt(GONE)

  override val booksListAdapter: RecyclerView.Adapter<*> = recyclerViewAdapter

  override val progressBarVisibility: ObservableInt = ObservableInt(VISIBLE)

  override fun floatingActionButtonClicked(view: View) {
    TODO(
        "not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  private val booksEventListener = object : ChildEventListener {
    override fun onCancelled(databaseError: DatabaseError?) {
      if (databaseError != null)
        Timber.e(databaseError.toException(), "Failed to get books from firebase")
      errorCallback?.invoke(R.string.failed_to_get_books_from_firebase)
    }

    override fun onChildMoved(dataSnapshot: DataSnapshot?, p1: String?) {
      // TODO: Have no clue what to do here
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

  @OnLifecycleEvent(ON_CREATE)
  fun onScreenCreated() {
    booksDatabaseReference = getBooksDatabaseReferenceUseCase.getBooksDatabaseReference()
  }

  @OnLifecycleEvent(ON_RESUME)
  fun onScreenResumed() {
    showDataLoading()
    booksDatabaseReference?.addChildEventListener(booksEventListener)
  }

  @OnLifecycleEvent(ON_PAUSE)
  fun onScreenPaused() = booksDatabaseReference?.removeEventListener(booksEventListener)

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

  fun RecyclerView.Adapter<*>.isEmpty() = itemCount == 0
}
