package com.free.tvtracker.ui.details

import com.free.tvtracker.core.Logger
import com.free.tvtracker.data.reviews.ReviewsRepository
import com.free.tvtracker.data.tracked.MarkEpisodeWatched
import com.free.tvtracker.data.tracked.TrackedShowsRepository
import com.free.tvtracker.data.watchlists.WatchlistsRepository
import com.free.tvtracker.domain.GetMovieByTmdbIdUseCase
import com.free.tvtracker.domain.GetPurchaseStatusUseCase
import com.free.tvtracker.domain.GetShowByTmdbIdUseCase
import com.free.tvtracker.domain.PurchaseStatus
import com.free.tvtracker.expect.ViewModel
import com.free.tvtracker.ui.details.mappers.DetailsRatingsUiModelMapper
import com.free.tvtracker.ui.details.mappers.DetailsReviewsUiModelMapper
import com.free.tvtracker.ui.details.mappers.DetailsUiModelForMovieMapper
import com.free.tvtracker.ui.details.mappers.DetailsUiModelForShowMapper
import com.free.tvtracker.ui.watchlists.list.WatchlistsUiModel
import com.free.tvtracker.ui.watchlists.list.WatchlistsUiState
import com.free.tvtracker.ui.watchlists.list.WatchlistsViewModel
import com.free.tvtracker.watchlists.response.WatchlistApiModel.Companion.FINISHED_LIST_ID
import com.free.tvtracker.watchlists.response.WatchlistApiModel.Companion.WATCHLIST_LIST_ID
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DetailsViewModel(
    private val detailsUiModelForShowMapper: DetailsUiModelForShowMapper,
    private val detailsUiModelForMovieMapper: DetailsUiModelForMovieMapper,
    private val ratingsMapper: DetailsRatingsUiModelMapper,
    private val reviewsMapper: DetailsReviewsUiModelMapper,
    private val trackedShowsRepository: TrackedShowsRepository,
    private val reviewsRepository: ReviewsRepository,
    private val getShowByTmdbId: GetShowByTmdbIdUseCase,
    private val getMovieByTmdbId: GetMovieByTmdbIdUseCase,
    private val watchlistsViewModel: WatchlistsViewModel,
    private val watchlistsRepository: WatchlistsRepository,
    purchaseStatus: GetPurchaseStatusUseCase,
    private val logger: Logger,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
) : ViewModel() {

    data class LoadContent(val tmdbId: Int, val isTvShow: Boolean)

    val result: MutableStateFlow<DetailsUiState> = MutableStateFlow(DetailsUiState.Loading)

    val isActionsAllowed: Flow<Boolean> =
        purchaseStatus.invoke().map { it.status != PurchaseStatus.Status.TrialFinished }

    data class DetailsWatchlists(val lists: List<Item>) {
        data class Item(val list: WatchlistsUiModel, val checked: Boolean)
    }

    val watchlists: Flow<DetailsWatchlists> = combine(result, watchlistsViewModel.stateAsFlow) { show, list ->
        if (show is DetailsUiState.Ok && list is WatchlistsUiState.Ok) {
            DetailsWatchlists(lists = list.watchlists.map { watchlist ->
                val isChecked = when (watchlist.id) {
                    WATCHLIST_LIST_ID -> {
                        show.data.watchlisted == true
                    }

                    FINISHED_LIST_ID -> {
                        show.data.isFinished == true
                    }

                    else -> {
                        show.data.watchlists.map { it.id }.contains(watchlist.id)
                    }
                }
                DetailsWatchlists.Item(watchlist, isChecked)
            })
        } else {
            DetailsWatchlists(emptyList())
        }
    }

    fun getShareLink(): String {
        logger.d("result: ${result.value}")
        return (result.value as? DetailsUiState.Ok)?.data?.homepageUrl ?: "Missing url"
    }

    fun loadContent(content: LoadContent) {
        result.value = DetailsUiState.Loading
        if (content.isTvShow) {
            loadTvShow(content.tmdbId)
        } else {
            loadMovie(content.tmdbId)
        }
    }

    private fun loadTvShow(tmdbId: Int) {
        viewModelScope.launch(ioDispatcher) {
            getShowByTmdbId.invoke(tmdbId).catch {
                result.value = DetailsUiState.Error
            }.collect { trackedShowResult ->
                trackedShowResult.showData
                    .coAsSuccess { data ->
                        result.value =
                            DetailsUiState.Ok(detailsUiModelForShowMapper.map(data, trackedShowResult.tracked))
                        loadRatings(data.imdbId)
                    }.asError {
                        result.value = DetailsUiState.Error
                    }
            }
        }
    }

    private fun loadMovie(tmdbId: Int) {
        viewModelScope.launch(ioDispatcher) {
            getMovieByTmdbId.invoke(tmdbId).catch {
                result.value = DetailsUiState.Error
            }.collect { trackedShowResult ->
                trackedShowResult.showData
                    .coAsSuccess { data ->
                        result.value =
                            DetailsUiState.Ok(detailsUiModelForMovieMapper.map(data, trackedShowResult.tracked))
                        loadRatings(data.imdbId)
                    }.coAsError {
                        result.value = DetailsUiState.Error
                    }
            }
        }
    }

    private suspend fun loadRatings(imdbId: String?) {
        if (imdbId == null) return
        val ratingsData = reviewsRepository.getRatings(imdbId)
        val ratings = ratingsMapper.map(ratingsData)
        if (ratings?.imdbRating != null && ratings.imdbVoteCount != null) {
            result.update {
                if (it is DetailsUiState.Ok) it.copy(
                    data = it.data.copy(
                        omdbRatings = ratings,
                    )
                ) else it
            }
        }
        val reviewsData = reviewsRepository.getReviews(imdbId)
        val reviews = reviewsMapper.map(reviewsData)
        if (reviews?.reviews != null) {
            result.update {
                if (it is DetailsUiState.Ok) it.copy(
                    data = it.data.copy(
                        reviews = reviews
                    )
                ) else it
            }
        }
    }

    fun action(action: DetailsAction) {
        when (action) {
            is DetailsAction.MarkSeasonWatched -> {
                viewModelScope.launch(ioDispatcher) {
                    var trackedShow = trackedShowsRepository.getByTmdbId(action.tmdbShowId)
                    if (trackedShow == null) {
                        trackedShow = trackedShowsRepository.coAddTrackedShow(
                            action.tmdbShowId,
                            isTvShow = true,
                            watchlisted = false
                        )
                    }
                    if (trackedShow == null) return@launch
                    val season =
                        (result.value as? DetailsUiState.Ok)?.data?.seasons?.firstOrNull { it.seasonId == action.seasonId }
                    if (season == null) return@launch
                    val episodes = season.episodes
                        .filter { !it.watched }
                        .filter { it.isWatchable }
                        .map { MarkEpisodeWatched(trackedShow.tvShow!!.id, it.id) }
                    trackedShowsRepository.markEpisodeAsWatched(episodes)
                }
            }

            is DetailsAction.TrackingAction -> {
                if (action.trackingAction != DetailsUiModel.TrackingStatus.Action.ManageWatchlists) {
                    result.update {
                        if (it is DetailsUiState.Ok) it.copy(
                            data = it.data.copy(
                                trackingStatus = it.data.trackingStatus.copy(
                                    isLoading = true
                                )
                            )
                        ) else it
                    }
                }
                viewModelScope.launch(ioDispatcher) {
                    when (action.trackingAction) {
                        DetailsUiModel.TrackingStatus.Action.RemoveFromWatchlist -> {
                            trackedShowsRepository.removeContent(
                                action.uiModel.trackedContentId!!,
                                action.uiModel.isTvShow
                            )
                        }

                        DetailsUiModel.TrackingStatus.Action.RemoveFromWatching -> {
                            trackedShowsRepository.removeContent(
                                action.uiModel.trackedContentId!!,
                                action.uiModel.isTvShow
                            )
                            action.navAction.invoke(DetailsScreenNavAction.HideManageWatchlists)
                        }

                        DetailsUiModel.TrackingStatus.Action.RemoveMovieFromWatched -> {
                            trackedShowsRepository.removeContent(action.uiModel.trackedContentId!!, isTvShow = false)
                        }

                        DetailsUiModel.TrackingStatus.Action.TrackWatchlist -> {
                            trackedShowsRepository.addTrackedShow(
                                action.uiModel.tmdbId,
                                isTvShow = action.uiModel.isTvShow,
                                watchlisted = true
                            )
                        }

                        DetailsUiModel.TrackingStatus.Action.TrackWatching -> {
                            trackedShowsRepository.addTrackedShow(
                                action.uiModel.tmdbId,
                                isTvShow = action.uiModel.isTvShow,
                                watchlisted = false
                            )
                        }

                        DetailsUiModel.TrackingStatus.Action.MoveToWatchlist -> {
                            trackedShowsRepository.setWatchlisted(
                                action.uiModel.trackedContentId!!,
                                isTvShow = action.uiModel.isTvShow,
                                watchlisted = true
                            )
                        }

                        DetailsUiModel.TrackingStatus.Action.MoveToWatching -> {
                            trackedShowsRepository.setWatchlisted(
                                action.uiModel.trackedContentId!!,
                                isTvShow = action.uiModel.isTvShow,
                                watchlisted = false
                            )
                        }

                        DetailsUiModel.TrackingStatus.Action.MoveMovieToFinished -> {
                            trackedShowsRepository.setWatchlisted(
                                action.uiModel.trackedContentId!!,
                                isTvShow = action.uiModel.isTvShow,
                                watchlisted = false
                            )
                        }

                        DetailsUiModel.TrackingStatus.Action.ManageWatchlists -> {
                            action.navAction.invoke(DetailsScreenNavAction.GoManageWatchlists)
                        }
                    }
                }
            }

            is DetailsAction.MarkShowWatched -> {
                viewModelScope.launch(ioDispatcher) {
                    if (action.trackedContentId == null) {
                        trackedShowsRepository.coAddTrackedShow(
                            action.tmdbShowId,
                            isTvShow = true,
                            watchlisted = false
                        )
                    }
                    val trackedShow = trackedShowsRepository.getByTmdbId(action.tmdbShowId) ?: return@launch
                    val seasons = (result.value as? DetailsUiState.Ok)?.data?.seasons ?: return@launch
                    val episodes = seasons.flatMap { it.episodes }
                        .filter { !it.watched }
                        .filter { it.isWatchable }
                        .map { MarkEpisodeWatched(trackedShow.tvShow!!.id, it.id) }
                    trackedShowsRepository.markEpisodeAsWatched(episodes)
                }
            }

            is DetailsAction.AddToWatchList -> {
                if (action.watchlistId == WATCHLIST_LIST_ID) {
                    viewModelScope.launch(ioDispatcher) {
                        trackedShowsRepository.setWatchlisted(
                            action.uiModel.trackedContentId!!,
                            isTvShow = action.uiModel.isTvShow,
                            watchlisted = true
                        )
                    }
                } else {
                    viewModelScope.launch(ioDispatcher) {
                        trackedShowsRepository.addContentToWatchlist(
                            action.uiModel.trackedContentId!!,
                            action.watchlistId,
                            action.uiModel.isTvShow
                        )
                        watchlistsRepository.fetch()
                        watchlistsRepository.fetchContent(action.watchlistId, forceUpdate = true)
                    }
                }
            }

            is DetailsAction.RemoveFromWatchList -> {
                if (action.watchlistId == WATCHLIST_LIST_ID) {
                    viewModelScope.launch(ioDispatcher) {
                        trackedShowsRepository.setWatchlisted(
                            action.uiModel.trackedContentId!!,
                            isTvShow = action.uiModel.isTvShow,
                            watchlisted = false
                        )
                    }
                } else {
                    viewModelScope.launch(ioDispatcher) {
                        trackedShowsRepository.removeContentFromWatchlist(
                            action.uiModel.trackedContentId!!,
                            action.watchlistId,
                            action.uiModel.isTvShow
                        )
                        watchlistsRepository.fetch()
                        watchlistsRepository.fetchContent(action.watchlistId, forceUpdate = true)
                    }
                }
            }
        }
    }

    sealed class DetailsAction {
        data class MarkSeasonWatched(val seasonId: Int, val tmdbShowId: Int) : DetailsAction()
        data class TrackingAction(
            val uiModel: DetailsUiModel,
            val trackingAction: DetailsUiModel.TrackingStatus.Action,
            val navAction: ((DetailsScreenNavAction) -> Unit),
        ) : DetailsAction()

        data class MarkShowWatched(val tmdbShowId: Int, val trackedContentId: Int?) : DetailsAction()
        data class AddToWatchList(val uiModel: DetailsUiModel, val watchlistId: Int) : DetailsAction()
        data class RemoveFromWatchList(val uiModel: DetailsUiModel, val watchlistId: Int) : DetailsAction()
    }
}

sealed class DetailsUiState {
    data object Loading : DetailsUiState()
    data object Error : DetailsUiState()
    data class Ok(val data: DetailsUiModel) : DetailsUiState()
}

data class DetailsUiModel(
    val isTvShow: Boolean,
    val tmdbId: Int,
    val name: String,
    val posterUrl: String,
    val releaseStatus: String,
    val duration: String?,
    val watchlisted: Boolean?,
    val isWatching: Boolean?,
    val isFinished: Boolean?,
    val trackingStatus: TrackingStatus,
    val trackedContentId: Int?,
    val homepageUrl: String?,
    val description: String?,
    val genres: List<String>,
    val seasonsInfo: String?,
    val seasons: List<Season>?,
    val movieSeries: MovieSeries?,
    val castFirst: Cast?,
    val castSecond: Cast?,
    val cast: List<Cast>,
    val watchProviders: List<WatchProvider>,
    val crew: List<Crew>,
    val watchProviderCountry: String,
    val mediaTrailer: Video?,
    val mediaVideosTrailers: List<Video>,
    val mediaVideosTeasers: List<Video>,
    val mediaVideosBehindTheScenes: List<Video>,
    val mediaVideosClipsAndOther: List<Video>,
    val mediaMostPopularImage: String?,
    val mediaImagesPosters: List<String>,
    val mediaImagesBackdrops: List<String>,
    val ratingTmdbVoteAverage: String,
    val ratingTmdbVoteCount: String,
    val omdbRatings: Ratings?,
    val reviews: Reviews?,
    val budget: String,
    val revenue: String,
    val website: String?,
    val watchlists: List<Watchlist>,
) {
    sealed interface Person {
        val tmdbId: Int
        val irlName: String
        val photo: String
    }

    data class Cast(
        override val tmdbId: Int,
        override val irlName: String,
        override val photo: String,
        val characterName: String,
    ) : Person

    data class Crew(
        override val tmdbId: Int,
        override val irlName: String,
        override val photo: String,
        val job: String,
    ) : Person

    data class TrackingStatus(val action1: Action?, val action2: Action?, val isLoading: Boolean = false) {
        enum class Action {
            RemoveFromWatchlist,
            RemoveFromWatching,
            RemoveMovieFromWatched,
            TrackWatchlist,
            TrackWatching,
            MoveToWatchlist,
            MoveToWatching,
            MoveMovieToFinished,
            ManageWatchlists,
        }
    }

    data class Season(
        val seasonId: Int,
        val tmdbShowId: Int,
        val seasonTitle: String,
        val watched: Boolean,
        val isWatchable: Boolean,
        val episodes: List<Episode>
    ) {
        data class Episode(
            val id: Int,
            val thumbnail: String,
            val number: String,
            val name: String,
            val releaseDate: String,
            val watched: Boolean,
            val isWatchable: Boolean,
        )
    }

    data class WatchProvider(val logo: String, val deeplink: String)

    data class Video(val thumbnail: String, val videoUrl: String, val title: String?)

    data class MovieSeries(
        val overview: String?,
        val movies: List<Movie>
    ) {
        data class Movie(val tmdbId: Int, val posterUrl: String, val name: String, val year: String)
    }

    data class Ratings(
        val imdbRating: String?,
        val imdbVoteCount: String?,
        val tomatoesRatingPercentage: String?
    )

    data class Reviews(
        val reviews: List<Review>,
        val total: Long
    ) {
        data class Review(
            val id: String,
            val authorName: String,
            val title: String,
            val content: String,
            val created: String,
        )
    }

    data class Watchlist(
        val id: Int,
        val name: String,
    )
}
