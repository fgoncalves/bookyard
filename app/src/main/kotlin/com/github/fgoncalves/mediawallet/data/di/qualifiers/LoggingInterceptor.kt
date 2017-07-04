package com.github.fgoncalves.mediawallet.data.di.qualifiers

import javax.inject.Qualifier
import kotlin.annotation.AnnotationRetention.RUNTIME

/**
 * Qualifier used to qualify the logging interceptor injection for the okhttp client
 */
@Qualifier
@MustBeDocumented
@Retention(RUNTIME)
annotation class LoggingInterceptor
