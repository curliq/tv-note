package com.free.tvtracker.ui.watchlist

import com.free.tvtracker.expect.ui.ViewModel
import com.free.tvtracker.data.tracked.TrackedShowsRepository
import com.free.tvtracker.domain.GetShowsUseCase
import com.free.tvtracker.domain.GetWatchlistedShowsUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filter
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
                            shows.value = WatchlistUiState.Ok(
                                shows = res.map(mapper.map())
                            )
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
}

sealed class WatchlistUiState {
    data object Loading : WatchlistUiState()
    data object Error : WatchlistUiState()
    data object Empty : WatchlistUiState()
    data class Ok(val shows: List<WatchlistShowUiModel>) : WatchlistUiState()
}

data class WatchlistShowUiModel(
    val tmdbId: Int,
    val title: String,
    val image: String,
    val status: String,
)
