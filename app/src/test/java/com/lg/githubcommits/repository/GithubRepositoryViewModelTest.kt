package com.lg.githubcommits.repository

import app.cash.turbine.test
import com.lg.domain.common.eitherFailure
import com.lg.domain.common.eitherSuccess
import com.lg.domain.githublist.usecase.CreateGithubListUseCase
import com.lg.domain.githublist.view.RepositoryCommitsError
import com.lg.domain.githublist.view.RepositoryCommitsViewData
import com.lg.domain.history.GetHistoricSearchUseCase
import com.lg.domain.history.view.CachedRepositoriesViewData
import com.lg.domain.history.view.CachedRepositoryError
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class GithubRepositoryViewModelTest {


    @Before
    fun setup() {
        Dispatchers.setMain(Dispatchers.Unconfined)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private val createGithubListUseCase: CreateGithubListUseCase = mockk()
    private val getHistoricSearchUseCase: GetHistoricSearchUseCase = mockk()

    @Test
    fun ` success loading history states`() {
        runTest {
            coEvery { getHistoricSearchUseCase.getCachedRepositories() } returns flowOf(
                eitherSuccess(
                    listOf(
                        CachedRepositoriesViewData("owner", 1)
                    )
                )
            )

            val viewModel =
                GithubRepositoryViewModel(createGithubListUseCase, getHistoricSearchUseCase)


            viewModel.state.test {
                viewModel.loadHistory()
                Assert.assertTrue(awaitItem() is GithubRepositoryEvents.InitialState)
                Assert.assertTrue(awaitItem() is GithubRepositoryEvents.ShowLoading)
                Assert.assertTrue(awaitItem() is GithubRepositoryEvents.ShowCachedHistory)
                cancelAndIgnoreRemainingEvents()
            }
        }
    }

    @Test
    fun ` error loading history states`() {
        runTest {
            coEvery { getHistoricSearchUseCase.getCachedRepositories() } returns flowOf(
                eitherFailure(CachedRepositoryError.GenericError)
            )

            val viewModel =
                GithubRepositoryViewModel(createGithubListUseCase, getHistoricSearchUseCase)


            viewModel.state.test {
                viewModel.loadHistory()
                Assert.assertTrue(awaitItem() is GithubRepositoryEvents.InitialState)
                Assert.assertTrue(awaitItem() is GithubRepositoryEvents.ShowLoading)
                Assert.assertTrue(awaitItem() is GithubRepositoryEvents.ShowToastError)
                cancelAndIgnoreRemainingEvents()
            }
        }
    }


    @Test
    fun ` commits loading success should return correct states states`() {
        runTest {
            coEvery { createGithubListUseCase.fetchGithubCommits(any(), any()) } returns flowOf(
                eitherSuccess(RepositoryCommitsViewData(1, listOf()))
            )

            val viewModel =
                GithubRepositoryViewModel(createGithubListUseCase, getHistoricSearchUseCase)


            viewModel.state.test {
                viewModel.loadRepository("")
                Assert.assertTrue(awaitItem() is GithubRepositoryEvents.InitialState)
                Assert.assertTrue(awaitItem() is GithubRepositoryEvents.ShowLoading)
                Assert.assertTrue(awaitItem() is GithubRepositoryEvents.ShowFetchedRepositoryCommits)
                cancelAndIgnoreRemainingEvents()
            }
        }
    }

    @Test
    fun `input validation error in loading commits check states`() {
        runTest {
            coEvery { createGithubListUseCase.fetchGithubCommits(any(), any()) } returns flowOf(
                eitherFailure(RepositoryCommitsError.InvalidRepositoryAndOwnerFormat)
            )

            val viewModel =
                GithubRepositoryViewModel(createGithubListUseCase, getHistoricSearchUseCase)


            viewModel.state.test {
                viewModel.loadRepository("")
                Assert.assertTrue(awaitItem() is GithubRepositoryEvents.InitialState)
                Assert.assertTrue(awaitItem() is GithubRepositoryEvents.ShowLoading)
                Assert.assertTrue(awaitItem() is GithubRepositoryEvents.ShowToastError)
                cancelAndIgnoreRemainingEvents()
            }
        }
    }

    @Test
    fun `LoadRepositoryFailure in loading commits check states`() {
        runTest {
            coEvery { createGithubListUseCase.fetchGithubCommits(any(), any()) } returns flowOf(
                eitherFailure(RepositoryCommitsError.LoadRepositoryFailure)
            )

            val viewModel =
                GithubRepositoryViewModel(createGithubListUseCase, getHistoricSearchUseCase)


            viewModel.state.test {
                viewModel.loadRepository("")
                Assert.assertTrue(awaitItem() is GithubRepositoryEvents.InitialState)
                Assert.assertTrue(awaitItem() is GithubRepositoryEvents.ShowLoading)
                Assert.assertTrue(awaitItem() is GithubRepositoryEvents.ShowToastError)
                cancelAndIgnoreRemainingEvents()
            }
        }
    }

    @Test
    fun `LoadCommitsFailure in loading commits check states`() {
        runTest {
            coEvery { createGithubListUseCase.fetchGithubCommits(any(), any()) } returns flowOf(
                eitherFailure(RepositoryCommitsError.LoadCommitsFailure)
            )

            val viewModel =
                GithubRepositoryViewModel(createGithubListUseCase, getHistoricSearchUseCase)


            viewModel.state.test {
                viewModel.loadRepository("")
                Assert.assertTrue(awaitItem() is GithubRepositoryEvents.InitialState)
                Assert.assertTrue(awaitItem() is GithubRepositoryEvents.ShowLoading)
                Assert.assertTrue(awaitItem() is GithubRepositoryEvents.ShowToastError)
                cancelAndIgnoreRemainingEvents()
            }
        }
    }

    @Test
    fun `next page commits loading success should return correct states states`() {
        runTest {
            coEvery { createGithubListUseCase.fetchGithubCommits(any(), any()) } returns flowOf(
                eitherSuccess(RepositoryCommitsViewData(1, listOf()))
            )

            val viewModel =
                GithubRepositoryViewModel(createGithubListUseCase, getHistoricSearchUseCase)


            viewModel.state.test {
                viewModel.loadNextPage()
                Assert.assertTrue(awaitItem() is GithubRepositoryEvents.InitialState)
                Assert.assertTrue(awaitItem() is GithubRepositoryEvents.ShowLoading)
                Assert.assertTrue(awaitItem() is GithubRepositoryEvents.ShowFetchedRepositoryCommits)
                cancelAndIgnoreRemainingEvents()
            }
        }
    }

    @Test
    fun `input validation error in loading next commit page check states`() {
        runTest {
            coEvery { createGithubListUseCase.fetchGithubCommits(any(), any()) } returns flowOf(
                eitherFailure(RepositoryCommitsError.InvalidRepositoryAndOwnerFormat)
            )

            val viewModel =
                GithubRepositoryViewModel(createGithubListUseCase, getHistoricSearchUseCase)


            viewModel.state.test {
                viewModel.loadNextPage()
                Assert.assertTrue(awaitItem() is GithubRepositoryEvents.InitialState)
                Assert.assertTrue(awaitItem() is GithubRepositoryEvents.ShowLoading)
                Assert.assertTrue(awaitItem() is GithubRepositoryEvents.ShowToastError)
                cancelAndIgnoreRemainingEvents()
            }
        }
    }

    @Test
    fun `LoadRepositoryFailure in loading next commit page check states`() {
        runTest {
            coEvery { createGithubListUseCase.fetchGithubCommits(any(), any()) } returns flowOf(
                eitherFailure(RepositoryCommitsError.LoadRepositoryFailure)
            )

            val viewModel =
                GithubRepositoryViewModel(createGithubListUseCase, getHistoricSearchUseCase)


            viewModel.state.test {
                viewModel.loadNextPage()
                Assert.assertTrue(awaitItem() is GithubRepositoryEvents.InitialState)
                Assert.assertTrue(awaitItem() is GithubRepositoryEvents.ShowLoading)
                Assert.assertTrue(awaitItem() is GithubRepositoryEvents.ShowToastError)
                cancelAndIgnoreRemainingEvents()
            }
        }
    }

    @Test
    fun `LoadCommitsFailure in loading next commit page check states`() {
        runTest {
            coEvery { createGithubListUseCase.fetchGithubCommits(any(), any()) } returns flowOf(
                eitherFailure(RepositoryCommitsError.LoadCommitsFailure)
            )

            val viewModel =
                GithubRepositoryViewModel(createGithubListUseCase, getHistoricSearchUseCase)


            viewModel.state.test {
                viewModel.loadNextPage()
                Assert.assertTrue(awaitItem() is GithubRepositoryEvents.InitialState)
                Assert.assertTrue(awaitItem() is GithubRepositoryEvents.ShowLoading)
                Assert.assertTrue(awaitItem() is GithubRepositoryEvents.ShowToastError)
                cancelAndIgnoreRemainingEvents()
            }
        }
    }

}