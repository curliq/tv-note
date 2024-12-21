package com.free.tvtracker.ui.finished

import com.free.tvtracker.core.Logger
import com.free.tvtracker.data.iap.IapRepository
import com.free.tvtracker.data.tracked.TrackedShowsRepository
import com.free.tvtracker.domain.GetPurchaseStatusUseCase
import com.free.tvtracker.domain.GetShowsUseCase
import com.free.tvtracker.domain.IsTrackedShowWatchableUseCase
import com.free.tvtracker.domain.PurchaseStatus
import com.free.tvtracker.expect.ViewModel
import com.free.tvtracker.ui.watchlist.WatchlistShowUiModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class FinishedShowsViewModel(
    private val trackedShowsRepository: TrackedShowsRepository,
    private val getShowsUseCase: GetShowsUseCase,
    private val isTrackedShowWatchableUseCase: IsTrackedShowWatchableUseCase,
    private val mapper: FinishedShowUiModelMapper,
    private val purchaseStatus: GetPurchaseStatusUseCase,
    private val iapRepository: IapRepository,
    private val logger: Logger,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
) : ViewModel() {
    val shows: MutableStateFlow<FinishedUiState> = MutableStateFlow(FinishedUiState.Loading)
    val status: Flow<PurchaseStatus> = purchaseStatus.invoke()

    init {
        viewModelScope.launch(ioDispatcher) {
            getShowsUseCase(trackedShowsRepository.finishedShows)
                .filter { it.status.fetched == true }
                .collect { data ->
                    if (data.status.success) {
                        logger.d(
                            "finished shows updated: ${data.data.map { it.typedId }}",
                            this@FinishedShowsViewModel::class.simpleName!!
                        )
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
                        it.copy(filterTvShows = !it.filterTvShows)
                    } else it
                }
            }

            FinishedAction.ToggleTvShows -> {
                shows.update {
                    if (it is FinishedUiState.Ok) {
                        it.copy(filterTvShows = !it.filterTvShows)
                    } else it
                }
            }

            FinishedAction.Buy -> {
                viewModelScope.launch(ioDispatcher) {
                    iapRepository.purchase()
                }
            }

            FinishedAction.Sub -> {
                viewModelScope.launch(ioDispatcher) {
                    iapRepository.subscribe()
                }
            }
        }
    }

    sealed class FinishedAction {
        data object ToggleTvShows : FinishedAction()
        data object ToggleMovies : FinishedAction()
        data object Buy : FinishedAction()
        data object Sub : FinishedAction()
    }
}

sealed class FinishedUiState {
    data object Loading : FinishedUiState()
    data object Error : FinishedUiState()
    data object Empty : FinishedUiState()
    data class Ok(
        val _shows: List<FinishedShowUiModel>,
        val filterTvShows: Boolean,
    ) : FinishedUiState() {
        val shows: List<FinishedShowUiModel>
            get() {
                return _shows.filter { filterTvShows == it.isTvShow }
            }
    }
}

data class FinishedShowUiModel(
    val tmdbId: Int,
    val title: String,
    val image: String,
    val status: String,
    val isTvShow: Boolean
) {
    fun toWatchlistUiModel(): WatchlistShowUiModel {
        return WatchlistShowUiModel(
            tmdbId = this.tmdbId,
            title = this.title,
            image = this.image,
            status = this.status,
            isTvShow = this.isTvShow
        )
    }
}
