package com.lg.data.database.mapper

import com.lg.data.database.entities.RepositoryEntity
import com.lg.domain.githublist.data.repository.RepositoryResponse

class GithubRepositoryMapper {

    fun mapRepositoryResponseToRepositoryEntity(
        ownerAndRepository: String,
        repositoryResponse: RepositoryResponse
    ): RepositoryEntity = RepositoryEntity(
        repositoryId = repositoryResponse.id,
        ownerAndRepository = ownerAndRepository
    )

    fun mapRepositoryEntityToRepositoryResponse(repositoryEntity: RepositoryEntity): RepositoryResponse =
        RepositoryResponse(repositoryEntity.repositoryId).apply {
            this.ownerAndRepository = repositoryEntity.ownerAndRepository
        }
}