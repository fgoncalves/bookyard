package com.github.fgoncalves.bookyard.data.di

import android.content.Context
import com.github.fgoncalves.bookyard.BuildConfig
import com.github.fgoncalves.bookyard.config.ENDPOINT
import com.github.fgoncalves.bookyard.data.BooksApiClient
import com.github.fgoncalves.bookyard.data.di.qualifiers.LoggingInterceptor
import com.github.fgoncalves.bookyard.di.qualifiers.ApplicationContext
import dagger.Module
import dagger.Provides
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level.BODY
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
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
    fun providesOkHttpClient(
            @LoggingInterceptor loggingInterceptor: Interceptor,
            @ApplicationContext context: Context): OkHttpClient {
        val cacheSize: Long = 10 * 1024 * 1024
        val cache = Cache(File(context.cacheDir, ".cache"), cacheSize)

        val builder = OkHttpClient.Builder()
                .cache(cache)
        if (BuildConfig.DEBUG)
            builder.addInterceptor(loggingInterceptor)
        return builder.build()
    }

    @JvmStatic
    @Provides
    @Singleton
    fun providesRetrofit(okHttp: OkHttpClient): Retrofit =
            Retrofit.Builder()
                    .baseUrl(ENDPOINT)
                    .client(okHttp)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build()

    @JvmStatic
    @Provides
    @Singleton
    fun providesBooksApiClient(retrofit: Retrofit): BooksApiClient = retrofit.create(
            BooksApiClient::class.java)

}
