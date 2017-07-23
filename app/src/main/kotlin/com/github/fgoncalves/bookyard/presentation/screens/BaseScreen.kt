package com.github.fgoncalves.bookyard.presentation.screens

import android.support.v4.app.Fragment
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.widget.Toolbar
import android.view.Gravity
import android.view.Menu
import android.view.MenuInflater
import com.github.fgoncalves.bookyard.MainActivity
import com.github.fgoncalves.bookyard.R

abstract class BaseScreen : Fragment() {
  protected companion object {
    @JvmStatic
    val NO_RESOURCE = 0
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
}
