package com.github.fgoncalves.bookyard.presentation.screens

import android.arch.lifecycle.LifecycleRegistry
import android.arch.lifecycle.LifecycleRegistryOwner
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import com.github.fgoncalves.bookyard.R
import com.github.fgoncalves.bookyard.databinding.SplashScreenBinding
import com.github.fgoncalves.bookyard.presentation.viewmodels.SplashScreenViewModel
import com.github.fgoncalves.pathmanager.ScreenNavigator
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.common.GooglePlayServicesUtil
import javax.inject.Inject
import kotlin.properties.Delegates


class SplashScreen : BaseScreen<SplashScreenBinding>(), LifecycleRegistryOwner {
    override val layout: Int = R.layout.splash_screen
    private val lifecycleRegistry = LifecycleRegistry(this)
    private var viewModel: SplashScreenViewModel by Delegates.notNull()

    @Inject lateinit var navigator: ScreenNavigator

    companion object {
        @JvmStatic
        fun newInstance(): SplashScreen = SplashScreen()

        const val RC_SIGN_IN = 0xbeef
        const val REQUEST_CODE_RECOVER_PLAY_SERVICES = 0xcafe
    }

    override fun applyBindings(viewDataBinding: SplashScreenBinding) {
        viewModel = ViewModelProviders.of(this, viewModelFactory)[SplashScreenViewModel::class.java]
        viewDataBinding.viewModel = viewModel

        viewModel.onSignInWithGoogle {
            val signInIntent = Auth.GoogleSignInApi.getSignInIntent(it)
            startActivityForResult(signInIntent, RC_SIGN_IN)
        }.onError {
            showErrorDialog(it)
        }.onTransitionToHomeScreen {
            navigator.single(HomeScreen.newInstance())
        }.onGooglePlayServicesUnavailable {
            GooglePlayServicesUtil.showErrorDialogFragment(it,
                    activity, this, REQUEST_CODE_RECOVER_PLAY_SERVICES) {
                showErrorDialog(getString(R.string.cannot_continue_with_services))
            }
        }
        lifecycle.addObserver(viewModel)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.onActivityCreated()
    }

    override fun getLifecycle(): LifecycleRegistry = lifecycleRegistry

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode != RC_SIGN_IN) return

        val result = Auth.GoogleSignInApi.getSignInResultFromIntent(data)
        if (result != null && result.isSuccess) {
            val account = result.signInAccount
            viewModel.onSignedIn(account)
            return
        }

        viewModel.onSignedFailed()
    }

    private fun showErrorDialog(errorMsg: String) {
        AlertDialog.Builder(context)
                .setTitle(getString(R.string.error_dialog_title))
                .setMessage(errorMsg)
                .setPositiveButton(R.string.ok) { _, _ -> }
                .show()
    }
}
