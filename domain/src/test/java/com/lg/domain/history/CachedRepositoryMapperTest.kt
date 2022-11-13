package com.lg.domain.history

import com.lg.domain.githublist.data.repository.RepositoryResponse
import org.junit.Assert.*
import org.junit.Test

class CachedRepositoryMapperTest{

    @Test
    fun `map cached without owners to proper view list`(){
        val cachedData = listOf(RepositoryResponse(1), RepositoryResponse(2), RepositoryResponse(3).apply { this.ownerAndRepository = "owner" })
        val cachedRepositoryMapper = CachedRepositoryMapper()
        assertTrue(cachedRepositoryMapper.mapCachedRepositoryToView(cachedData).size == 1)
        assertTrue(cachedRepositoryMapper.mapCachedRepositoryToView(cachedData).first().repositoryId == 3)
        assertTrue(cachedRepositoryMapper.mapCachedRepositoryToView(cachedData).first().ownerAndRepository == "owner")
    }

    @Test
    fun `map cached to proper view object`(){
        val cachedData = listOf(RepositoryResponse(1).apply { this.ownerAndRepository = "owner1" }, RepositoryResponse(2).apply { this.ownerAndRepository = "owner2" }, RepositoryResponse(3).apply { this.ownerAndRepository = "owner3" })
        val cachedRepositoryMapper = CachedRepositoryMapper()
        assertTrue(cachedRepositoryMapper.mapCachedRepositoryToView(cachedData).size == 3)
        assertTrue(cachedRepositoryMapper.mapCachedRepositoryToView(cachedData).first().repositoryId == 1)
        assertTrue(cachedRepositoryMapper.mapCachedRepositoryToView(cachedData).first().ownerAndRepository == "owner1")
        assertTrue(cachedRepositoryMapper.mapCachedRepositoryToView(cachedData)[1].repositoryId == 2)
        assertTrue(cachedRepositoryMapper.mapCachedRepositoryToView(cachedData)[1].ownerAndRepository == "owner2")
        assertTrue(cachedRepositoryMapper.mapCachedRepositoryToView(cachedData)[2].repositoryId == 3)
        assertTrue(cachedRepositoryMapper.mapCachedRepositoryToView(cachedData)[2].ownerAndRepository == "owner3")
    }
}