package com.github.fgoncalves.bookyard.presentation.utils

import android.support.transition.Transition
import android.view.View
import com.github.fgoncalves.bookyard.presentation.screens.BaseScreen

/**
 * Responsible for navigating between screens keeping the history and managing the stack
 */
interface ScreenNavigator {
  /**
   * Go to the given screen applying the given transitions. The screen will be added to the history
   */
  fun go(
      to: BaseScreen<*>,
      from: BaseScreen<*>? = null,
      enterTransition: Transition? = null,
      enterSharedTransition: Transition? = null,
      exitTransition: Transition? = null,
      exitSharedTransition: Transition? = null,
      sharedElement: View? = null,
      sharedElementTransactionName: String? = null)

  /**
   * Clear the history and add the given screen to the history
   */
  fun single(screen: BaseScreen<*>)

  /**
   * Remove the last screen from the history.
   *
   * @return False if there's no more screens to go back. True otherwise
   */
  fun back(): Boolean
}
