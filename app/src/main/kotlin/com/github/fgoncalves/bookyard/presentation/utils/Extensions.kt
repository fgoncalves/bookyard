package com.github.fgoncalves.bookyard.presentation.utils

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

/**
 * Add this disposable to the list of disposables
 */
fun Disposable.addTo(compositeDisposable: CompositeDisposable) =
        compositeDisposable.add(this)
