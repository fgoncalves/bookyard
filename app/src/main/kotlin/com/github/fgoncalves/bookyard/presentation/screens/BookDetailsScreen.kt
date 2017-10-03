package com.github.fgoncalves.bookyard.presentation.screens

import android.arch.lifecycle.LifecycleRegistry
import android.arch.lifecycle.LifecycleRegistryOwner
import android.arch.lifecycle.ViewModelProviders
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import com.github.fgoncalves.bookyard.R
import com.github.fgoncalves.bookyard.data.models.Item
import com.github.fgoncalves.bookyard.databinding.BookDetailsBinding
import com.github.fgoncalves.bookyard.presentation.viewmodels.BookDetailsViewModel

class BookDetailsScreen : BaseScreen<BookDetailsBinding>(), LifecycleRegistryOwner {
    override val layout = R.layout.book_details

    private val lifecycleRegistry = LifecycleRegistry(this)
    private lateinit var viewModel: BookDetailsViewModel
    private lateinit var book: Item

    companion object {
        @JvmStatic
        fun newInstance(): BookDetailsScreen = BookDetailsScreen()
    }

    override fun getLifecycle(): LifecycleRegistry = lifecycleRegistry

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
