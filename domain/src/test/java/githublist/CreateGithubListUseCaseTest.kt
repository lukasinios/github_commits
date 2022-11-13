package githublist

import com.lg.domain.common.eitherFailure
import com.lg.domain.common.eitherSuccess
import com.lg.domain.githublist.data.list.Author
import com.lg.domain.githublist.data.list.Commit
import com.lg.domain.githublist.data.list.CommitResponse
import com.lg.domain.githublist.data.repository.RepositoryResponse
import com.lg.domain.githublist.repository.GithubApiOnlineRepository
import com.lg.domain.githublist.repository.GithubCachedRepository
import com.lg.domain.githublist.usecase.CreateGithubListUseCase
import com.lg.domain.githublist.usecase.InternetStateUseCase
import com.lg.domain.githublist.usecase.OwnerAndRepositoryNameValidator
import com.lg.domain.githublist.view.CommitsViewData
import com.lg.domain.githublist.view.RepositoryCommitsError
import com.lg.domain.githublist.view.RepositoryCommitsViewData
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.*
import java.net.ConnectException

@OptIn(ExperimentalCoroutinesApi::class)
class CreateGithubListUseCaseTest {


    private val internetStateUseCase: InternetStateUseCase = mockk<InternetStateUseCase>()
    private val githubCachedRepository: GithubCachedRepository = mockk<GithubCachedRepository>()
    private val githubApiOnlineRepository: GithubApiOnlineRepository =
        mockk<GithubApiOnlineRepository>()
    private val ownerAndRepositoryNameValidator: OwnerAndRepositoryNameValidator =
        mockk<OwnerAndRepositoryNameValidator>()


    @Test
    fun `empty name input should return error`() = runTest {
        every { ownerAndRepositoryNameValidator.isValid(any()) } returns false
        val createGithubListUseCase = CreateGithubListUseCase(
            internetStateUseCase,
            githubCachedRepository,
            githubApiOnlineRepository,
            ownerAndRepositoryNameValidator
        )

        Assert.assertEquals(
            eitherFailure(RepositoryCommitsError.InvalidRepositoryAndOwnerFormat),
            createGithubListUseCase.fetchGithubCommits("", 0).first()
        )
    }

    @Test
    fun `invalid name input should return error`() = runTest {
        every { ownerAndRepositoryNameValidator.isValid(any()) } returns false
        val createGithubListUseCase = CreateGithubListUseCase(
            internetStateUseCase,
            githubCachedRepository,
            githubApiOnlineRepository,
            ownerAndRepositoryNameValidator
        )

        Assert.assertEquals(
            eitherFailure(RepositoryCommitsError.InvalidRepositoryAndOwnerFormat),
            createGithubListUseCase.fetchGithubCommits("a/@", 0).first()
        )
    }

    @Test
    fun `correct name input and internet turn off should get data from cache`() = runTest {
        every { ownerAndRepositoryNameValidator.isValid(any()) } returns true
        every { internetStateUseCase.isDeviceConnectedToNetwork() } returns false
        val repositoryId = 1
        coEvery { githubCachedRepository.getRepository(any()) } returns RepositoryResponse(
            repositoryId
        )
        val sha = "sha"
        val message = "message"
        val name = "authorName"
        coEvery { githubCachedRepository.getCommits(any(), any()) } returns listOf(
            CommitResponse(
                sha, Commit(
                    message, Author(name)
                )
            )
        )
        val createGithubListUseCase = CreateGithubListUseCase(
            internetStateUseCase,
            githubCachedRepository,
            githubApiOnlineRepository,
            ownerAndRepositoryNameValidator
        )

        Assert.assertEquals(
            eitherSuccess(
                RepositoryCommitsViewData(
                    repositoryId, listOf(
                        CommitsViewData(message, sha, name)
                    )
                )
            ), createGithubListUseCase.fetchGithubCommits("a/a", 0).first()
        )
    }

    @Test
    fun `correct name input and internet turn on should get data api and save to cache`() =
        runTest {
            every { ownerAndRepositoryNameValidator.isValid(any()) } returns true
            every { internetStateUseCase.isDeviceConnectedToNetwork() } returns true
            val repositoryId = 1
            coEvery { githubApiOnlineRepository.getRepository(any()) } returns RepositoryResponse(
                repositoryId
            )
            val sha = "sha"
            val message = "message"
            val name = "authorName"
            coEvery { githubApiOnlineRepository.getCommits(any(), any()) } returns listOf(
                CommitResponse(
                    sha, Commit(
                        message, Author(name)
                    )
                )
            )
            coJustRun { githubCachedRepository.saveRepository(any(), any()) }
            coJustRun { githubCachedRepository.saveCommits(any(), any(), any()) }
            coJustRun { githubCachedRepository.deleteAllRepositoriesForOwnerAndRepo(any()) }
            coJustRun { githubCachedRepository.deleteAllCommitsForOwnerAndRepo(any(), any()) }

            val createGithubListUseCase = CreateGithubListUseCase(
                internetStateUseCase,
                githubCachedRepository,
                githubApiOnlineRepository,
                ownerAndRepositoryNameValidator
            )

            Assert.assertEquals(
                eitherSuccess(
                    RepositoryCommitsViewData(
                        repositoryId, listOf(
                            CommitsViewData(message, sha, name)
                        )
                    )
                ), createGithubListUseCase.fetchGithubCommits("a/a", 0).first()
            )
        }

    @Test
    fun `correct name input, internet turn on, error on downloading repo should try get data from cache and emit response`() =
        runTest {
            every { ownerAndRepositoryNameValidator.isValid(any()) } returns true
            every { internetStateUseCase.isDeviceConnectedToNetwork() } returns true
            val repositoryId = 1
            coEvery { githubApiOnlineRepository.getRepository(any()) } throws ConnectException()
            coEvery { githubCachedRepository.getRepository(any()) } returns RepositoryResponse(
                repositoryId
            )
            val sha = "sha"
            val message = "message"
            val name = "authorName"
            coEvery { githubCachedRepository.getCommits(any(), any()) } returns listOf(
                CommitResponse(
                    sha, Commit(
                        message, Author(name)
                    )
                )
            )

            val createGithubListUseCase = CreateGithubListUseCase(
                internetStateUseCase,
                githubCachedRepository,
                githubApiOnlineRepository,
                ownerAndRepositoryNameValidator
            )

            Assert.assertEquals(
                eitherSuccess(
                    RepositoryCommitsViewData(
                        repositoryId, listOf(
                            CommitsViewData(message, sha, name)
                        )
                    )
                ), createGithubListUseCase.fetchGithubCommits("a/a", 0).first()
            )
        }


    @Test
    fun `correct name input, internet turn on, error on downloading commits should try get data from cache and emit response`() =
        runTest {
            every { ownerAndRepositoryNameValidator.isValid(any()) } returns true
            every { internetStateUseCase.isDeviceConnectedToNetwork() } returns true
            val repositoryId = 1
            coEvery { githubApiOnlineRepository.getRepository(any()) } returns RepositoryResponse(
                repositoryId
            )
            coEvery { githubCachedRepository.getRepository(any()) } returns RepositoryResponse(
                repositoryId
            )
            coEvery { githubApiOnlineRepository.getCommits(any(), any()) } throws ConnectException()
            val sha = "sha"
            val message = "message"
            val name = "authorName"
            coEvery { githubCachedRepository.getCommits(any(), any()) } returns listOf(
                CommitResponse(
                    sha, Commit(
                        message, Author(name)
                    )
                )
            )

            val createGithubListUseCase = CreateGithubListUseCase(
                internetStateUseCase,
                githubCachedRepository,
                githubApiOnlineRepository,
                ownerAndRepositoryNameValidator
            )

            Assert.assertEquals(
                eitherSuccess(
                    RepositoryCommitsViewData(
                        repositoryId, listOf(
                            CommitsViewData(message, sha, name)
                        )
                    )
                ), createGithubListUseCase.fetchGithubCommits("a/a", 0).first()
            )
        }

}