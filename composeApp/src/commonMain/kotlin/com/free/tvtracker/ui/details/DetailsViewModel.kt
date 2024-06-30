package com.free.tvtracker.ui.details

import com.free.tvtracker.expect.ui.ViewModel
import com.free.tvtracker.data.tracked.MarkEpisodeWatched
import com.free.tvtracker.data.tracked.TrackedShowsRepository
import com.free.tvtracker.domain.GetMovieByTmdbIdUseCase
import com.free.tvtracker.domain.GetShowByTmdbIdUseCase
import com.free.tvtracker.ui.details.mappers.DetailsUiModelForMovieMapper
import com.free.tvtracker.ui.details.mappers.DetailsUiModelForShowMapper
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DetailsViewModel(
    private val detailsUiModelForShowMapper: DetailsUiModelForShowMapper,
    private val detailsUiModelForMovieMapper: DetailsUiModelForMovieMapper,
    private val trackedShowsRepository: TrackedShowsRepository,
    private val getShowByTmdbIdUseCase: GetShowByTmdbIdUseCase,
    private val getMovieByTmdbIdUseCase: GetMovieByTmdbIdUseCase,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
) : ViewModel() {

    data class LoadContent(val tmdbId: Int, val isTvShow: Boolean)

    val result: MutableStateFlow<DetailsUiState> = MutableStateFlow(DetailsUiState.Loading)

    fun getShareLink(): String = (result.value as? DetailsUiState.Ok)?.data?.homepageUrl ?: "Missing url"

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
            getShowByTmdbIdUseCase.invoke(tmdbId).catch {
                result.value = DetailsUiState.Error
            }.collect { trackedShowResult ->
                trackedShowResult.showData
                    .asSuccess { data ->
                        result.value =
                            DetailsUiState.Ok(detailsUiModelForShowMapper.map(data, trackedShowResult.tracked))
                    }.asError {
                        result.value = DetailsUiState.Error
                    }
            }
        }
    }

    private fun loadMovie(tmdbId: Int) {
        viewModelScope.launch(ioDispatcher) {
            getMovieByTmdbIdUseCase.invoke(tmdbId).catch {
                result.value = DetailsUiState.Error
            }.collect { trackedShowResult ->
                trackedShowResult.showData
                    .asSuccess { data ->
                        result.value =
                            DetailsUiState.Ok(detailsUiModelForMovieMapper.map(data, trackedShowResult.tracked))
                    }.coAsError {
                        result.value = DetailsUiState.Error
                    }
            }
        }
    }

    fun action(action: DetailsAction) {
        when (action) {
            is DetailsAction.MarkSeasonWatched -> {
                val trackedShow = trackedShowsRepository.getByTmdbId(action.tmdbShowId) ?: return
                val season =
                    (result.value as? DetailsUiState.Ok)?.data?.seasons?.firstOrNull { it.seasonId == action.seasonId }
                if (season == null) return
                viewModelScope.launch(ioDispatcher) {
                    val episodes = season.episodes
                        .filter { !it.watched }
                        .filter { it.isWatchable }
                        .map { MarkEpisodeWatched(trackedShow.tvShow!!.id, it.id) }
                    trackedShowsRepository.markEpisodeAsWatched(episodes)
                }
            }

            is DetailsAction.TrackingAction -> {
                result.update {
                    if (it is DetailsUiState.Ok) it.copy(
                        data = it.data.copy(
                            trackingStatus = it.data.trackingStatus.copy(
                                isLoading = true
                            )
                        )
                    ) else it
                }
                viewModelScope.launch(ioDispatcher) {
                    // todo: this loads infinitely if http call fails
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

                        DetailsUiModel.TrackingStatus.Action.RemoveMovieFromWatched -> {
                            trackedShowsRepository.removeContent(action.uiModel.trackedContentId!!, isTvShow = false)
                        }

                        DetailsUiModel.TrackingStatus.Action.MoveMovieToFinished -> {
                            trackedShowsRepository.setWatchlisted(
                                action.uiModel.trackedContentId!!,
                                isTvShow = action.uiModel.isTvShow,
                                watchlisted = false
                            )
                        }
                    }
                }
            }
        }
    }

    sealed class DetailsAction {
        data class MarkSeasonWatched(val seasonId: Int, val tmdbShowId: Int) : DetailsAction()
        data class TrackingAction(
            val uiModel: DetailsUiModel,
            val trackingAction: DetailsUiModel.TrackingStatus.Action
        ) : DetailsAction()
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
    val trackingStatus: TrackingStatus,
    val trackedContentId: Int?,
    val homepageUrl: String?,
    val description: String?,
    val genres: String?,
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
    val budget: String,
    val revenue: String,
    val website: String?,
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
            MoveMovieToFinished
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
}
