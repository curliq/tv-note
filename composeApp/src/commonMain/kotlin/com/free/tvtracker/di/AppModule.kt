package com.free.tvtracker.di

import com.free.tvtracker.core.Logger
import com.free.tvtracker.data.common.LocalSqlDataProvider
import com.free.tvtracker.data.iap.IapRepository
import com.free.tvtracker.data.reviews.ReviewsRepository
import com.free.tvtracker.data.search.SearchRepository
import com.free.tvtracker.data.session.SessionRepository
import com.free.tvtracker.data.session.SessionStore
import com.free.tvtracker.data.tracked.TrackedShowsRepository
import com.free.tvtracker.data.tracked.WatchedEpisodesTaskQueue
import com.free.tvtracker.data.watchlists.WatchlistsRepository
import com.free.tvtracker.domain.GetMovieByTmdbIdUseCase
import com.free.tvtracker.domain.GetNextUnwatchedEpisodeUseCase
import com.free.tvtracker.domain.GetPurchaseStatusUseCase
import com.free.tvtracker.domain.GetShowByTmdbIdUseCase
import com.free.tvtracker.domain.GetShowStatusUseCase
import com.free.tvtracker.domain.GetShowsUseCase
import com.free.tvtracker.domain.GetWatchlistedShowsUseCase
import com.free.tvtracker.domain.IsTrackedShowWatchableUseCase
import com.free.tvtracker.domain.TrackedShowReducer
import com.free.tvtracker.expect.data.CachingLocationService
import com.free.tvtracker.expect.data.DatabaseDriverFactory
import com.free.tvtracker.expect.data.OmdbHttpClient
import com.free.tvtracker.expect.data.RapidApiHttpClient
import com.free.tvtracker.expect.data.TvHttpClient
import com.free.tvtracker.expect.data.TvHttpClientEndpoints
import com.free.tvtracker.shared.db.AppDatabase
import com.free.tvtracker.ui.details.mappers.ContentDetailsRatingsUiModelMapper
import com.free.tvtracker.ui.details.mappers.ContentDetailsReviewsUiModelMapper
import com.free.tvtracker.ui.details.mappers.ContentDetailsUiModelForMovieMapper
import com.free.tvtracker.ui.details.mappers.ContentDetailsUiModelForShowMapper
import com.free.tvtracker.ui.details.mappers.ContentShowCastUiModelMapper
import com.free.tvtracker.ui.details.mappers.ContentShowCrewUiModelMapper
import com.free.tvtracker.ui.details.mappers.ContentShowEpisodeUiModelMapper
import com.free.tvtracker.ui.details.mappers.ShowSeasonUiModelMapper
import com.free.tvtracker.ui.details.mappers.ContentShowVideoUiModelMapper
import com.free.tvtracker.ui.details.mappers.ContentShowWatchProviderUiModelMapper
import com.free.tvtracker.ui.discover.DiscoverMovieUiModelMapper
import com.free.tvtracker.ui.discover.DiscoverShowUiModelMapper
import com.free.tvtracker.ui.discover.RecommendedShowUiModelMapper
import com.free.tvtracker.ui.person.PersonCastUiModelMapper
import com.free.tvtracker.ui.person.PersonCrewUiModelMapper
import com.free.tvtracker.ui.person.PersonPhotoUiModelMapper
import com.free.tvtracker.ui.person.PersonUiModelMapper
import com.free.tvtracker.ui.search.MovieSearchUiModelMapper
import com.free.tvtracker.ui.search.PersonSearchUiModelMapper
import com.free.tvtracker.ui.search.ShowSearchUiModelMapper
import com.free.tvtracker.ui.settings.SettingsUiModelMapper
import com.free.tvtracker.ui.watching.GetWatchingShowsUseCase
import com.free.tvtracker.ui.watching.WatchingShowUiModelMapper
import com.free.tvtracker.ui.watchlists.details.WatchlistDetailsShowUiModelMapper
import com.squareup.sqldelight.db.SqlDriver
import org.koin.dsl.module

fun appModules() = module {
    single<SessionStore> { SessionStore() }
    single<TvHttpClient> { TvHttpClient(get()) }
    single<OmdbHttpClient> { OmdbHttpClient() }
    single<RapidApiHttpClient> { RapidApiHttpClient() }
    single<DatabaseDriverFactory> { DatabaseDriverFactory() }
    single<SqlDriver> { get<DatabaseDriverFactory>().createDriver() }
    single<Logger> { Logger() }
    single<AppDatabase> { AppDatabase(get()) }
    single<LocalSqlDataProvider> { LocalSqlDataProvider(get()) }
    single<TvHttpClientEndpoints> { TvHttpClientEndpoints(get()) }
    single<SessionRepository> { SessionRepository(get(), get(), get(), get()) }
    single<TrackedShowsRepository> { TrackedShowsRepository(get(), get(), get(), get()) }
    single<WatchlistsRepository> { WatchlistsRepository(get(), get()) }
    single<WatchedEpisodesTaskQueue> { WatchedEpisodesTaskQueue(get(), get(), get()) }
    single<SearchRepository> { SearchRepository(get()) }
    single<IapRepository> { IapRepository(get(), get()) }
    single<ReviewsRepository> { ReviewsRepository(get(), get()) }
    factory<ShowSearchUiModelMapper> { ShowSearchUiModelMapper() }
    factory<MovieSearchUiModelMapper> { MovieSearchUiModelMapper() }
    factory<PersonSearchUiModelMapper> { PersonSearchUiModelMapper() }
    factory<GetWatchlistedShowsUseCase> { GetWatchlistedShowsUseCase() }
    factory<TrackedShowReducer> { TrackedShowReducer() }
    factory<CachingLocationService> { CachingLocationService() }
    factory<GetShowByTmdbIdUseCase> { GetShowByTmdbIdUseCase(get(), get(), get(), get(), get(), get()) }
    factory<GetMovieByTmdbIdUseCase> { GetMovieByTmdbIdUseCase(get(), get(), get()) }
    factory<GetShowsUseCase> { GetShowsUseCase(get(), get()) }
    factory<GetWatchingShowsUseCase> { GetWatchingShowsUseCase(get(), get(), get()) }
    factory<IsTrackedShowWatchableUseCase> { IsTrackedShowWatchableUseCase(get()) }
    factory<ContentShowEpisodeUiModelMapper> { ContentShowEpisodeUiModelMapper() }
    factory<ShowSeasonUiModelMapper> { ShowSeasonUiModelMapper(get()) }
    factory<ContentShowCastUiModelMapper> { ContentShowCastUiModelMapper() }
    factory<ContentShowCrewUiModelMapper> { ContentShowCrewUiModelMapper() }
    factory<ContentShowWatchProviderUiModelMapper> { ContentShowWatchProviderUiModelMapper() }
    factory<ContentShowVideoUiModelMapper> { ContentShowVideoUiModelMapper() }
    factory<ContentDetailsUiModelForShowMapper> {
        ContentDetailsUiModelForShowMapper(get(), get(), get(), get(), get(), get(), get(), get(), get())
    }
    factory<ContentDetailsUiModelForMovieMapper> { ContentDetailsUiModelForMovieMapper(get(), get(), get(), get(), get(), get()) }
    factory<PersonCastUiModelMapper> { PersonCastUiModelMapper() }
    factory<PersonCrewUiModelMapper> { PersonCrewUiModelMapper() }
    factory<PersonPhotoUiModelMapper> { PersonPhotoUiModelMapper() }
    factory<PersonUiModelMapper> { PersonUiModelMapper(get(), get(), get()) }
    factory<DiscoverShowUiModelMapper> { DiscoverShowUiModelMapper() }
    factory<DiscoverMovieUiModelMapper> { DiscoverMovieUiModelMapper() }
    factory<WatchingShowUiModelMapper> { WatchingShowUiModelMapper(get()) }
    factory<WatchlistDetailsShowUiModelMapper> { WatchlistDetailsShowUiModelMapper(get()) }
    factory<RecommendedShowUiModelMapper> { RecommendedShowUiModelMapper() }
    factory<SettingsUiModelMapper> { SettingsUiModelMapper() }
    factory<ContentDetailsReviewsUiModelMapper> { ContentDetailsReviewsUiModelMapper() }
    factory<ContentDetailsRatingsUiModelMapper> { ContentDetailsRatingsUiModelMapper() }
    factory<GetShowStatusUseCase> { GetShowStatusUseCase() }
    factory<GetNextUnwatchedEpisodeUseCase> { GetNextUnwatchedEpisodeUseCase() }
    factory<GetPurchaseStatusUseCase> { GetPurchaseStatusUseCase(get(), get(), get()) }
}
