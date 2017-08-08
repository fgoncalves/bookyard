package com.github.fgoncalves.bookyard.presentation.utils

import kotlin.annotation.AnnotationRetention.RUNTIME
import kotlin.annotation.AnnotationTarget.CLASS

const val NO_LAYOUT = 0

@MustBeDocumented
@Target(CLASS)
@Retention(RUNTIME)
annotation class LayoutResource(val value: Int = NO_LAYOUT)
