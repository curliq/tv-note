package com.free.tvtracker.ui.finished

import com.free.tvtracker.data.tracked.TrackedShowsRepository
import com.free.tvtracker.domain.GetShowsUseCase
import com.free.tvtracker.domain.IsTrackedShowWatchableUseCase
import com.free.tvtracker.expect.ui.ViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class FinishedShowsViewModel(
    private val trackedShowsRepository: TrackedShowsRepository,
    private val getShowsUseCase: GetShowsUseCase,
    private val isTrackedShowWatchableUseCase: IsTrackedShowWatchableUseCase,
    private val mapper: FinishedShowUiModelMapper,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
) : ViewModel() {
    val shows: MutableStateFlow<FinishedUiState> = MutableStateFlow(FinishedUiState.Loading)

    init {
        viewModelScope.launch(ioDispatcher) {
            getShowsUseCase(trackedShowsRepository.finishedShows)
                .filter { it.status.fetched }
                .collect { data ->
                    if (data.status.success) {
                        val res = isTrackedShowWatchableUseCase.unwatchable(data.data).filter {
                            if (!it.isTvShow) {
                                !it.watchlisted
                            } else true
                        }
                        if (res.isEmpty()) {
                            shows.value = FinishedUiState.Empty
                        } else {
                            shows.update {
                                FinishedUiState.Ok(
                                    _shows = res.map(mapper.map()),
                                    (it as? FinishedUiState.Ok)?.filterTvShows ?: true,
                                    (it as? FinishedUiState.Ok)?.filterMovies ?: true
                                )
                            }
                        }
                    } else {
                        shows.value = FinishedUiState.Error
                    }
                }
        }
        refresh()
    }

    fun refresh() {
        shows.value = FinishedUiState.Loading
        viewModelScope.launch(ioDispatcher) {
            trackedShowsRepository.updateFinished()
        }
    }

    fun action(action: FinishedAction) {
        when (action) {
            FinishedAction.ToggleMovies -> {
                shows.update {
                    if (it is FinishedUiState.Ok) {
                        val filterMovies = !it.filterMovies
                        it.copy(
                            filterMovies = filterMovies,
                            filterTvShows = if (!filterMovies) true else it.filterTvShows
                        )
                    } else it
                }
            }

            FinishedAction.ToggleTvShows -> {
                shows.update {
                    if (it is FinishedUiState.Ok) {
                        val filterTvShows = !it.filterTvShows
                        it.copy(
                            filterTvShows = !it.filterTvShows,
                            filterMovies = if (!filterTvShows) true else it.filterMovies
                        )
                    } else it
                }
            }
        }
    }

    sealed class FinishedAction {
        data object ToggleTvShows : FinishedAction()
        data object ToggleMovies : FinishedAction()
    }
}

sealed class FinishedUiState {
    data object Loading : FinishedUiState()
    data object Error : FinishedUiState()
    data object Empty : FinishedUiState()
    data class Ok(
        val _shows: List<FinishedShowUiModel>,
        val filterTvShows: Boolean,
        val filterMovies: Boolean
    ) : FinishedUiState() {
        val shows: List<FinishedShowUiModel>
            get() {
                return _shows.filter {
                    if (filterTvShows && filterMovies) {
                        true
                    } else if (!filterTvShows && filterMovies) {
                        !it.isTvShow
                    } else if (filterTvShows && !filterMovies) {
                        it.isTvShow
                    } else {
                        true
                    }
                }
            }
    }
}

data class FinishedShowUiModel(
    val tmdbId: Int,
    val title: String,
    val image: String,
    val status: String,
    val isTvShow: Boolean
)
