package com.free.tvtracker.di

import com.free.tvtracker.core.data.http.TvHttpClient
import com.free.tvtracker.core.data.sql.DatabaseDriverFactory
import com.free.tvtracker.core.data.sql.LocalSqlDataProvider
import com.free.tvtracker.data.search.SearchRepository
import com.free.tvtracker.data.tracked.TrackedShowsRepository
import com.free.tvtracker.data.tracked.WatchedEpisodesTaskQueue
import com.free.tvtracker.screens.watching.GetNextUnwatchedEpisodeUseCase
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
    single<TrackedShowsRepository> { TrackedShowsRepository(get(), get(), get()) }
    single<WatchedEpisodesTaskQueue> { WatchedEpisodesTaskQueue(get(), get()) }
    factory<GetWatchingShowsUseCase> { GetWatchingShowsUseCase(get(), get()) }
    single<SearchRepository> { SearchRepository(get()) }
    factory<GetNextUnwatchedEpisodeUseCase> { GetNextUnwatchedEpisodeUseCase() }
}
