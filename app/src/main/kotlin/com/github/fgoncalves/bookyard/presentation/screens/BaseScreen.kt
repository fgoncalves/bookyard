package com.github.fgoncalves.bookyard.presentation.screens

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.github.fgoncalves.bookyard.MainActivity
import com.github.fgoncalves.bookyard.R
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

abstract class BaseScreen<in V : ViewDataBinding> : Fragment() {
    protected abstract val layout: Int

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val viewDataBinding = DataBindingUtil.inflate<V>(inflater, layout, container, false)

        applyBindings(viewDataBinding)

        return viewDataBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onAttach(context: Context?) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onResume() {
        super.onResume()
        val mainActivity: MainActivity = activity as MainActivity
        val drawer = mainActivity.drawer
        val supportsDrawer = supportsDrawer()

        if (supportsDrawer) {
            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED, Gravity.START)
        } else {
            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, Gravity.START)
        }

        toolbar()?.run {
            mainActivity.setSupportActionBar(this)
            mainActivity.supportActionBar?.title = toolbarTitle()

            if (supportsDrawer) {
                val toggle = ActionBarDrawerToggle(mainActivity, drawer, this, R.string.open_drawer,
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
        menuResource()?.let {
            inflater?.inflate(it, menu)
        }
    }

    open fun menuResource(): Int? = null

    open fun supportsHomeButton(): Boolean = false

    open fun supportsDrawer(): Boolean = false

    open fun toolbar(): Toolbar? = null

    open fun toolbarTitle(): String = ""

    open protected fun applyBindings(viewDataBinding: V) {

    }
}
