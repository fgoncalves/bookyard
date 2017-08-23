package com.github.fgoncalves.bookyard.presentation.viewmodels

import android.arch.lifecycle.Lifecycle.Event.ON_CREATE
import android.arch.lifecycle.Lifecycle.Event.ON_PAUSE
import android.arch.lifecycle.Lifecycle.Event.ON_RESUME
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import android.arch.lifecycle.ViewModel
import android.databinding.ObservableInt
import android.support.v7.widget.RecyclerView
import android.view.View.GONE
import android.view.View.OnClickListener
import android.view.View.VISIBLE
import com.github.fgoncalves.bookyard.domain.usecases.GetBooksDatabaseReferenceUseCase
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import javax.inject.Inject

abstract class HomeViewModel : ViewModel(), LifecycleObserver {
  abstract val recyclerViewVisibility: ObservableInt

  abstract val emptyViewVisibility: ObservableInt

  abstract val booksListAdapter: RecyclerView.Adapter<*>

  abstract val progressBarVisibility: ObservableInt

  abstract val floatingActionButtonClicked: OnClickListener
}

class HomeViewModelImpl @Inject constructor(
    val getBooksDatabaseReferenceUseCase: GetBooksDatabaseReferenceUseCase
) : HomeViewModel() {
  override val recyclerViewVisibility = ObservableInt(GONE)

  override val emptyViewVisibility = ObservableInt(GONE)

  override val booksListAdapter: RecyclerView.Adapter<*>
    get() = TODO()

  override val progressBarVisibility: ObservableInt = ObservableInt(VISIBLE)

  override val floatingActionButtonClicked = OnClickListener {

  }

  private val booksEventListener = object : ChildEventListener {
    override fun onCancelled(databaseError: DatabaseError?) {
      TODO(
          "not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onChildMoved(dataSnapshot: DataSnapshot?, p1: String?) {
      TODO(
          "not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onChildChanged(dataSnapshot: DataSnapshot?, p1: String?) {
      TODO(
          "not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onChildAdded(dataSnapshot: DataSnapshot?, p1: String?) {
      TODO(
          "not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onChildRemoved(dataSnapshot: DataSnapshot?) {
      TODO(
          "not implemented") //To change body of created functions use File | Settings | File Templates.
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

}
