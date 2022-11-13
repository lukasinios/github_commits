package com.lg.githubcommits.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.lg.data.database.GithubDao
import com.lg.data.database.entities.CommitEntity
import com.lg.data.database.entities.RepositoryEntity

@Database(
    entities = [
        CommitEntity::class,
        RepositoryEntity::class
    ],
    version = 1
)
abstract class GithubCommitsDatabase : RoomDatabase() {

    abstract fun provideGithubDao(): GithubDao
}