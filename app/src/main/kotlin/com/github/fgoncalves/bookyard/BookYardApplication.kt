package com.github.fgoncalves.bookyard

import android.app.Activity
import android.app.Application
import com.github.fgoncalves.bookyard.di.ApplicationComponent
import com.github.fgoncalves.bookyard.di.DaggerApplicationComponent
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import timber.log.Timber
import javax.inject.Inject

class BookYardApplication : Application(), HasActivityInjector {
    var applicationComponent: ApplicationComponent? = null
        get() {
            if (field == null) {
                field = DaggerApplicationComponent.builder()
                        .application(this)
                        .screenContainerId(R.id.main_container)
                        .build()
            }
            return field
        }

    @Inject
    lateinit var androidInjector: DispatchingAndroidInjector<Activity>

    override fun onCreate() {
        applicationComponent?.inject(this)
        super.onCreate()

        if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())
    }

    override fun activityInjector(): AndroidInjector<Activity> = androidInjector
}
