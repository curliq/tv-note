package com.free.tvtracker.screens.watchlist

import com.free.tvtracker.core.ui.ViewModel
import com.free.tvtracker.data.tracked.TrackedShowsRepository
import com.free.tvtracker.domain.GetShowsUseCase
import com.free.tvtracker.domain.GetWatchlistedShowsUseCase
import com.free.tvtracker.tracked.response.TrackedShowApiModel
import com.free.tvtracker.utils.TmdbConfigData
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class WatchlistedShowsViewModel(
    private val trackedShowsRepository: TrackedShowsRepository,
    private val getShowsUseCase: GetShowsUseCase,
    private val getWatchlistedShowsUseCase: GetWatchlistedShowsUseCase,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
) : ViewModel() {
    val shows: MutableStateFlow<WatchlistUiState> = MutableStateFlow(WatchlistUiState.Loading)

    init {
        viewModelScope.launch(ioDispatcher) {
            getShowsUseCase(trackedShowsRepository.watchlistedShows).collect { data ->
                data.data.asSuccess {
                    if (data.data.data.isNullOrEmpty()) {
                        shows.value = WatchlistUiState.Empty
                    } else {
                        shows.value = WatchlistUiState.Ok(
                            shows = getWatchlistedShowsUseCase(data.data.data!!).map { it.toUiModel() }
                        )
                    }
                }
                data.data.asError {
                    shows.value = WatchlistUiState.Error
                }
            }
        }
        refresh()
    }

    fun refresh() {
        shows.value = WatchlistUiState.Loading
        viewModelScope.launch(ioDispatcher) {
            trackedShowsRepository.emitLatestWatchlisted()
        }
    }
}

sealed class WatchlistUiState {
    data object Loading : WatchlistUiState()
    data object Error : WatchlistUiState()
    data object Empty : WatchlistUiState()
    data class Ok(val shows: List<WatchlistShowUiModel>) : WatchlistUiState()
}

data class WatchlistShowUiModel(
    val trackedShowId: Int,
    val tmdbId: Int,
    val title: String,
    val image: String,
    val status: String,
    val nextEpisode: String?,
)

fun TrackedShowApiModel.toUiModel(): WatchlistShowUiModel {
    return WatchlistShowUiModel(
        trackedShowId = this.id,
        tmdbId = this.storedShow.tmdbId,
        title = this.storedShow.title,
        image = TmdbConfigData.get().getPosterUrl(this.storedShow.posterImage),
        nextEpisode = "",
        status = this.storedShow.status
    )
}
