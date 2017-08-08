package com.github.fgoncalves.bookyard.presentation.utils

import android.os.Build
import android.support.annotation.IdRes
import android.support.transition.Transition
import android.support.v4.app.FragmentManager
import android.view.View
import com.github.fgoncalves.bookyard.presentation.screens.BaseScreen
import javax.inject.Inject

class ScreenNavigatorImpl @Inject constructor(
    val fragmentManager: FragmentManager, @IdRes val container: Int) : ScreenNavigator {
  override fun go(to: BaseScreen<*>, from: BaseScreen<*>?, enterTransition: Transition?,
      enterSharedTransition: Transition?, exitTransition: Transition?,
      exitSharedTransition: Transition?, sharedElement: View?,
      sharedElementTransactionName: String?) {
    val fragmentTransaction = fragmentManager.beginTransaction()
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
      if (exitSharedTransition != null) from?.sharedElementReturnTransition = exitSharedTransition
      if (enterSharedTransition != null) to.sharedElementEnterTransition = enterSharedTransition
      if (exitTransition != null) from?.exitTransition = exitTransition
      if (enterTransition != null) to.enterTransition = enterTransition
      if (sharedElement != null && sharedElementTransactionName != null) {
        fragmentTransaction.addSharedElement(sharedElement, sharedElementTransactionName)
      }
    }

    val canonicalName = to.javaClass.canonicalName
    fragmentTransaction.replace(container, to, canonicalName)
        .addToBackStack(canonicalName)
        .commit()
  }

  override fun single(screen: BaseScreen<*>) {
    fragmentManager.clear()
    go(screen)
  }

  override fun back(): Boolean {
    if (fragmentManager.backStackEntryCount <= 1) return false
    fragmentManager.popBackStackImmediate()
    return true
  }

  /**
   * Clear the stack immediately
   */
  private fun FragmentManager.clear() {
    for (i in 0..backStackEntryCount)
      popBackStackImmediate()
  }
}
