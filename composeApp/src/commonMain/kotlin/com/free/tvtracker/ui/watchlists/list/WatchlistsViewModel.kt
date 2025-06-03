package com.free.tvtracker.ui.watchlists.list

import com.free.tvtracker.core.Logger
import com.free.tvtracker.data.iap.IapRepository
import com.free.tvtracker.data.tracked.TrackedShowsRepository
import com.free.tvtracker.data.watchlists.WatchlistsRepository
import com.free.tvtracker.domain.GetPurchaseStatusUseCase
import com.free.tvtracker.domain.GetShowsUseCase
import com.free.tvtracker.domain.GetWatchlistedShowsUseCase
import com.free.tvtracker.domain.IsTrackedShowWatchableUseCase
import com.free.tvtracker.domain.PurchaseStatus
import com.free.tvtracker.expect.ViewModel
import com.free.tvtracker.tracked.response.TrackedContentApiModel
import com.free.tvtracker.ui.common.TmdbConfigData
import com.free.tvtracker.watchlists.response.WatchlistApiModel.Companion.FINISHED_LIST_ID
import com.free.tvtracker.watchlists.response.WatchlistApiModel.Companion.WATCHLIST_LIST_ID
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.onErrorResume
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class WatchlistsViewModel(
    private val watchlistsRepository: WatchlistsRepository,
    private val purchaseStatus: GetPurchaseStatusUseCase,
    private val trackedShowsRepository: TrackedShowsRepository,
    private val getShowsUseCase: GetShowsUseCase,
    private val isTrackedShowWatchableUseCase: IsTrackedShowWatchableUseCase,
    private val getWatchlistedShowsUseCase: GetWatchlistedShowsUseCase,
    private val iapRepository: IapRepository,
    private val logger: Logger,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
) : ViewModel() {
    val watchlists: MutableStateFlow<WatchlistsUiState> = MutableStateFlow(WatchlistsUiState.Loading)
    val status: Flow<PurchaseStatus> = purchaseStatus.invoke()

    init {
        refresh()
    }

    val TAG = this@WatchlistsViewModel::class.simpleName!!

    fun refresh() {
        watchlists.value = WatchlistsUiState.Loading
        viewModelScope.launch(ioDispatcher) {
            launch {
                trackedShowsRepository.updateWatchlisted()
                getShowsUseCase(trackedShowsRepository.watchlistedShows)
                    .filter { it.status.fetched == true }
                    .collect { data ->
                        logger.d(
                            "watchlist shows updated: ${data.data.map { it.typedId }}",
                            TAG
                        )
                        if (data.status.success) {
                            val res = getWatchlistedShowsUseCase(data.data)
                            watchlists.update {
                                val model = WatchlistsUiModel(
                                    id = WATCHLIST_LIST_ID,
                                    name = "Watchlisted",
                                    thumbnails = getThumbnails(res),
                                    tvShowCount = res.filter { it.isTvShow }.size,
                                    movieCount = res.filter { !it.isTvShow }.size
                                )
                                if (it is WatchlistsUiState.Ok) {
                                    val watchlists = it.watchlists.toMutableList()
                                    val watchlist = it.watchlists.firstOrNull { it.id == WATCHLIST_LIST_ID }
                                    if (watchlist != null) {
                                        watchlists.removeAt(0)
                                        watchlists.add(0, model)
                                        it.copy(watchlists = watchlists)
                                    } else {
                                        watchlists.add(0, model)
                                        it.copy(watchlists = watchlists)
                                    }
                                } else {
                                    WatchlistsUiState.Ok(watchlists = listOf(model))
                                }
                            }
                        }
                    }
            }
            launch {
                trackedShowsRepository.updateFinished()
                getShowsUseCase(trackedShowsRepository.finishedShows)
                    .filter { it.status.fetched == true }
                    .collect { data ->
                        if (data.status.success) {
                            logger.d(
                                "finished shows updated: ${data.data.map { it.typedId }}",
                                TAG
                            )
                            if (data.status.success) {
                                val res = isTrackedShowWatchableUseCase.unwatchable(data.data).filter {
                                    if (!it.isTvShow) {
                                        !it.watchlisted
                                    } else true
                                }
                                watchlists.update {
                                    val model = WatchlistsUiModel(
                                        id = FINISHED_LIST_ID,
                                        name = "Finished",
                                        thumbnails = getThumbnails(res),
                                        tvShowCount = res.filter { it.isTvShow }.size,
                                        movieCount = res.filter { !it.isTvShow }.size
                                    )
                                    if (it is WatchlistsUiState.Ok) {
                                        val watchlists = it.watchlists.toMutableList()
                                        val finishedIndex = it.watchlists.indexOfFirst { it.id == FINISHED_LIST_ID }
                                        if (finishedIndex != -1) {
                                            watchlists.removeAt(finishedIndex)
                                            watchlists.add(finishedIndex, model)
                                            it.copy(watchlists = watchlists)
                                        } else {
                                            val watchlistIndex =
                                                it.watchlists.indexOfFirst { it.id == WATCHLIST_LIST_ID }
                                            // add finished after watchlisted
                                            if (watchlistIndex != -1) {
                                                watchlists.add(1, model)
                                            } else {
                                                watchlists.add(0, model)
                                            }
                                            it.copy(watchlists = watchlists)
                                        }

                                    } else {
                                        WatchlistsUiState.Ok(watchlists = listOf(model))
                                    }
                                }
                            }
                        }
                    }
            }
            watchlistsRepository.fetch()
                .catch {
                    watchlists.update { WatchlistsUiState.Error }
                }
                .collect { value ->
                    value.asSuccess { data ->
                        watchlists.update {
                            val lists = data.map {
                                WatchlistsUiModel(
                                    id = it.id,
                                    name = it.name,
                                    thumbnails = emptyList(),
                                    tvShowCount = it.showsCount,
                                    movieCount = it.moviesCount
                                )
                            }
                            if (it is WatchlistsUiState.Ok) {
                                val watchlists = it.watchlists.toMutableList()
                                watchlists.removeAll { it.id !in listOf(WATCHLIST_LIST_ID, FINISHED_LIST_ID) }
                                watchlists.addAll(lists)
                                it.copy(watchlists = watchlists)
                            } else {
                                WatchlistsUiState.Ok(watchlists = lists)
                            }
                        }
                        data.forEach { watchlistApiModel ->
                            viewModelScope.launch(ioDispatcher) {
                                watchlistsRepository.fetchContent(watchlistId = watchlistApiModel.id)
                                    .asSuccess { content ->
                                        logger.d("custom lists: $content", TAG)
                                        watchlists.update {
                                            if (it is WatchlistsUiState.Ok) {
                                                it.watchlists.find { it.id == watchlistApiModel.id }?.thumbnails =
                                                    getThumbnails(content)
                                                it.copy(watchlists = it.watchlists)
                                            } else it
                                        }
                                    }
                            }
                        }
                    }
                    value.asError {
                        watchlists.update { WatchlistsUiState.Error }
                    }
                }
        }
    }

    private fun getThumbnails(content: List<TrackedContentApiModel>): List<String> {
        return content.map {
            TmdbConfigData.get().getBackdropUrl(
                (it.tvShow?.storedShow?.backdropImage
                    ?: it.movie?.storedMovie?.backdropImage ?: "")
            )
        }
    }

    fun action(action: WatchlistsAction) {
        when (action) {
            WatchlistsAction.Buy -> {
                viewModelScope.launch(ioDispatcher) {
                    iapRepository.purchase()
                }
            }

            WatchlistsAction.Sub -> {
                viewModelScope.launch(ioDispatcher) {
                    iapRepository.subscribe()
                }
            }
        }
    }

    sealed class WatchlistsAction {
        data object Buy : WatchlistsAction()
        data object Sub : WatchlistsAction()
    }
}

sealed class WatchlistsUiState {
    data object Loading : WatchlistsUiState()
    data object Error : WatchlistsUiState()
    data object Empty : WatchlistsUiState()
    data class Ok(val watchlists: List<WatchlistsUiModel>) : WatchlistsUiState()
}

data class WatchlistsUiModel(
    val id: Int,
    val name: String,
    var thumbnails: List<String>,
    val tvShowCount: Int,
    val movieCount: Int
)
