package com.free.tvtracker.ui.watchlists.details

import com.free.tvtracker.core.Logger
import com.free.tvtracker.data.tracked.TrackedShowsRepository
import com.free.tvtracker.data.watchlists.WatchlistsRepository
import com.free.tvtracker.domain.GetShowsUseCase
import com.free.tvtracker.domain.GetWatchlistedShowsUseCase
import com.free.tvtracker.domain.IsTrackedShowWatchableUseCase
import com.free.tvtracker.expect.ViewModel
import com.free.tvtracker.tracked.response.TrackedContentApiModel
import com.free.tvtracker.watchlists.response.WatchlistApiModel.Companion.FINISHED_LIST_ID
import com.free.tvtracker.watchlists.response.WatchlistApiModel.Companion.WATCHLIST_LIST_ID
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class WatchlistDetailsViewModel(
    private val watchlistsRepository: WatchlistsRepository,
    private val trackedShowsRepository: TrackedShowsRepository,
    private val getShowsUseCase: GetShowsUseCase,
    private val isTrackedShowWatchableUseCase: IsTrackedShowWatchableUseCase,
    private val getWatchlistedShowsUseCase: GetWatchlistedShowsUseCase,
    private val mapper: WatchlistDetailsShowUiModelMapper,
    private val logger: Logger,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
) : ViewModel() {

    data class LoadContent(val watchlistId: Int, val watchlistName: String)

    val shows: MutableStateFlow<WatchlistDetailsUiState> = MutableStateFlow(WatchlistDetailsUiState.Loading)
    val title = watchlistsRepository.watchlists.combine(shows) { a, b: WatchlistDetailsUiState ->
        val shows = b as? WatchlistDetailsUiState.Ok
        a.data?.firstOrNull { it.id == shows?.watchlistId }?.name ?: ""
    }

    fun loadContent(watchlistId: Int, watchlistName: String) {
        shows.value = WatchlistDetailsUiState.Loading
        viewModelScope.launch(ioDispatcher) {
            when (watchlistId) {
                WATCHLIST_LIST_ID -> {
                    getShowsUseCase(trackedShowsRepository.watchlistedShows)
                        .filter { it.status.fetched == true }
                        .collect { data ->
                            if (data.status.success) {
                                logger.d(
                                    "watchlist shows updated: ${data.data.map { it.typedId }}",
                                    this@WatchlistDetailsViewModel::class.simpleName!!
                                )
                                val res = getWatchlistedShowsUseCase(data.data)
                                updateState(watchlistId, watchlistName, res)
                            }
                        }
                }

                FINISHED_LIST_ID -> {
                    getShowsUseCase(trackedShowsRepository.finishedShows)
                        .filter { it.status.fetched == true }
                        .collect { data ->
                            if (data.status.success) {
                                logger.d(
                                    "finished shows updated: ${data.data.map { it.typedId }}",
                                    this@WatchlistDetailsViewModel::class.simpleName!!
                                )
                                val res = isTrackedShowWatchableUseCase.unwatchable(data.data).filter {
                                    if (!it.isTvShow) {
                                        !it.watchlisted
                                    } else true
                                }
                                updateState(watchlistId, watchlistName, res)
                            }
                        }
                }

                else -> {
                    watchlistsRepository.fetchContent(watchlistId).asSuccess { res ->
                        updateState(watchlistId, watchlistName, res)
                    }
                }
            }
        }
    }

    private fun updateState(watchlistId: Int, watchlistName: String, res: List<TrackedContentApiModel>) {
        shows.update { shows ->
            WatchlistDetailsUiState.Ok(
                watchlistId = watchlistId,
                watchlistName = watchlistName,
                _shows = res.map { mapper.map(it) },
                filterTvShows = true
            )
        }
    }

    fun action(action: WatchlistDetailsAction) {
        when (action) {
            WatchlistDetailsAction.ToggleMovies -> {
                shows.update {
                    if (it is WatchlistDetailsUiState.Ok) {
                        it.copy(filterTvShows = !it.filterTvShows)
                    } else it
                }
            }

            WatchlistDetailsAction.ToggleTvShows -> {
                shows.update {
                    if (it is WatchlistDetailsUiState.Ok) {
                        it.copy(filterTvShows = !it.filterTvShows)
                    } else it
                }
            }

            is WatchlistDetailsAction.Delete -> {
                viewModelScope.launch(ioDispatcher) {
                    watchlistsRepository.deleteList(action.id)
                }
            }

            is WatchlistDetailsAction.Rename -> {
                viewModelScope.launch(ioDispatcher) {
                    watchlistsRepository.renameList(action.id, action.newName)
                }
            }
        }
    }

    sealed class WatchlistDetailsAction {
        data object ToggleTvShows : WatchlistDetailsAction()
        data object ToggleMovies : WatchlistDetailsAction()
        data class Rename(val id: Int, val newName: String) : WatchlistDetailsAction()
        data class Delete(val id: Int) : WatchlistDetailsAction()
    }
}

sealed class WatchlistDetailsUiState {
    data object Loading : WatchlistDetailsUiState()
    data object Error : WatchlistDetailsUiState()
    data class Ok(
        val watchlistName: String,
        val watchlistId: Int,
        val _shows: List<WatchlistDetailsShowUiModel>,
        val filterTvShows: Boolean,
    ) : WatchlistDetailsUiState() {
        val shows: List<WatchlistDetailsShowUiModel>
            get() {
                return _shows.filter { filterTvShows == it.isTvShow }
            }
    }
}

data class WatchlistDetailsShowUiModel(
    val tmdbId: Int,
    val title: String,
    val image: String,
    val status: String,
    val isTvShow: Boolean
)
