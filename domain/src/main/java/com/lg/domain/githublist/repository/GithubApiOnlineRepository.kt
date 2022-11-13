package com.lg.domain.githublist.repository

import com.lg.domain.githublist.data.list.CommitResponse
import com.lg.domain.githublist.data.repository.RepositoryResponse

interface GithubApiOnlineRepository {

    suspend fun getRepository(
        ownerAndRepo: String
    ): RepositoryResponse

    suspend fun getCommits(
        ownerAndRepo: String,
        page: Int
    ): List<CommitResponse>
}