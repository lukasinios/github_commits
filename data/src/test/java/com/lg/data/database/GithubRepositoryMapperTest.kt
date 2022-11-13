package com.lg.data.database

import com.lg.data.database.entities.RepositoryEntity
import com.lg.data.database.mapper.GithubRepositoryMapper
import com.lg.domain.githublist.data.repository.RepositoryResponse
import org.junit.Assert.*
import org.junit.Test

class GithubRepositoryMapperTest{
    @Test
    fun `map entity to response should return valid object`(){
        val githubRepositoryDaoMapper = GithubRepositoryMapper()
        val repositoryEntity = RepositoryEntity(repositoryId = 1, ownerAndRepository =  "ownerAndRepository")
        val response =  githubRepositoryDaoMapper.mapRepositoryEntityToRepositoryResponse(repositoryEntity)

        assertTrue(repositoryEntity.repositoryId == response.id)
        assertTrue(repositoryEntity.ownerAndRepository == response.ownerAndRepository)
    }

    @Test
    fun `map response to entity should return valid object`(){
        val githubRepositoryDaoMapper = GithubRepositoryMapper()
        val repositoryResponse = RepositoryResponse( 1)
        val ownerAndRepository = "ownerAndRepository"
        val response =  githubRepositoryDaoMapper.mapRepositoryResponseToRepositoryEntity(ownerAndRepository, repositoryResponse)

        assertTrue(response.repositoryId == repositoryResponse.id)
        assertTrue(response.ownerAndRepository == ownerAndRepository)
    }
}