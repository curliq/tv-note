package com.free.tvtracker.ui.watching

import com.free.tvtracker.core.Logger
import com.free.tvtracker.data.iap.IapRepository
import com.free.tvtracker.data.tracked.MarkEpisodeWatched
import com.free.tvtracker.data.tracked.TrackedShowsRepository
import com.free.tvtracker.domain.GetPurchaseStatusUseCase
import com.free.tvtracker.domain.IsTrackedShowWatchableUseCase
import com.free.tvtracker.domain.PurchaseStatus
import com.free.tvtracker.expect.ViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class WatchingViewModel(
    private val trackedShowsRepository: TrackedShowsRepository,
    private val getWatchingShows: GetWatchingShowsUseCase,
    private val isTrackedShowWatchableUseCase: IsTrackedShowWatchableUseCase,
    private val watchingShowUiModelMapper: WatchingShowUiModelMapper,
    private val purchaseStatus: GetPurchaseStatusUseCase,
    private val iapRepository: IapRepository,
    val logger: Logger,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
) : ViewModel() {

    val shows: MutableStateFlow<WatchingUiState> = MutableStateFlow(WatchingUiState.Loading)
    val status: Flow<PurchaseStatus> = purchaseStatus.invoke()

    init {
        viewModelScope.launch(ioDispatcher) {
            getWatchingShows().collect { data ->
                if (data.status.fetched == null) {
                    shows.value = WatchingUiState.Loading
                } else if (data.status.success) {
                    logger.d(
                        "watching shows updated: ${data.data.map { it.typedId }}",
                        this@WatchingViewModel::class.simpleName!!
                    )
                    if (data.data.isEmpty()) {
                        shows.value = WatchingUiState.Empty
                    } else {
                        shows.value = WatchingUiState.Ok(
                            watching = isTrackedShowWatchableUseCase.canWatchNow(data.data)
                                .map(watchingShowUiModelMapper.map()),
                            waitingNextEpisode = isTrackedShowWatchableUseCase.canWatchSoon(data.data)
                                .map(watchingShowUiModelMapper.map())
                        )
                    }
                } else {
                    logger.d("watching shows error updating")
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

            // fetch everything so we can check if something is tracked in the search screen
            trackedShowsRepository.updateFinished(forceUpdate = false)
            trackedShowsRepository.updateWatchlisted(forceUpdate = false)
        }
    }

    fun markEpisodeWatched(showId: Int?, episodeId: Int?) {
        viewModelScope.launch(ioDispatcher) {
            episodeId?.let {
                trackedShowsRepository.markEpisodeAsWatched(listOf(MarkEpisodeWatched(showId!!, it)))
            }
        }
    }

    val toaster: MutableSharedFlow<String?> = MutableSharedFlow()

    fun onBuy() {
        viewModelScope.launch(ioDispatcher) {
            if (!iapRepository.purchase()) {
                toaster.emit("Error completing purchase, try again later.")
            }
        }
    }

    fun onSub() {
        viewModelScope.launch(ioDispatcher) {
            if (!iapRepository.subscribe()) {
                toaster.emit("Error completing subscription, try again later.")
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
