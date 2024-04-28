package com.free.tvtracker.screens.finished

import com.free.tvtracker.core.ui.ViewModel
import com.free.tvtracker.data.tracked.TrackedShowsRepository
import com.free.tvtracker.domain.IsTrackedShowWatchableUseCase
import com.free.tvtracker.domain.GetShowsUseCase
import com.free.tvtracker.tracked.response.TrackedShowApiModel
import com.free.tvtracker.utils.TmdbConfigData
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class FinishedShowsViewModel(
    private val trackedShowsRepository: TrackedShowsRepository,
    private val getShowsUseCase: GetShowsUseCase,
    private val isTrackedShowWatchableUseCase: IsTrackedShowWatchableUseCase,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
) : ViewModel() {
    val shows: MutableStateFlow<FinishedUiState> = MutableStateFlow(FinishedUiState.Loading)

    init {
        viewModelScope.launch(ioDispatcher) {
            getShowsUseCase(trackedShowsRepository.finishedShows).collect { data ->
                data.data.asSuccess {
                    if (data.data.data.isNullOrEmpty()) {
                        shows.value = FinishedUiState.Empty
                    } else {
                        shows.value = FinishedUiState.Ok(
                            shows = isTrackedShowWatchableUseCase.unwatchable(data.data.data!!).map { it.toUiModel() }
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
            trackedShowsRepository.emitLatestFinished()
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
    val trackedShowId: Int,
    val tmdbId: Int,
    val title: String,
    val image: String,
    val status: String,
    val nextEpisode: String?,
)

fun TrackedShowApiModel.toUiModel(): FinishedShowUiModel {
    return FinishedShowUiModel(
        trackedShowId = this.id,
        tmdbId = this.storedShow.tmdbId,
        title = this.storedShow.title,
        image = TmdbConfigData.get().getPosterUrl(this.storedShow.posterImage),
        nextEpisode = "",
        status = this.storedShow.status
    )
}
