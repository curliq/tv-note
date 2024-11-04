package com.free.tvtracker.ui.watchlist

import com.free.tvtracker.data.tracked.TrackedShowsRepository
import com.free.tvtracker.domain.GetShowsUseCase
import com.free.tvtracker.domain.GetWatchlistedShowsUseCase
import com.free.tvtracker.expect.ui.ViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class WatchlistedShowsViewModel(
    private val trackedShowsRepository: TrackedShowsRepository,
    private val getShowsUseCase: GetShowsUseCase,
    private val getWatchlistedShowsUseCase: GetWatchlistedShowsUseCase,
    private val mapper: WatchlistShowUiModelMapper,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
) : ViewModel() {
    val shows: MutableStateFlow<WatchlistUiState> = MutableStateFlow(WatchlistUiState.Loading)

    init {
        viewModelScope.launch(ioDispatcher) {
            getShowsUseCase(trackedShowsRepository.watchlistedShows)
                .filter { it.status.fetched }
                .collect { data ->
                    if (data.status.success) {
                        val res = getWatchlistedShowsUseCase(data.data)
                        if (res.isEmpty()) {
                            shows.value = WatchlistUiState.Empty
                        } else {
                            shows.update {
                                WatchlistUiState.Ok(
                                    _shows = res.map(mapper.map()),
                                    (it as? WatchlistUiState.Ok)?.filterTvShows ?: true,
                                )
                            }
                        }
                    } else {
                        shows.value = WatchlistUiState.Error
                    }
                }
        }
        refresh()
    }

    fun refresh() {
        shows.value = WatchlistUiState.Loading
        viewModelScope.launch(ioDispatcher) {
            trackedShowsRepository.updateWatchlisted()
        }
    }

    fun action(action: WatchlistedAction) {
        when (action) {
            WatchlistedAction.ToggleMovies -> {
                shows.update {
                    if (it is WatchlistUiState.Ok) {
                        it.copy(filterTvShows = !it.filterTvShows)
                    } else it
                }
            }

            WatchlistedAction.ToggleTvShows -> {
                shows.update {
                    if (it is WatchlistUiState.Ok) {
                        it.copy(filterTvShows = !it.filterTvShows)
                    } else it
                }
            }
        }
    }

    sealed class WatchlistedAction {
        data object ToggleTvShows : WatchlistedAction()
        data object ToggleMovies : WatchlistedAction()
    }
}

sealed class WatchlistUiState {
    data object Loading : WatchlistUiState()
    data object Error : WatchlistUiState()
    data object Empty : WatchlistUiState()
    data class Ok(
        val _shows: List<WatchlistShowUiModel>,
        val filterTvShows: Boolean,
    ) : WatchlistUiState() {
        val shows: List<WatchlistShowUiModel>
            get() {
                return _shows.filter { filterTvShows == it.isTvShow }
            }
    }
}

data class WatchlistShowUiModel(
    val tmdbId: Int,
    val title: String,
    val image: String,
    val status: String,
    val isTvShow: Boolean
)
