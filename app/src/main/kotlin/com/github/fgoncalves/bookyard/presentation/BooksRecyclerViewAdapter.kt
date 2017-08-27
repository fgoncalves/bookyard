package com.github.fgoncalves.bookyard.presentation

import android.support.v7.util.SortedList
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.fgoncalves.bookyard.BookYardApplication
import com.github.fgoncalves.bookyard.databinding.BookCardviewBinding
import com.github.fgoncalves.bookyard.di.BookItemModule
import com.github.fgoncalves.bookyard.di.BooksItemComponent
import com.github.fgoncalves.bookyard.presentation.viewmodels.BookItemViewModel
import javax.inject.Inject

typealias Isbn = String

class ViewHolder(val viewModel: BookItemViewModel, root: View) : RecyclerView.ViewHolder(root)

abstract class BooksRecyclerViewAdapter : RecyclerView.Adapter<ViewHolder>() {
  abstract fun add(book: Isbn)

  abstract fun add(books: List<Isbn>)

  abstract fun remove(book: Isbn)

  abstract fun remove(books: List<Isbn>)

  abstract fun replaceAll(books: List<Isbn>)

  abstract fun replace(book: Isbn, position: Int)

  abstract fun itemPosition(book: Isbn?): Int?
}

class BooksRecyclerViewAdapterImpl @Inject constructor(
    val application: BookYardApplication
) : BooksRecyclerViewAdapter() {
  private var _component: BooksItemComponent? = null
  private var component: BooksItemComponent
    get() {
      if (_component == null) {
        _component = application.applicationComponent?.plus(BookItemModule)
      }
      return _component!!
    }
    set(value) {
      _component = null
    }

  private val sortedListCallbacks = object : SortedList.Callback<Isbn>() {
    override fun onChanged(position: Int, count: Int)
        = notifyItemChanged(position, count)

    override fun onInserted(position: Int, count: Int)
        = notifyItemRangeInserted(position, count)

    override fun compare(o1: Isbn?, o2: Isbn?): Int =
        if (o2 == null) -1
        else
          o1?.compareTo(o2) ?: -1

    override fun onRemoved(position: Int, count: Int)
        = notifyItemRangeRemoved(position, count)

    override fun areItemsTheSame(item1: Isbn?, item2: Isbn?): Boolean
        = item1 == item2

    override fun onMoved(fromPosition: Int, toPosition: Int)
        = notifyItemMoved(fromPosition, toPosition)

    override fun areContentsTheSame(oldItem: Isbn?, newItem: Isbn?): Boolean
        = oldItem == newItem
  }
  private val sortedList = SortedList(Isbn::class.java, sortedListCallbacks)

  override fun add(book: Isbn) {
    sortedList.add(book)
  }

  override fun add(books: List<Isbn>) {
    sortedList.addAll(books)
  }

  override fun remove(book: Isbn) {
    sortedList.remove(book)
  }

  override fun remove(books: List<Isbn>) {
    if (books.isEmpty()) return
    sortedList.beginBatchedUpdates()
    books.forEach { sortedList.remove(it) }
    sortedList.endBatchedUpdates()
  }

  override fun replace(book: Isbn, position: Int) {
    sortedList.updateItemAt(position, book)
  }

  override fun replaceAll(books: List<Isbn>) {
    sortedList.clear()
    notifyDataSetChanged()
    sortedList.addAll(books)
  }

  override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
    val binding = BookCardviewBinding.inflate(LayoutInflater.from(parent?.context), parent, false)

    val viewModel = component.booksItemVieModel()
    binding.viewModel = viewModel
    return ViewHolder(viewModel, binding.root)
  }

  override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
    holder?.viewModel?.bindModel(sortedList[position])
  }

  override fun getItemCount(): Int = sortedList.size()

  override fun itemPosition(book: Isbn?): Int? {
    val index = sortedList.indexOf(book)
    return if (index == SortedList.INVALID_POSITION) null else index
  }
}
