package com.github.fgoncalves.bookyard.presentation.base

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import java.lang.IllegalArgumentException
import javax.inject.Inject
import javax.inject.Provider

class ViewModelFactory @Inject constructor(
        val viewmodels: Map<@JvmSuppressWildcards Class<out ViewModel>, @JvmSuppressWildcards Provider<ViewModel>>) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>?): T {
        if (modelClass == null)
            throw IllegalArgumentException("Cannot create a view model for null class")

        var viewmodelProvider = viewmodels[modelClass]
        if (viewmodelProvider == null) {
            viewmodels.entries.forEach {
                if (modelClass.isAssignableFrom(it.key)) {
                    viewmodelProvider = it.value
                    return@forEach
                }
            }
        }

        if (viewmodelProvider == null)
            throw IllegalArgumentException("Cannot find view model for class $modelClass")

        @Suppress("UNCHECKED_CAST")
        return viewmodelProvider!!.get() as T
    }
}
