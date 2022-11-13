package com.lg.domain.history

import com.lg.domain.githublist.data.repository.RepositoryResponse
import com.lg.domain.history.view.CachedRepositoriesViewData

class CachedRepositoryMapper {

    fun mapCachedRepositoryToView(cachedRepositories: List<RepositoryResponse>): List<CachedRepositoriesViewData> =
        cachedRepositories
            .map { repositoryResponse ->
                CachedRepositoriesViewData(
                    repositoryResponse.ownerAndRepository.orEmpty(),
                    repositoryResponse.id
                )
            }.filter { it.ownerAndRepository.isNotBlank() }
}