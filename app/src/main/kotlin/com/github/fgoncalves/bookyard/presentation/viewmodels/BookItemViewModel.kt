package com.github.fgoncalves.bookyard.presentation.viewmodels

import android.databinding.ObservableField
import com.github.fgoncalves.bookyard.data.models.Item

interface BookItemViewModel {
  val title: ObservableField<String>
  val authors: ObservableField<String>
  val coverUrl: ObservableField<String>

  fun onBindViewHolder(item: Item)
}

class BookItemViewModelImpl : BookItemViewModel {
  override val title = ObservableField<String>("")
  override val authors = ObservableField<String>("")
  override val coverUrl = ObservableField<String>("")

  override fun onBindViewHolder(item: Item) {
    title.set(item.volumeInfo?.title ?: "")
    authors.set(item.authors())
    coverUrl.set(item.volumeInfo?.imageLinks?.thumbnail ?: "")
  }

  fun Item.authors(): String
      = volumeInfo?.authors?.joinToString(",") ?: ""
}
