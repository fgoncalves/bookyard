package com.github.fgoncalves.bookyard.presentation.utils

import kotlin.annotation.AnnotationRetention.RUNTIME
import kotlin.annotation.AnnotationTarget.TYPE

const val NO_LAYOUT = 0

@MustBeDocumented
@Target(TYPE)
@Retention(RUNTIME)
annotation class LayoutResource(val value: Int = NO_LAYOUT)
