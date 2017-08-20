package com.github.fgoncalves.bookyard.di

import android.content.Context
import com.github.fgoncalves.bookyard.BookYardApplication
import com.github.fgoncalves.bookyard.R
import com.github.fgoncalves.bookyard.data.di.ApiClientsModule
import com.github.fgoncalves.bookyard.di.qualifiers.ApplicationContext
import com.github.fgoncalves.bookyard.di.qualifiers.NetworkSchedulerTransformer
import com.github.fgoncalves.bookyard.domain.di.UseCaseModule
import com.github.fgoncalves.rx_schedulers.SchedulerTransformer
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import dagger.Module
import dagger.Provides
import io.reactivex.CompletableTransformer
import io.reactivex.MaybeTransformer
import io.reactivex.ObservableTransformer
import io.reactivex.SingleTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Singleton

@Module(includes = arrayOf(
    ApiClientsModule::class,
    ServicesModule::class,
    FirebaseModule::class,
    UseCaseModule::class))
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

  @JvmStatic
  @Provides
  @Singleton
  @NetworkSchedulerTransformer
  fun providesNetworkSchedulerTransformer(): SchedulerTransformer
      = object : SchedulerTransformer {
    private val observerOn = AndroidSchedulers.mainThread()
    private val subscribeOn = Schedulers.io()

    override fun applyCompletableTransformer(): CompletableTransformer
        = CompletableTransformer { it.subscribeOn(subscribeOn).observeOn(observerOn) }

    override fun <T> applyMaybeTransformer(): MaybeTransformer<T, T>
        = MaybeTransformer { it.subscribeOn(subscribeOn).observeOn(observerOn) }

    override fun <T> applyObservableSchedulers(): ObservableTransformer<T, T>
        = ObservableTransformer { it.subscribeOn(subscribeOn).observeOn(observerOn) }

    override fun <T> applySingleSchedulers(): SingleTransformer<T, T>
        = SingleTransformer { it.subscribeOn(subscribeOn).observeOn(observerOn) }
  }
}
