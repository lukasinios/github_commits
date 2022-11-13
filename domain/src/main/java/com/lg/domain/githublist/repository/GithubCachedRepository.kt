package com.lg.domain.githublist.repository

import com.lg.domain.githublist.data.list.CommitResponse
import com.lg.domain.githublist.data.repository.RepositoryResponse

interface GithubCachedRepository {
    suspend fun getRepository(
        ownerAndRepo: String
    ): RepositoryResponse

    suspend fun saveRepository(ownerAndRepo: String, repositoryResponse: RepositoryResponse)

    suspend fun getCommits(
        ownerAndRepo: String,
        page: Int
    ): List<CommitResponse>

    suspend fun saveCommits(ownerAndRepo: String, page: Int, commits: List<CommitResponse>)

    suspend fun getCachedRepositories(): List<RepositoryResponse>

    suspend fun deleteAllRepositoriesForOwnerAndRepo(ownerAndRepo: String)

    suspend fun deleteAllCommitsForOwnerAndRepo(ownerAndRepo: String, page: Int)

}