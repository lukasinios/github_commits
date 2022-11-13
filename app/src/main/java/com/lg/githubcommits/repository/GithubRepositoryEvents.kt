package com.lg.githubcommits.repository

import androidx.annotation.StringRes
import com.lg.domain.githublist.view.RepositoryCommitsViewData
import com.lg.domain.history.view.CachedRepositoriesViewData

sealed class GithubRepositoryEvents {
    object InitialState: GithubRepositoryEvents()
    object ShowLoading: GithubRepositoryEvents()
    data class ShowCachedHistory(val cachedHistory: List<CachedRepositoriesViewData>): GithubRepositoryEvents()
    data class ShowFetchedRepositoryCommits(val repositoryCommitsViewData: RepositoryCommitsViewData, val clearAdapter: Boolean ): GithubRepositoryEvents()
    data class ShowToastError(@StringRes val message: Int): GithubRepositoryEvents()
    data class ShareSelectedCommits(val commitsToShare: String): GithubRepositoryEvents()
}