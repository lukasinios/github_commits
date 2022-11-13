package com.lg.data.database

import androidx.room.*
import com.lg.data.database.entities.CommitEntity
import com.lg.data.database.entities.RepositoryEntity

@Dao
interface GithubDao {

    @Query("SELECT * FROM RepositoryEntity WHERE RepositoryEntity.ownerAndRepository = :ownerAndRepo LIMIT 1")
    suspend fun getRepository(ownerAndRepo: String): RepositoryEntity

    @Query("SELECT * FROM RepositoryEntity")
    suspend fun getAllRepositories(): List<RepositoryEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveRepository(repositoryResponse: RepositoryEntity)

    @Query("DELETE FROM RepositoryEntity WHERE RepositoryEntity.ownerAndRepository = :ownerAndRepository")
    suspend fun deleteRepositories(ownerAndRepository: String)

    @Query("SELECT * FROM CommitEntity WHERE CommitEntity.ownerAndRepository = :ownerAndRepo AND CommitEntity.page = :page")
    suspend fun getCommits(ownerAndRepo: String, page: Int): List<CommitEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveCommits(commits: List<CommitEntity>)

    @Query("DELETE FROM CommitEntity WHERE CommitEntity.ownerAndRepository = :ownerAndRepository AND CommitEntity.page = :page")
    suspend fun deleteCommits(ownerAndRepository: String, page: Int)
}