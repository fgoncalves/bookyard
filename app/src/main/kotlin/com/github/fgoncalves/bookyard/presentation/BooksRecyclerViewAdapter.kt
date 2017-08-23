package com.github.fgoncalves.bookyard.presentation

import android.support.v7.util.SortedList
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.github.fgoncalves.bookyard.data.models.Item
import com.github.fgoncalves.bookyard.presentation.viewmodels.BookItemViewModel

class ViewHolder(val viewModel: BookItemViewModel, root: View) : RecyclerView.ViewHolder(root) {

}

abstract class BooksRecyclerViewAdapter : RecyclerView.Adapter<ViewHolder>() {
  abstract fun add(book: Item)

  abstract fun add(books: List<Item>)

  abstract fun remove(book: Item)

  abstract fun remove(books: List<Item>)

  abstract fun replaceAll(books: List<Item>)
}

class BooksRecyclerViewAdapterImpl : BooksRecyclerViewAdapter() {
  private val sortedListCallbacks = object : SortedList.Callback<Item>() {
    override fun onChanged(position: Int, count: Int)
        = notifyItemChanged(position, count)

    override fun onInserted(position: Int, count: Int)
        = notifyItemRangeInserted(position, count)

    override fun compare(o1: Item?, o2: Item?): Int {
      if (o2?.volumeInfo?.title == null) return -1
      return o1?.volumeInfo?.title?.compareTo(o2.volumeInfo.title) ?: -1
    }

    override fun onRemoved(position: Int, count: Int)
        = notifyItemRangeRemoved(position, count)

    override fun areItemsTheSame(item1: Item?, item2: Item?): Boolean
        = item1 == item2

    override fun onMoved(fromPosition: Int, toPosition: Int)
        = notifyItemMoved(fromPosition, toPosition)

    override fun areContentsTheSame(oldItem: Item?, newItem: Item?): Boolean
        = oldItem?.id == newItem?.id
  }
  private val sortedList = SortedList(Item::class.java, sortedListCallbacks)

  override fun add(book: Item) {
    sortedList.add(book)
  }

  override fun add(books: List<Item>) {
    sortedList.addAll(books)
  }

  override fun remove(book: Item) {
    sortedList.remove(book)
  }

  override fun remove(books: List<Item>) {
    if (books.isEmpty()) return
    sortedList.beginBatchedUpdates()
    books.forEach { sortedList.remove(it) }
    sortedList.endBatchedUpdates()
  }

  override fun replaceAll(books: List<Item>) {
    sortedList.clear()
    notifyDataSetChanged()
    sortedList.addAll(books)
  }

  override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
    TODO(
        "not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
    TODO(
        "not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun getItemCount(): Int = sortedList.size()
}
