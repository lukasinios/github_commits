package com.lg.githubcommits.di

import android.content.Context
import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.lg.data.api.GithubApi
import com.lg.githubcommits.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.Reusable
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
class NetworkComponent {

    @Singleton
    @Provides
    fun provideOkHttpBuilder(
        @ApplicationContext context: Context
    ): OkHttpClient {
        return OkHttpClient.Builder().apply {
            if (BuildConfig.DEBUG) {
                this.addInterceptor(HttpLoggingInterceptor().apply {
                    this.level = HttpLoggingInterceptor.Level.BODY
                })
            }
        }.build()
    }


    @Provides
    @Reusable
    internal fun provideGson(): Gson = GsonBuilder()
        .serializeNulls()
        .setFieldNamingPolicy(FieldNamingPolicy.IDENTITY)
        .create()

    @Provides
    @Reusable
    internal fun provideRetrofitInterface(
        gson: Gson,
        okHttpClient: OkHttpClient
    ): Retrofit {
        return Retrofit.Builder()
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .baseUrl(BuildConfig.BASE_URL)
            .build()
    }

    @Provides
    @Reusable
    fun provideGithubApi(
        @ApplicationContext context: Context,
        retrofit: Retrofit
    ): GithubApi = retrofit.create(GithubApi::class.java)

}