package com.github.fgoncalves.bookyard.presentation.screens

import android.arch.lifecycle.ViewModelProvider
import android.content.Context
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.widget.Toolbar
import android.view.Gravity
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import com.github.fgoncalves.bookyard.MainActivity
import com.github.fgoncalves.bookyard.R
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

abstract class BaseScreen<in V : ViewDataBinding> : Fragment() {
  protected companion object {
    @JvmStatic
    val NO_RESOURCE = 0
  }

  protected abstract val layout: Int

  @Inject lateinit var viewModelFactory: ViewModelProvider.Factory

  override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
      savedInstanceState: Bundle?): View? {
    val viewDataBinding = DataBindingUtil.inflate<V>(inflater, layout, container, false)

    applyBindings(viewDataBinding)

    viewDataBinding.executePendingBindings()

    return viewDataBinding.root
  }

  override fun onAttach(context: Context?) {
    AndroidSupportInjection.inject(this)
    super.onAttach(context)
  }

  override fun onResume() {
    super.onResume()
    val mainActivity: MainActivity = activity as MainActivity
    val drawer = mainActivity.drawer

    if (supportsDrawer()) {
      drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED, Gravity.START)
    } else {
      drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, Gravity.START)
    }

    val toolbar = toolbar()
    if (toolbar != null) {
      mainActivity.setSupportActionBar(toolbar)
      mainActivity.supportActionBar?.title = toolbarTitle()

      if (supportsDrawer()) {
        val toggle = ActionBarDrawerToggle(mainActivity, drawer, toolbar, R.string.open_drawer,
            R.string.close_drawer)

        drawer.addDrawerListener(toggle)
        toggle.syncState()
      } else {
        mainActivity.supportActionBar?.setDisplayHomeAsUpEnabled(supportsHomeButton())
      }
    }
  }

  override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
    super.onCreateOptionsMenu(menu, inflater)
    val menuId = menuResource()
    if (menuId == NO_RESOURCE) return
    inflater?.inflate(menuId, menu)
  }

  open fun menuResource(): Int = NO_RESOURCE

  open fun supportsHomeButton(): Boolean = false

  open fun supportsDrawer(): Boolean = false

  open fun toolbar(): Toolbar? = null

  open fun toolbarTitle(): String = ""

  open protected fun applyBindings(viewDataBinding: V) {

  }
}
