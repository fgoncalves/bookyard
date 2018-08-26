package com.github.fgoncalves.bookyard.presentation.screens

import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProviders
import com.github.fgoncalves.bookyard.R
import com.github.fgoncalves.bookyard.data.models.Item
import com.github.fgoncalves.bookyard.databinding.BookDetailsBinding
import com.github.fgoncalves.bookyard.presentation.viewmodels.BookDetailsViewModel

class BookDetailsScreen : BaseScreen<BookDetailsBinding>() {
    override val layout = R.layout.book_details

    private lateinit var viewModel: BookDetailsViewModel
    private lateinit var book: Item

    companion object {
        @JvmStatic
        fun newInstance(): BookDetailsScreen = BookDetailsScreen()
    }

    override fun applyBindings(viewDataBinding: BookDetailsBinding) {
        viewModel = ViewModelProviders.of(this, viewModelFactory)[BookDetailsViewModel::class.java]
        viewDataBinding.viewModel = viewModel

        lifecycle.addObserver(viewModel)

        viewModel.forModel(book)
    }

    override fun toolbar(): Toolbar? =
            view?.findViewById(R.id.book_details_toolbar) as Toolbar?

    override fun supportsHomeButton(): Boolean = true

    override fun onOptionsItemSelected(item: MenuItem?): Boolean =
            if (item?.itemId == android.R.id.home) {
                viewModel.onBackClicked()
                true
            } else
                super.onOptionsItemSelected(item)

    fun display(book: Item) = apply {
        this.book = book
    }
}
