package com.free.tvtracker.di

import com.free.tvtracker.data.common.sql.LocalSqlDataProvider
import com.free.tvtracker.data.search.SearchRepository
import com.free.tvtracker.data.session.SessionRepository
import com.free.tvtracker.data.session.SessionStore
import com.free.tvtracker.data.tracked.TrackedShowsRepository
import com.free.tvtracker.data.tracked.WatchedEpisodesTaskQueue
import com.free.tvtracker.data.user.UserRepository
import com.free.tvtracker.domain.GetNextUnwatchedEpisodeUseCase
import com.free.tvtracker.domain.GetShowStatusUseCase
import com.free.tvtracker.domain.GetShowsUseCase
import com.free.tvtracker.domain.GetTrackedShowByTmdbIdUseCase
import com.free.tvtracker.domain.GetWatchlistedShowsUseCase
import com.free.tvtracker.domain.IsTrackedShowWatchableUseCase
import com.free.tvtracker.domain.TrackedShowReducer
import com.free.tvtracker.expect.data.DatabaseDriverFactory
import com.free.tvtracker.expect.data.TvHttpClient
import com.free.tvtracker.expect.data.TvHttpClientEndpoints
import com.free.tvtracker.shared.db.AppDatabase
import com.free.tvtracker.ui.details.mappers.ShowCastUiModelMapper
import com.free.tvtracker.ui.details.mappers.ShowCrewUiModelMapper
import com.free.tvtracker.ui.details.mappers.ShowEpisodeUiModelMapper
import com.free.tvtracker.ui.details.mappers.ShowSeasonUiModelMapper
import com.free.tvtracker.ui.details.mappers.ShowUiModelMapper
import com.free.tvtracker.ui.details.mappers.ShowVideoUiModelMapper
import com.free.tvtracker.ui.details.mappers.ShowWatchProviderUiModelMapper
import com.free.tvtracker.ui.discover.DiscoverShowUiModelMapper
import com.free.tvtracker.ui.discover.RecommendedShowUiModelMapper
import com.free.tvtracker.ui.finished.FinishedShowUiModelMapper
import com.free.tvtracker.ui.person.PersonCastUiModelMapper
import com.free.tvtracker.ui.person.PersonCrewUiModelMapper
import com.free.tvtracker.ui.person.PersonPhotoUiModelMapper
import com.free.tvtracker.ui.person.PersonUiModelMapper
import com.free.tvtracker.ui.search.ShowSearchUiModelMapper
import com.free.tvtracker.ui.settings.SettingsUiModelMapper
import com.free.tvtracker.ui.watching.GetWatchingShowsUseCase
import com.free.tvtracker.ui.watching.WatchingShowUiModelMapper
import com.free.tvtracker.ui.watchlist.WatchlistShowUiModelMapper
import com.squareup.sqldelight.db.SqlDriver
import org.koin.dsl.module

fun appModules() = module {
    single<SessionStore> { SessionStore() }
    single<TvHttpClient> { TvHttpClient(get()) }
    single<DatabaseDriverFactory> { DatabaseDriverFactory() }
    single<SqlDriver> { get<DatabaseDriverFactory>().createDriver() }
    single<AppDatabase> { AppDatabase(get()) }
    single<LocalSqlDataProvider> { LocalSqlDataProvider(get()) }
    single<TvHttpClientEndpoints> { TvHttpClientEndpoints(get()) }
    single<SessionRepository> { SessionRepository(get(), get(), get()) }
    single<TrackedShowsRepository> { TrackedShowsRepository(get(), get(), get()) }
    single<WatchedEpisodesTaskQueue> { WatchedEpisodesTaskQueue(get(), get()) }
    single<SearchRepository> { SearchRepository(get()) }
    single<UserRepository> { UserRepository(get(), get()) }
    factory<ShowSearchUiModelMapper> { ShowSearchUiModelMapper() }
    factory<GetWatchlistedShowsUseCase> { GetWatchlistedShowsUseCase() }
    factory<TrackedShowReducer> { TrackedShowReducer() }
    factory<GetTrackedShowByTmdbIdUseCase> { GetTrackedShowByTmdbIdUseCase(get(), get(), get(), get()) }
    factory<GetShowsUseCase> { GetShowsUseCase(get(), get()) }
    factory<GetWatchingShowsUseCase> { GetWatchingShowsUseCase(get(), get(), get()) }
    factory<IsTrackedShowWatchableUseCase> { IsTrackedShowWatchableUseCase(get()) }
    factory<ShowEpisodeUiModelMapper> { ShowEpisodeUiModelMapper() }
    factory<ShowSeasonUiModelMapper> { ShowSeasonUiModelMapper(get()) }
    factory<ShowCastUiModelMapper> { ShowCastUiModelMapper() }
    factory<ShowCrewUiModelMapper> { ShowCrewUiModelMapper() }
    factory<ShowWatchProviderUiModelMapper> { ShowWatchProviderUiModelMapper() }
    factory<ShowVideoUiModelMapper> { ShowVideoUiModelMapper() }
    factory<ShowUiModelMapper> { ShowUiModelMapper(get(), get(), get(), get(), get(), get(), get()) }
    factory<PersonCastUiModelMapper> { PersonCastUiModelMapper() }
    factory<PersonCrewUiModelMapper> { PersonCrewUiModelMapper() }
    factory<PersonPhotoUiModelMapper> { PersonPhotoUiModelMapper() }
    factory<PersonUiModelMapper> { PersonUiModelMapper(get(), get(), get()) }
    factory<DiscoverShowUiModelMapper> { DiscoverShowUiModelMapper() }
    factory<WatchingShowUiModelMapper> { WatchingShowUiModelMapper(get()) }
    factory<WatchlistShowUiModelMapper> { WatchlistShowUiModelMapper(get()) }
    factory<FinishedShowUiModelMapper> { FinishedShowUiModelMapper(get()) }
    factory<RecommendedShowUiModelMapper> { RecommendedShowUiModelMapper() }
    factory<SettingsUiModelMapper> { SettingsUiModelMapper() }
    factory<GetShowStatusUseCase> { GetShowStatusUseCase() }
    factory<GetNextUnwatchedEpisodeUseCase> { GetNextUnwatchedEpisodeUseCase() }
}
