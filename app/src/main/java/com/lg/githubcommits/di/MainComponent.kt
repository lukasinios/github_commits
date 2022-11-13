package com.lg.githubcommits.di

import android.content.Context
import com.lg.data.api.GithubApi
import com.lg.data.api.GithubApiOnlineRepositoryImpl
import com.lg.data.database.GithubCachedRepositoryImpl
import com.lg.data.database.GithubDao
import com.lg.data.database.mapper.GithubCommitMapper
import com.lg.data.database.mapper.GithubRepositoryMapper
import com.lg.domain.githublist.repository.GithubApiOnlineRepository
import com.lg.domain.githublist.repository.GithubCachedRepository
import com.lg.domain.githublist.usecase.CreateGithubListUseCase
import com.lg.domain.githublist.usecase.InternetStateUseCase
import com.lg.domain.githublist.usecase.OwnerAndRepositoryNameValidator
import com.lg.domain.history.CachedRepositoryMapper
import com.lg.domain.history.GetHistoricSearchUseCase
import dagger.Module
import dagger.Provides
import dagger.Reusable
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module(includes = [DbComponent::class, NetworkComponent::class])
class MainComponent {

    @Provides
    @Reusable
    fun provideGithubCommitMapper() = GithubCommitMapper()

    @Provides
    @Reusable
    fun provideGithubRepositoryMapper() = GithubRepositoryMapper()

    @Provides
    @Reusable
    fun provideOwnerAndRepositoryNameValidator() = OwnerAndRepositoryNameValidator()

    @Provides
    @Reusable
    fun provideGithubCachedRepository(
        githubDao: GithubDao,
        githubRepositoryMapper: GithubRepositoryMapper,
        githubCommitMapper: GithubCommitMapper
    ): GithubCachedRepository =
        GithubCachedRepositoryImpl(githubDao, githubRepositoryMapper, githubCommitMapper)

    @Provides
    @Reusable
    fun provideGithubOnlineRepository(githubApi: GithubApi): GithubApiOnlineRepository =
        GithubApiOnlineRepositoryImpl(githubApi)

    @Provides
    @Reusable
    fun provideInternetStateUseCase(@ApplicationContext context: Context) =
        InternetStateUseCase(context)

    @Provides
    @Reusable
    fun provideCreateGithubListUseCase(
        internetStateUseCase: InternetStateUseCase,
        githubCachedRepository: GithubCachedRepository,
        githubApiOnlineRepository: GithubApiOnlineRepository,
        ownerAndRepositoryNameValidator: OwnerAndRepositoryNameValidator
    ) = CreateGithubListUseCase(
        internetStateUseCase,
        githubCachedRepository,
        githubApiOnlineRepository,
        ownerAndRepositoryNameValidator
    )

    @Provides
    @Reusable
    fun provideCachedRepositoryMapper() = CachedRepositoryMapper()

    @Provides
    @Reusable
    fun provideGetHistoricSearchUseCase(
        githubCachedRepository: GithubCachedRepository,
        cachedRepositoryMapper: CachedRepositoryMapper
    ) = GetHistoricSearchUseCase(githubCachedRepository, cachedRepositoryMapper)

}