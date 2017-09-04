package com.github.fgoncalves.bookyard.presentation.viewmodels

import android.arch.lifecycle.ViewModel
import android.databinding.ObservableField
import android.graphics.Bitmap

/**
 * Details view model for a given book
 */
abstract class BookDetailsViewModel : ViewModel() {
  abstract val bookCover: ObservableField<Bitmap>

  abstract val bookTitle: ObservableField<String>
}
