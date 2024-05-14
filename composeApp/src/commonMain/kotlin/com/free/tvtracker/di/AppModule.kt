package com.free.tvtracker.di

import com.free.tvtracker.core.data.http.RemoteDataSource
import com.free.tvtracker.core.data.http.TvHttpClient
import com.free.tvtracker.core.data.sql.DatabaseDriverFactory
import com.free.tvtracker.core.data.sql.LocalSqlDataProvider
import com.free.tvtracker.data.search.SearchRepository
import com.free.tvtracker.data.tracked.TrackedShowsRepository
import com.free.tvtracker.data.tracked.WatchedEpisodesTaskQueue
import com.free.tvtracker.domain.GetNextUnwatchedEpisodeUseCase
import com.free.tvtracker.domain.GetTrackedShowUseCase
import com.free.tvtracker.domain.GetShowsUseCase
import com.free.tvtracker.domain.GetWatchlistedShowsUseCase
import com.free.tvtracker.domain.IsTrackedShowWatchableUseCase
import com.free.tvtracker.domain.TrackedShowReducer
import com.free.tvtracker.screens.details.mappers.ShowCastUiModelMapper
import com.free.tvtracker.screens.details.mappers.ShowEpisodeUiModelMapper
import com.free.tvtracker.screens.details.mappers.ShowSeasonUiModelMapper
import com.free.tvtracker.screens.details.mappers.ShowUiModelMapper
import com.free.tvtracker.screens.details.mappers.ShowVideoUiModelMapper
import com.free.tvtracker.screens.details.mappers.ShowWatchProviderUiModelMapper
import com.free.tvtracker.screens.watching.GetWatchingShowsUseCase
import com.free.tvtracker.shared.db.AppDatabase
import com.squareup.sqldelight.db.SqlDriver
import org.koin.dsl.module

fun appModules() = module {
    single<TvHttpClient> { TvHttpClient() }
    single<DatabaseDriverFactory> { DatabaseDriverFactory() }
    single<SqlDriver> { get<DatabaseDriverFactory>().createDriver() }
    single<AppDatabase> { AppDatabase(get()) }
    single<LocalSqlDataProvider> { LocalSqlDataProvider(get()) }
    single<RemoteDataSource> { RemoteDataSource(get()) }
    single<TrackedShowsRepository> { TrackedShowsRepository(get(), get(), get()) }
    single<WatchedEpisodesTaskQueue> { WatchedEpisodesTaskQueue(get(), get()) }
    single<GetWatchlistedShowsUseCase> { GetWatchlistedShowsUseCase() }
    factory<TrackedShowReducer> { TrackedShowReducer() }
    factory<GetTrackedShowUseCase> { GetTrackedShowUseCase(get(), get(), get()) }
    factory<GetShowsUseCase> { GetShowsUseCase(get(), get()) }
    factory<GetWatchingShowsUseCase> { GetWatchingShowsUseCase(get(), get(), get()) }
    factory<IsTrackedShowWatchableUseCase> { IsTrackedShowWatchableUseCase(get()) }
    factory<ShowEpisodeUiModelMapper> { ShowEpisodeUiModelMapper() }
    factory<ShowSeasonUiModelMapper> { ShowSeasonUiModelMapper(get()) }
    factory<ShowCastUiModelMapper> { ShowCastUiModelMapper() }
    factory<ShowWatchProviderUiModelMapper> { ShowWatchProviderUiModelMapper() }
    factory<ShowVideoUiModelMapper> { ShowVideoUiModelMapper() }
    factory<ShowUiModelMapper> { ShowUiModelMapper(get(), get(), get(), get()) }
    single<SearchRepository> { SearchRepository(get()) }
    factory<GetNextUnwatchedEpisodeUseCase> { GetNextUnwatchedEpisodeUseCase() }
}
