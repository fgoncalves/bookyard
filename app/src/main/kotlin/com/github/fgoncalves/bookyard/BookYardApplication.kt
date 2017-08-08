package com.github.fgoncalves.bookyard

import android.app.Activity
import android.app.Application
import com.github.fgoncalves.bookyard.di.ApplicationComponent
import com.github.fgoncalves.bookyard.di.DaggerApplicationComponent
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import javax.inject.Inject

class BookYardApplication : Application(), HasActivityInjector {
  private var _applicationComponent: ApplicationComponent? = null
  var applicationComponent: ApplicationComponent?
    get() {
      if (_applicationComponent == null) {
        _applicationComponent = DaggerApplicationComponent.builder()
            .application(this)
            .screenContainerId(R.id.main_container)
            .build()
      }
      return _applicationComponent
    }
    set(value) {
      _applicationComponent = value
    }

  @Inject
  lateinit var androidInjector: DispatchingAndroidInjector<Activity>

  override fun onCreate() {
    applicationComponent?.inject(this)
    super.onCreate()
  }

  override fun activityInjector(): AndroidInjector<Activity> = androidInjector
}
