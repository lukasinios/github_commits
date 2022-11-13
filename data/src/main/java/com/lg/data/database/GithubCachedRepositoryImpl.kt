package com.lg.data.database

import com.lg.data.database.mapper.GithubCommitMapper
import com.lg.data.database.mapper.GithubRepositoryMapper
import com.lg.domain.githublist.data.list.CommitResponse
import com.lg.domain.githublist.data.repository.RepositoryResponse
import com.lg.domain.githublist.repository.GithubCachedRepository

class GithubCachedRepositoryImpl constructor(
    private val githubDao: GithubDao,
    private val githubRepositoryMapper: GithubRepositoryMapper,
    private val githubCommitMapper: GithubCommitMapper
) : GithubCachedRepository {

    override suspend fun getRepository(ownerAndRepo: String): RepositoryResponse =
        githubRepositoryMapper.mapRepositoryEntityToRepositoryResponse(
            githubDao.getRepository(ownerAndRepo)
        )

    override suspend fun saveRepository(
        ownerAndRepo: String,
        repositoryResponse: RepositoryResponse
    ) = githubDao.saveRepository(
        githubRepositoryMapper.mapRepositoryResponseToRepositoryEntity(
            ownerAndRepo,
            repositoryResponse
        )
    )

    override suspend fun getCommits(ownerAndRepo: String, page: Int): List<CommitResponse> =
        githubDao.getCommits(ownerAndRepo, page)
            .map { commitEntity -> githubCommitMapper.mapCommitEntityToCommitResponse(commitEntity) }

    override suspend fun saveCommits(
        ownerAndRepo: String,
        page: Int,
        commits: List<CommitResponse>
    ) {
        val mappedCommits = commits.map { commitResponse ->
            githubCommitMapper.mapCommitResponseToCommitEntity(
                ownerAndRepo,
                page,
                commitResponse
            )
        }
        githubDao.saveCommits(mappedCommits)
    }

    override suspend fun getCachedRepositories(): List<RepositoryResponse> =
        githubDao.getAllRepositories().map { repositoryEntity ->
            githubRepositoryMapper.mapRepositoryEntityToRepositoryResponse(repositoryEntity)
        }

    override suspend fun deleteAllRepositoriesForOwnerAndRepo(ownerAndRepo: String) =
        githubDao.deleteRepositories(ownerAndRepo)

    override suspend fun deleteAllCommitsForOwnerAndRepo(ownerAndRepo: String, page: Int,) =
        githubDao.deleteCommits(ownerAndRepo, page)


}