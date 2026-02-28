package com.ukrainiantranslator.di

import android.content.Context
import androidx.room.Room
import com.ukrainiantranslator.BuildConfig
import com.ukrainiantranslator.data.local.TranslationDao
import com.ukrainiantranslator.data.local.TranslationDatabase
import com.ukrainiantranslator.data.remote.LibreTranslateApi
import com.ukrainiantranslator.util.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val logging = HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG)
                HttpLoggingInterceptor.Level.BODY
            else
                HttpLoggingInterceptor.Level.NONE
        }
        return OkHttpClient.Builder()
            .addInterceptor(logging)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.LIBRE_TRANSLATE_BASE_URL + "/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideLibreTranslateApi(retrofit: Retrofit): LibreTranslateApi {
        return retrofit.create(LibreTranslateApi::class.java)
    }

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): TranslationDatabase {
        return Room.databaseBuilder(
            context,
            TranslationDatabase::class.java,
            Constants.DATABASE_NAME
        ).build()
    }

    @Provides
    @Singleton
    fun provideTranslationDao(database: TranslationDatabase): TranslationDao {
        return database.translationDao()
    }
}
