package com.lg.domain.githublist.view

sealed class RepositoryCommitsError {
    object LoadRepositoryFailure: RepositoryCommitsError()
    object LoadCommitsFailure: RepositoryCommitsError()
    object InvalidRepositoryAndOwnerFormat: RepositoryCommitsError()
}