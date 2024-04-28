package com.free.tvtracker.screens.watching

import com.free.tvtracker.core.ui.ViewModel
import com.free.tvtracker.data.tracked.MarkEpisodeWatched
import com.free.tvtracker.data.tracked.TrackedShowsRepository
import com.free.tvtracker.domain.GetNextUnwatchedEpisodeUseCase
import com.free.tvtracker.domain.IsTrackedShowWatchableUseCase
import com.free.tvtracker.tracked.response.TrackedShowApiModel
import com.free.tvtracker.utils.TmdbConfigData
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class WatchingViewModel(
    private val trackedShowsRepository: TrackedShowsRepository,
    private val getShowsUseCase: GetWatchingShowsUseCase,
    private val getNextUnwatchedEpisodeUseCase: GetNextUnwatchedEpisodeUseCase,
    private val isTrackedShowWatchableUseCase: IsTrackedShowWatchableUseCase,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
) : ViewModel() {
    val shows: MutableStateFlow<WatchingUiState> = MutableStateFlow(WatchingUiState.Loading)

    init {
        viewModelScope.launch(ioDispatcher) {
            getShowsUseCase().collect { data ->
                data.data.asSuccess {
                    if (data.data.data.isNullOrEmpty()) {
                        shows.value = WatchingUiState.Empty
                    } else {
                        shows.value = WatchingUiState.Ok(
                            watching = isTrackedShowWatchableUseCase.canWatchNow(data.data.data!!).map { show ->
                                val nextEpisode = getNextUnwatchedEpisodeUseCase(show)
                                show.toUiModel(nextEpisode)
                            },
                            waitingNextEpisode = isTrackedShowWatchableUseCase.canWatchSoon(data.data.data!!)
                                .map { show ->
                                    val nextEpisode = getNextUnwatchedEpisodeUseCase(show)
                                    show.toUiModel(nextEpisode)
                                },
                        )
                    }
                }
                data.data.asError {
                    shows.value = WatchingUiState.Error
                }
            }
        }
        refresh()
    }

    fun refresh() {
        shows.value = WatchingUiState.Loading
        viewModelScope.launch(ioDispatcher) {
            trackedShowsRepository.emitLatestWatching()
        }
    }

    fun markEpisodeWatched(showId: Int?, episodeId: Int?) {
        viewModelScope.launch(ioDispatcher) {
            episodeId?.let {
                trackedShowsRepository.markEpisodeAsWatched(listOf(MarkEpisodeWatched(showId!!, it)))
            }
        }
    }
}

sealed class WatchingUiState {
    data object Loading : WatchingUiState()
    data object Error : WatchingUiState()
    data object Empty : WatchingUiState()
    data class Ok(
        val watching: List<WatchingItemUiModel>,
        val waitingNextEpisode: List<WatchingItemUiModel>
    ) : WatchingUiState()
}

data class WatchingItemUiModel(
    val trackedShowId: Int,
    val tmdbId: Int,
    val title: String,
    val image: String,
    val nextEpisode: NextEpisode?,
) {
    /**
     * All this splitting up is needed to build the animation, the full text is something like: "Watch next: S1 E5"
     */
    data class NextEpisode(
        val id: Int,
        val body: String,
        val season: String,
        val seasonNumber: Int,
        val episode: String,
        val episodeNumber: Int,
    )
}

fun TrackedShowApiModel.toUiModel(nextEpisode: TrackedShowApiModel.StoredEpisodeApiModel?): WatchingItemUiModel {
    return WatchingItemUiModel(
        trackedShowId = this.id,
        tmdbId = this.storedShow.tmdbId,
        title = this.storedShow.title,
        image = TmdbConfigData.get().getPosterUrl(this.storedShow.posterImage),
        nextEpisode = nextEpisode?.toUiModel(),
    )
}

fun TrackedShowApiModel.StoredEpisodeApiModel.toUiModel(): WatchingItemUiModel.NextEpisode {
    return WatchingItemUiModel.NextEpisode(
        id = this.id,
        body = "Watch next: ",
        season = "S${season} ",
        seasonNumber = season,
        episode = "E${episode}",
        episodeNumber = episode,
    )
}
