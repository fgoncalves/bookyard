package com.github.fgoncalves.bookyard.di.keys

import android.arch.lifecycle.ViewModel
import dagger.MapKey
import kotlin.annotation.AnnotationRetention.RUNTIME
import kotlin.annotation.AnnotationTarget.FUNCTION
import kotlin.reflect.KClass

@MapKey
@MustBeDocumented
@Target(FUNCTION)
@Retention(RUNTIME)
annotation class ViewModelKey(val value: KClass<out ViewModel>)
