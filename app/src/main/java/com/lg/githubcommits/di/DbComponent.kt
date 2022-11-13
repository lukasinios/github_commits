package com.lg.githubcommits.di

import android.content.Context
import androidx.room.Room
import com.lg.data.database.GithubDao
import com.lg.githubcommits.db.GithubCommitsDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DbComponent {

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): GithubCommitsDatabase =
        Room.databaseBuilder(context, GithubCommitsDatabase::class.java, DATABASE_NAME).build()

    @Singleton
    @Provides
    fun provideGithubDao(githubCommitsDatabase: GithubCommitsDatabase): GithubDao =
        githubCommitsDatabase.provideGithubDao()

    companion object {
        private const val DATABASE_NAME = "GithubCommitsDatabaseName"
    }
}