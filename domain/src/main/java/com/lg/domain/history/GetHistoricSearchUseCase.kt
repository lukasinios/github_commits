package com.lg.domain.history

import com.lg.domain.common.Either
import com.lg.domain.common.eitherFailure
import com.lg.domain.common.eitherSuccess
import com.lg.domain.githublist.repository.GithubCachedRepository
import com.lg.domain.history.view.CachedRepositoriesViewData
import com.lg.domain.history.view.CachedRepositoryError
import kotlinx.coroutines.flow.flow

class GetHistoricSearchUseCase constructor(
    private val githubCachedRepository: GithubCachedRepository,
    private val cachedRepositoryMapper: CachedRepositoryMapper
) {

    fun getCachedRepositories() =
        flow<Either<List<CachedRepositoriesViewData>, CachedRepositoryError>> {
            try {
                return@flow emit(
                    eitherSuccess(
                        cachedRepositoryMapper.mapCachedRepositoryToView(
                            githubCachedRepository.getCachedRepositories()
                        )
                    )
                )
            } catch (exception: Exception) {
                return@flow emit(eitherFailure(CachedRepositoryError.GenericError))
            }
        }
}