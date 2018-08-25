package com.github.fgoncalves.bookyard.di

import android.content.Context
import com.github.fgoncalves.bookyard.BookYardApplication
import com.github.fgoncalves.bookyard.R
import com.github.fgoncalves.bookyard.data.di.ApiClientsModule
import com.github.fgoncalves.bookyard.di.qualifiers.ApplicationContext
import com.github.fgoncalves.bookyard.domain.di.UseCaseModule
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(includes = [
    ApiClientsModule::class,
    ServicesModule::class,
    FirebaseModule::class,
    UseCaseModule::class])
object ApplicationModule {
    @JvmStatic
    @Provides
    @ApplicationContext
    @Singleton
    fun providesApplicationContext(application: BookYardApplication): Context = application

    @JvmStatic
    @Provides
    @Singleton
    fun providesGoogleSignInOptions(
            @ApplicationContext context: Context): GoogleSignInOptions = GoogleSignInOptions.Builder(
            GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(context.getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
}
