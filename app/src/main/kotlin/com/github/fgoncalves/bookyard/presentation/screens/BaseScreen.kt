package com.github.fgoncalves.bookyard.presentation.screens

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
import com.github.fgoncalves.bookyard.presentation.utils.LayoutResource
import com.github.fgoncalves.bookyard.presentation.utils.NO_LAYOUT

abstract class BaseScreen<in V : ViewDataBinding> : Fragment() {
  protected companion object {
    @JvmStatic
    val NO_RESOURCE = 0
  }

  override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
      savedInstanceState: Bundle?): View? {
    val layout = layout()
    if (layout == NO_LAYOUT) return null

    val viewDataBinding = DataBindingUtil.inflate<V>(inflater, layout, container, false)

    applyBindings(viewDataBinding)

    return viewDataBinding.root
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

  fun menuResource(): Int = NO_RESOURCE

  fun supportsHomeButton(): Boolean = false

  fun supportsDrawer(): Boolean = false

  fun toolbar(): Toolbar? = null

  fun toolbarTitle(): String = ""

  protected fun applyBindings(viewDataBinding: V) {

  }

  private fun layout(): Int {
    val layoutResource: LayoutResource? = this::class.annotations.find { it is LayoutResource } as LayoutResource
    return layoutResource?.value ?: NO_LAYOUT
  }
}
