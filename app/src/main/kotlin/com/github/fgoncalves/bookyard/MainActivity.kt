package com.github.fgoncalves.bookyard

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.AppCompatActivity
import com.github.fgoncalves.bookyard.presentation.screens.SplashScreen
import com.github.fgoncalves.bookyard.presentation.utils.ScreenNavigator
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject
import kotlin.properties.Delegates

class MainActivity : AppCompatActivity(), HasSupportFragmentInjector {
  val drawer: DrawerLayout by Delegates.notNull()

  @Inject
  lateinit var androidInjector: DispatchingAndroidInjector<Fragment>
  @Inject
  lateinit var screenNavigator: ScreenNavigator

  override fun onCreate(savedInstanceState: Bundle?) {
    AndroidInjection.inject(this)
    super.onCreate(savedInstanceState)

    setContentView(R.layout.main)

    screenNavigator.single(SplashScreen.newInstance())
  }

  override fun supportFragmentInjector(): AndroidInjector<Fragment> = androidInjector
}
