package com.lg.githubcommits.repository

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lg.domain.common.map
import com.lg.domain.githublist.usecase.CreateGithubListUseCase
import com.lg.domain.githublist.view.CommitsViewData
import com.lg.domain.githublist.view.RepositoryCommitsError
import com.lg.domain.history.GetHistoricSearchUseCase
import com.lg.githubcommits.R
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GithubRepositoryViewModel @Inject constructor(
    private val createGithubListUseCase: CreateGithubListUseCase,
    private val getHistoricSearchUseCase: GetHistoricSearchUseCase
) : ViewModel() {

    private val mutableState =
        MutableStateFlow<GithubRepositoryEvents>(GithubRepositoryEvents.InitialState)
    val state: StateFlow<GithubRepositoryEvents> = mutableState

    private var page: Int = 0
    private var ownerAndRepo: String? = null
    private var isDownloadingNextPage = false

    fun loadRepository(ownerAndRepo: String) {
        this.ownerAndRepo = ownerAndRepo
        this.page = 0
        loadNextPage()
    }

    fun loadNextPage() {
        if (isDownloadingNextPage) {
            return
        }

        isDownloadingNextPage = true
        page++

        createGithubListUseCase.fetchGithubCommits(ownerAndRepo.orEmpty(), page)
            .onStart { mutableState.emit(GithubRepositoryEvents.ShowLoading) }
            .onEach { either ->
                delay(100) //fixme delay to handle state in tests
                either.map(
                    onSuccess = { repositoryCommitsViewData ->
                        mutableState.emit(
                            GithubRepositoryEvents.ShowFetchedRepositoryCommits(
                                repositoryCommitsViewData,
                                shouldClearAdapter()
                            )
                        )
                    }, onFailure = { repositoryCommitsError ->
                        when (repositoryCommitsError) {
                            RepositoryCommitsError.LoadRepositoryFailure -> mutableState.emit(
                                GithubRepositoryEvents.ShowToastError(R.string.fetching_repository_error)
                            )
                            RepositoryCommitsError.LoadCommitsFailure -> mutableState.emit(
                                GithubRepositoryEvents.ShowToastError(R.string.fetching_commits_error)
                            )
                            RepositoryCommitsError.InvalidRepositoryAndOwnerFormat -> mutableState.emit(
                                GithubRepositoryEvents.ShowToastError(R.string.incorrect_owner_repo_format)
                            )
                        }
                    })
            }.onCompletion {
                isDownloadingNextPage = false
            }.catch { throwable ->
                throwable.printStackTrace()
                mutableState.emit(GithubRepositoryEvents.ShowToastError(R.string.fetching_repository_error))
            }.launchIn(viewModelScope)

    }

    private fun shouldClearAdapter() = page == 0

    fun loadHistory() {
        getHistoricSearchUseCase.getCachedRepositories()
            .onStart { mutableState.emit(GithubRepositoryEvents.ShowLoading) }
            .onEach { either ->
                delay(100) //fixme delay to handle state in tests
                either.map(
                    onSuccess = { cachedRepositoriesViewDataList ->
                        mutableState.emit(
                            GithubRepositoryEvents.ShowCachedHistory(
                                cachedRepositoriesViewDataList
                            )
                        )
                    },
                    onFailure = {
                        mutableState.emit(GithubRepositoryEvents.ShowToastError(R.string.loading_history_error))
                    }
                )
            }.catch { throwable ->
                throwable.printStackTrace()
                mutableState.emit(GithubRepositoryEvents.ShowToastError(R.string.loading_history_error))
            }.launchIn(viewModelScope)

    }

    fun shareSelectedCommits(commits: List<CommitsViewData>) {

        viewModelScope.launch {
            val filteredCommits = commits.filter { it.isSelected }
            if (filteredCommits.isEmpty()){
                mutableState.emit(GithubRepositoryEvents.ShowToastError(R.string.select_commit))
                return@launch
            }
            val dataToShare = filteredCommits
                .joinToString(separator = "") { "sha: ${it.sha}\nmessage: ${it.message}\nauthorName: ${it.authorName}\n\n" }
            mutableState.emit(GithubRepositoryEvents.ShareSelectedCommits(dataToShare))
        }
    }

}