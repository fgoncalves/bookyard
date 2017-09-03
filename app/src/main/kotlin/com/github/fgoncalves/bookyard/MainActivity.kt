package com.github.fgoncalves.bookyard

import android.arch.lifecycle.LifecycleRegistry
import android.arch.lifecycle.LifecycleRegistryOwner
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.AppCompatActivity
import android.view.ViewGroup
import com.github.fgoncalves.bookyard.databinding.ProfileBinding
import com.github.fgoncalves.bookyard.presentation.base.ViewModelFactory
import com.github.fgoncalves.bookyard.presentation.screens.SplashScreen
import com.github.fgoncalves.bookyard.presentation.viewmodels.ProfileViewModel
import com.github.fgoncalves.pathmanager.ScreenNavigator
import com.google.firebase.auth.FirebaseAuth
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject

class MainActivity : AppCompatActivity(), HasSupportFragmentInjector, LifecycleRegistryOwner {
  private val lifecycleRegistry = LifecycleRegistry(this)

  lateinit var drawer: DrawerLayout

  @Inject
  lateinit var androidInjector: DispatchingAndroidInjector<Fragment>
  @Inject
  lateinit var screenNavigator: ScreenNavigator
  @Inject
  lateinit var viewmodelFactory: ViewModelFactory
  @Inject
  lateinit var firebaseAuth: FirebaseAuth

  override fun onCreate(savedInstanceState: Bundle?) {
    AndroidInjection.inject(this)
    super.onCreate(savedInstanceState)

    setContentView(R.layout.main)

    drawer = findViewById(R.id.drawer_layout) as DrawerLayout

    setupDrawer()

    if (savedInstanceState == null) screenNavigator.single(SplashScreen.newInstance())
  }

  override fun supportFragmentInjector(): AndroidInjector<Fragment> = androidInjector

  override fun getLifecycle(): LifecycleRegistry = lifecycleRegistry

  override fun onBackPressed() {
    if (!screenNavigator.back()) finish()
  }

  private fun setupDrawer() {
    val drawerView = findViewById(R.id.drawer) as NavigationView?
    val binding = ProfileBinding.inflate(layoutInflater, drawerView as ViewGroup?, false)
    val profileViewModel: ProfileViewModel = ViewModelProviders.of(this,
        viewmodelFactory)[ProfileViewModel::class.java]

    binding.viewModel = profileViewModel
    lifecycleRegistry.addObserver(profileViewModel)

    drawerView?.let {
      drawerView.addHeaderView(binding.root)
      drawerView.setNavigationItemSelectedListener {
        drawer.closeDrawer(GravityCompat.START)
        it.isChecked = true
        when (it.itemId) {
          R.id.signout -> {
            firebaseAuth.signOut()
            screenNavigator.single(SplashScreen.newInstance())
            return@setNavigationItemSelectedListener true
          }
        }
        return@setNavigationItemSelectedListener false
      }
    }
  }
}
