package com.lg.data.database

import com.lg.data.database.entities.CommitEntity
import com.lg.data.database.mapper.GithubCommitMapper
import com.lg.domain.githublist.data.list.Author
import com.lg.domain.githublist.data.list.Commit
import com.lg.domain.githublist.data.list.CommitResponse
import org.junit.Assert.assertTrue
import org.junit.Test

class GithubCommitMapperTest {

    @Test
    fun `map entity to response should return valid object`() {
        val githubCommitMapper = GithubCommitMapper()
        val commitEntity = CommitEntity("sha", "ownerAndRepo", "message", "author")
        val response = githubCommitMapper.mapCommitEntityToCommitResponse(commitEntity)

        assertTrue(commitEntity.message == response.commit.message)
        assertTrue(commitEntity.sha == response.sha)
        assertTrue(commitEntity.authorName == response.commit.author.name)
    }

    @Test
    fun `map response to entity should return valid object`() {
        val githubCommitMapper = GithubCommitMapper()
        val commitEntity = CommitResponse("sha", Commit("message", Author("author")))
        val ownerAndRepo = "ownerAndRepo"
        val page = 2
        val response =
            githubCommitMapper.mapCommitResponseToCommitEntity(ownerAndRepo,page, commitEntity)

        assertTrue(ownerAndRepo == response.ownerAndRepository)
        assertTrue(commitEntity.sha == response.sha)
        assertTrue(commitEntity.commit.message == response.message)
        assertTrue(commitEntity.commit.author.name == response.authorName)
        assertTrue(page == response.page)
    }
}