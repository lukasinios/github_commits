package com.lg.data.api

import com.lg.domain.githublist.data.list.CommitResponse
import com.lg.domain.githublist.data.repository.RepositoryResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GithubApi {
    @GET("repos/{owner}/{repo}")
    suspend fun getRepository(
        @Path("owner") owner: String,
        @Path("repo")repo: String,
    ): RepositoryResponse

    @GET("repos/{owner}/{repo}/commits")
    suspend fun getCommits(
        @Path("owner") owner: String,
        @Path("repo")repo: String,
        @Query("page") page: Int
    ): List<CommitResponse>

}