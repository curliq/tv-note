package com.free.tvtracker.ui.finished

import com.free.tvtracker.expect.ui.ViewModel
import com.free.tvtracker.data.tracked.TrackedShowsRepository
import com.free.tvtracker.domain.GetShowsUseCase
import com.free.tvtracker.domain.IsTrackedShowWatchableUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
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
            getShowsUseCase(trackedShowsRepository.finishedShows).collect { data ->
                data.data.asSuccess {
                    val res = isTrackedShowWatchableUseCase.unwatchable(data.data.data ?: emptyList())
                    if (res.isEmpty()) {
                        shows.value = FinishedUiState.Empty
                    } else {
                        shows.value = FinishedUiState.Ok(
                            shows = res.map(mapper.map())
                        )
                    }
                }
                data.data.asError {
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
}

sealed class FinishedUiState {
    data object Loading : FinishedUiState()
    data object Error : FinishedUiState()
    data object Empty : FinishedUiState()
    data class Ok(val shows: List<FinishedShowUiModel>) : FinishedUiState()
}

data class FinishedShowUiModel(
    val tmdbId: Int,
    val title: String,
    val image: String,
    val status: String,
)
