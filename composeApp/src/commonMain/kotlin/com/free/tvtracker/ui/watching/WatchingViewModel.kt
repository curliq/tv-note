package com.free.tvtracker.ui.watching

import com.free.tvtracker.expect.ui.ViewModel
import com.free.tvtracker.data.tracked.MarkEpisodeWatched
import com.free.tvtracker.data.tracked.TrackedShowsRepository
import com.free.tvtracker.domain.IsTrackedShowWatchableUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class WatchingViewModel(
    private val trackedShowsRepository: TrackedShowsRepository,
    private val getWatchingShows: GetWatchingShowsUseCase,
    private val isTrackedShowWatchableUseCase: IsTrackedShowWatchableUseCase,
    private val watchingShowUiModelMapper: WatchingShowUiModelMapper,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
) : ViewModel() {
    val shows: MutableStateFlow<WatchingUiState> = MutableStateFlow(WatchingUiState.Loading)

    init {
        viewModelScope.launch(ioDispatcher) {
            getWatchingShows().collect { data ->
                data.data.asSuccess {
                    if (data.data.data.isNullOrEmpty()) {
                        shows.value = WatchingUiState.Empty
                    } else {
                        shows.value = WatchingUiState.Ok(
                            watching = isTrackedShowWatchableUseCase.canWatchNow(data.data.data!!)
                                .map(watchingShowUiModelMapper.map()),
                            waitingNextEpisode = isTrackedShowWatchableUseCase.canWatchSoon(data.data.data!!)
                                .map(watchingShowUiModelMapper.map())
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
            trackedShowsRepository.updateWatching()
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
    val nextEpisodeCountdown: String?,
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
