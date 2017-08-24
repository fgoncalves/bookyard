package com.github.fgoncalves.bookyard.presentation.viewmodels

import android.databinding.ObservableField
import com.github.fgoncalves.bookyard.data.models.Item
import com.github.fgoncalves.bookyard.di.qualifiers.NetworkSchedulerTransformer
import com.github.fgoncalves.bookyard.domain.usecases.GetBookUseCase
import com.github.fgoncalves.rx_schedulers.SchedulerTransformer
import timber.log.Timber
import javax.inject.Inject

typealias Isbn = String

interface BookItemViewModel {
  val title: ObservableField<String>
  val authors: ObservableField<String>
  val coverUrl: ObservableField<String>

  fun bindModel(item: Isbn)
}

class BookItemViewModelImpl @Inject constructor(
    val getBookUseCase: GetBookUseCase,
    @NetworkSchedulerTransformer
    val schedulerTransformer: SchedulerTransformer
) : BookItemViewModel {
  override val title = ObservableField<String>("")
  override val authors = ObservableField<String>("")
  override val coverUrl = ObservableField<String>("")

  override fun bindModel(item: Isbn) {
    // TODO: Blur the card
    title.set("")
    authors.set("")
    coverUrl.set("")
    getBookUseCase.get(item)
        .compose(schedulerTransformer.applySingleSchedulers())
        .subscribe(
            {
              val book = it.items[0]
              title.set(book.volumeInfo?.title ?: "")
              authors.set(book.authors())
              coverUrl.set(book.volumeInfo?.imageLinks?.thumbnail ?: "")
            },
            {Timber.e(it, "Failed to get book with isbn: $item")})
  }

  fun Item.authors(): String
      = volumeInfo?.authors?.joinToString(",") ?: ""
}
