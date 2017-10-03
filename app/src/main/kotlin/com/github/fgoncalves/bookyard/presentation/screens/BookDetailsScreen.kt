package com.github.fgoncalves.bookyard.presentation.screens

import android.arch.lifecycle.LifecycleRegistry
import android.arch.lifecycle.LifecycleRegistryOwner
import android.arch.lifecycle.ViewModelProviders
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

    fun display(book: Item) = apply {
        this.book = book
    }
}
