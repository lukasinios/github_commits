package com.lg.data.database.mapper

import com.lg.data.database.entities.CommitEntity
import com.lg.domain.githublist.data.list.Author
import com.lg.domain.githublist.data.list.Commit
import com.lg.domain.githublist.data.list.CommitResponse

class GithubCommitMapper {

    fun mapCommitResponseToCommitEntity(
        ownerAndRepositoru: String,
        page: Int,
        commitResponse: CommitResponse
    ): CommitEntity = CommitEntity(
        sha = commitResponse.sha,
        ownerAndRepository = ownerAndRepositoru,
        message = commitResponse.commit.message,
        authorName = commitResponse.commit.author.name,
        page = page
    )

    fun mapCommitEntityToCommitResponse(commitEntity: CommitEntity): CommitResponse =
        CommitResponse(
            sha = commitEntity.sha,
            commit = Commit(commitEntity.message, Author(commitEntity.authorName))
        )
}