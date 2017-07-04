package com.github.fgoncalves.mediawallet.data.di

import com.github.fgoncalves.mediawallet.BuildConfig
import com.github.fgoncalves.mediawallet.config.ENDPOINT
import com.github.fgoncalves.mediawallet.data.BooksApiClient
import com.github.fgoncalves.mediawallet.data.di.qualifiers.LoggingInterceptor
import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level.BODY
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
object ApiClientsModule {

    @JvmStatic
    @Provides
    @Singleton
    @LoggingInterceptor
    fun providesLoggingInterceptor(): Interceptor {
      val interceptor = HttpLoggingInterceptor()
      interceptor.level = BODY
      return interceptor
    }

    @JvmStatic
    @Provides
    @Singleton
    fun providesOkHttpClient(@LoggingInterceptor loggingInterceptor: Interceptor): OkHttpClient {
      val builder = OkHttpClient.Builder()
      if (BuildConfig.DEBUG)
        builder.addInterceptor(loggingInterceptor)
      return builder.build()
    }

    @JvmStatic
    @Provides
    @Singleton
    fun providesRetrofit(okHttp: OkHttpClient): Retrofit {
      return Retrofit.Builder()
          .baseUrl(ENDPOINT)
          .client(okHttp)
          .addConverterFactory(GsonConverterFactory.create())
          .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
          .build()
    }

    @JvmStatic
    @Provides
    @Singleton
    fun providesBooksApiClient(retrofit: Retrofit): BooksApiClient = retrofit.create(
        BooksApiClient::class.java)

}
