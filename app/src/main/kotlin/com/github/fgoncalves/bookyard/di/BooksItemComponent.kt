package com.github.fgoncalves.bookyard.di

import com.github.fgoncalves.bookyard.presentation.viewmodels.BookItemViewModel
import dagger.Subcomponent

@Subcomponent(modules = [BookItemModule::class])
interface BooksItemComponent {
    fun booksItemVieModel(): BookItemViewModel
}
