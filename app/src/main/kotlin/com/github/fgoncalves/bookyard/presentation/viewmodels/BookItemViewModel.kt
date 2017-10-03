package com.github.fgoncalves.bookyard.presentation.viewmodels

import android.databinding.ObservableField
import com.github.fgoncalves.bookyard.data.models.Item
import com.github.fgoncalves.bookyard.domain.usecases.GetBookUseCase
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

typealias Isbn = String

interface BookItemViewModel {
    val title: ObservableField<String>
    val authors: ObservableField<String>
    val coverUrl: ObservableField<String>

    fun bindModel(item: Isbn)

    fun setOnItemClickListener(onItemClickListener: ((book: Item?) -> Unit)?)

    fun onItemClicked()
}

class BookItemViewModelImpl @Inject constructor(
        private val getBookUseCase: GetBookUseCase
) : BookItemViewModel {
    override val title = ObservableField<String>("")
    override val authors = ObservableField<String>("")
    override val coverUrl = ObservableField<String>("")

    private var book: Item? = null
    private var onItemClickListener: ((book: Item?) -> Unit)? = null

    override fun bindModel(item: Isbn) {
        // TODO: Blur the card
        title.set("")
        authors.set("")
        coverUrl.set("")
        getBookUseCase.get(item)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        {
                            book = it.items[0]
                            title.set(book?.title ?: "")
                            authors.set(book?.authors ?: "")
                            coverUrl.set(book?.cover ?: "")
                        },
                        { Timber.e(it, "Failed to get book with isbn: $item") })
    }

    override fun setOnItemClickListener(onItemClickListener: ((book: Item?) -> Unit)?) {
        this.onItemClickListener = onItemClickListener
    }

    override fun onItemClicked() {
        onItemClickListener?.invoke(book)
    }
}
