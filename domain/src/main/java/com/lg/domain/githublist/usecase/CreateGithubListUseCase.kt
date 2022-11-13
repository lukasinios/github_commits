package com.lg.domain.githublist.usecase

import android.util.Log
import com.lg.domain.common.Either
import com.lg.domain.common.eitherFailure
import com.lg.domain.common.eitherSuccess
import com.lg.domain.githublist.data.list.CommitResponse
import com.lg.domain.githublist.data.repository.RepositoryResponse
import com.lg.domain.githublist.repository.GithubApiOnlineRepository
import com.lg.domain.githublist.repository.GithubCachedRepository
import com.lg.domain.githublist.view.CommitsViewData
import com.lg.domain.githublist.view.RepositoryCommitsError
import com.lg.domain.githublist.view.RepositoryCommitsViewData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.flow

class CreateGithubListUseCase constructor(
    private val internetStateUseCase: InternetStateUseCase,
    private val githubCachedRepository: GithubCachedRepository,
    private val githubApiOnlineRepository: GithubApiOnlineRepository,
    private val ownerAndRepositoryNameValidator: OwnerAndRepositoryNameValidator
) {

    fun fetchGithubCommits(
        ownerAndRepo: String,
        page: Int
    ): Flow<Either<RepositoryCommitsViewData, RepositoryCommitsError>> {
      return   flow {
            if (!ownerAndRepositoryNameValidator.isValid(ownerAndRepo)) {
                Log.d(CreateGithubListUseCase::class.java.simpleName, "Invalid input")
                return@flow emit(eitherFailure(RepositoryCommitsError.InvalidRepositoryAndOwnerFormat))
            }
            if (internetStateUseCase.isDeviceConnectedToNetwork()) {
                Log.d(CreateGithubListUseCase::class.java.simpleName, "Device with network")
                return@flow loadOnlineData(ownerAndRepo, page)
            } else {
                Log.d(CreateGithubListUseCase::class.java.simpleName, "Device without network")
                return@flow loadDataFromCache(ownerAndRepo, page)
            }
        }

    }

    private suspend fun FlowCollector<Either<RepositoryCommitsViewData, RepositoryCommitsError>>.loadOnlineData(
        ownerAndRepo: String,
        page: Int
    ) {
        val repo = try {
            githubApiOnlineRepository.getRepository(ownerAndRepo)
        } catch (exception: Exception) {
            return loadDataFromCache(ownerAndRepo, page)
        }

        val commits: List<CommitResponse> = try {
            githubApiOnlineRepository.getCommits(ownerAndRepo, page)
        } catch (exception: Exception) {
            return loadDataFromCache(ownerAndRepo, page)
        }
        githubCachedRepository.deleteAllRepositoriesForOwnerAndRepo(ownerAndRepo)
        githubCachedRepository.deleteAllCommitsForOwnerAndRepo(ownerAndRepo, page)
        githubCachedRepository.saveRepository(ownerAndRepo, repo)
        githubCachedRepository.saveCommits(ownerAndRepo, page, commits)
        return emit(eitherSuccess(mapResponseToViewData(repo, commits)))
    }

    private suspend fun FlowCollector<Either<RepositoryCommitsViewData, RepositoryCommitsError>>.loadDataFromCache(
        ownerAndRepo: String,
        page: Int
    ) {
        val repository: RepositoryResponse = try {
            githubCachedRepository.getRepository(ownerAndRepo)
        } catch (exception: Exception) {
            return emit(eitherFailure(RepositoryCommitsError.LoadRepositoryFailure))
        }

        val commits: List<CommitResponse> = try {
            githubCachedRepository.getCommits(ownerAndRepo, page)
        } catch (exception: Exception) {
            return emit(eitherFailure(RepositoryCommitsError.LoadCommitsFailure))
        }

        return emit(
            eitherSuccess(
                mapResponseToViewData(repository, commits)
            )
        )
    }

    private fun mapResponseToViewData(
        repository: RepositoryResponse,
        commits: List<CommitResponse>,
    ) = RepositoryCommitsViewData(
        repository.id,
        commits.map {
            CommitsViewData(
                it.commit.message,
                it.sha,
                it.commit.author.name
            )
        })
}