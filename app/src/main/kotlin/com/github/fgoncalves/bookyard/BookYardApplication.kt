package com.github.fgoncalves.bookyard

import android.app.Application
import com.github.fgoncalves.bookyard.data.di.ApiClientsModule
import com.github.fgoncalves.bookyard.di.ApplicationComponent
import com.github.fgoncalves.bookyard.di.ApplicationModule
import com.github.fgoncalves.bookyard.di.DaggerApplicationComponent

class BookYardApplication : Application() {
  private var _applicationComponent: ApplicationComponent? = null
  var applicationComponent: ApplicationComponent?
    get() {
      if (_applicationComponent == null) {
        _applicationComponent = DaggerApplicationComponent.builder()
            .apiClientsModule(ApiClientsModule)
            .applicationModule(ApplicationModule(this))
            .build()
      }
      return _applicationComponent
    }
    set(value) {
      _applicationComponent = value
    }
}
