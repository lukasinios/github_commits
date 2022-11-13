package com.lg.data.api

import com.lg.domain.githublist.data.list.CommitResponse
import com.lg.domain.githublist.data.repository.RepositoryResponse
import com.lg.domain.githublist.repository.GithubApiOnlineRepository

class GithubApiOnlineRepositoryImpl constructor(private val githubApi: GithubApi) :
    GithubApiOnlineRepository {
    override suspend fun getRepository(ownerAndRepo: String): RepositoryResponse {
        val ownerAndRepoSplit = ownerAndRepo.split('/')
        return githubApi.getRepository(ownerAndRepoSplit.first(), ownerAndRepoSplit[1])
    }

    override suspend fun getCommits(ownerAndRepo: String, page: Int): List<CommitResponse> {
        val ownerAndRepoSplit = ownerAndRepo.split('/')
        return githubApi.getCommits(ownerAndRepoSplit.first(), ownerAndRepoSplit[1], page)
    }
}