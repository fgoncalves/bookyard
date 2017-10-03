package com.github.fgoncalves.bookyard.presentation.screens

import android.arch.lifecycle.LifecycleRegistry
import android.arch.lifecycle.LifecycleRegistryOwner
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.support.v7.app.AlertDialog
import android.support.v7.widget.Toolbar
import android.view.ContextThemeWrapper
import com.github.fgoncalves.bookyard.R
import com.github.fgoncalves.bookyard.databinding.HomeBinding
import com.github.fgoncalves.bookyard.presentation.viewmodels.HomeViewModel
import com.google.zxing.integration.android.IntentIntegrator


class HomeScreen : BaseScreen<HomeBinding>(), LifecycleRegistryOwner {
    override val layout = R.layout.home

    private val lifecycleRegistry = LifecycleRegistry(this)
    private lateinit var viewModel: HomeViewModel

    companion object {
        @JvmStatic
        fun newInstance(): HomeScreen = HomeScreen()
    }

    override fun getLifecycle(): LifecycleRegistry = lifecycleRegistry

    override fun applyBindings(viewDataBinding: HomeBinding) {
        viewModel = ViewModelProviders.of(this, viewModelFactory)[HomeViewModel::class.java]
        viewDataBinding.viewModel = viewModel

        lifecycle.addObserver(viewModel)

        viewModel.errorCallback = {
            AlertDialog.Builder(ContextThemeWrapper(context, R.style.AppTheme))
                    .setTitle(R.string.error_dialog_title)
                    .setMessage(getString(it))
                    .setPositiveButton(R.string.ok, { _, _ -> })
                    .show()
        }

        viewModel.displayDeletionConfirmationDialogCallback = { isbn, confirmCallback ->
            AlertDialog.Builder(ContextThemeWrapper(context, R.style.AppTheme))
                    .setTitle(R.string.remove_book)
                    .setMessage(R.string.confirmation_of_deletion)
                    .setNegativeButton(R.string.no, { _, _ -> })
                    .setPositiveButton(R.string.yes, { _, _ ->
                        confirmCallback.invoke(isbn)
                    })
                    .setCancelable(false)
                    .show()
        }

        viewModel.onStartCodeScannerCallback = {
            IntentIntegrator.forSupportFragment(this@HomeScreen).initiateScan()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result == null) {
            super.onActivityResult(requestCode, resultCode, data)
            return
        }

        result.contents?.let { viewModel.onIsbnScanned(result.contents) }
    }

    override fun toolbar(): Toolbar?
            = view?.findViewById(R.id.home_toolbar) as Toolbar?

    override fun toolbarTitle(): String = context.getString(R.string.app_name)

    override fun supportsDrawer(): Boolean = true
}
