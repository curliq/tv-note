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
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
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

    companion object {
        const val WATCHLIST = "Watchlisted"
        const val FINISHED = "Finished"
    }

    val watchlists: MutableStateFlow<WatchlistsUiState> = MutableStateFlow(WatchlistsUiState.Loading)
    val status: Flow<PurchaseStatus> = purchaseStatus.invoke()

    val TAG = this@WatchlistsViewModel::class.simpleName!!

    fun fetch() {
        viewModelScope.launch {
            trackedShowsRepository.updateWatchlisted()
            trackedShowsRepository.updateFinished()
            watchlistsRepository.fetch()
        }
    }

    val stateAsFlow: Flow<WatchlistsUiState> =
        getShowsUseCase(trackedShowsRepository.watchlistedShows)
            .filter { it.status.fetched == true }
            .map { data ->
                logger.d(
                    "watchlist shows updated: ${data.data.map { it.typedId }}",
                    TAG
                )
                if (data.status.success) {
                    val res = getWatchlistedShowsUseCase(data.data)
                    val model = WatchlistsUiModel(
                        id = WATCHLIST_LIST_ID,
                        name = WATCHLIST,
                        thumbnails = getThumbnails(res),
                        tvShowCount = res.filter { it.isTvShow }.size,
                        movieCount = res.filter { !it.isTvShow }.size
                    )

                    WatchlistsUiState.Ok(watchlists = listOf(model))
                } else {
                    WatchlistsUiState.Error
                }
            }
            .combine(getShowsUseCase(trackedShowsRepository.finishedShows).filter { it.status.fetched == true }) { uiState, finished ->
                logger.d(
                    "finished shows updated: ${finished.data.map { it.typedId }}",
                    TAG
                )
                if (finished.status.success) {
                    val res = isTrackedShowWatchableUseCase.unwatchable(finished.data).filter {
                        if (!it.isTvShow) {
                            !it.watchlisted
                        } else true
                    }

                    val model = WatchlistsUiModel(
                        id = FINISHED_LIST_ID,
                        name = FINISHED,
                        thumbnails = getThumbnails(res),
                        tvShowCount = res.filter { it.isTvShow }.size,
                        movieCount = res.filter { !it.isTvShow }.size
                    )
                    if (uiState is WatchlistsUiState.Ok) {
                        val watchlists = uiState.watchlists.toMutableList()
                        val finishedIndex = uiState.watchlists.indexOfFirst { it.id == FINISHED_LIST_ID }
                        // check if finished already exists and update it
                        if (finishedIndex != -1) {
                            watchlists.removeAt(finishedIndex)
                            watchlists.add(finishedIndex, model)
                            uiState.copy(watchlists = watchlists)
                        } else {
                            val watchlistIndex =
                                uiState.watchlists.indexOfFirst { it.id == WATCHLIST_LIST_ID }
                            // add finished after watchlisted
                            if (watchlistIndex != -1) {
                                watchlists.add(1, model)
                            } else {
                                watchlists.add(0, model)
                            }
                            uiState.copy(watchlists = watchlists)
                        }
                    } else {
                        WatchlistsUiState.Ok(watchlists = listOf(model))
                    }
                } else {
                    uiState
                }
            }.combine(watchlistsRepository.watchlists) { uiState, watchlists ->
                if (watchlists.isSuccess()) {
                    watchlists.data!!.forEach { watchlistApiModel ->
                        viewModelScope.launch(ioDispatcher) {
                            watchlistsRepository.fetchContent(watchlistId = watchlistApiModel.id)
                        }
                    }
                    val lists = watchlists.data!!.map {
                        WatchlistsUiModel(
                            id = it.id,
                            name = it.name,
                            thumbnails = emptyList(),
                            tvShowCount = it.showsCount,
                            movieCount = it.moviesCount
                        )
                    }

                    if (uiState is WatchlistsUiState.Ok) {
                        val watchlists = uiState.watchlists.toMutableList()
                        watchlists.removeAll { it.id !in listOf(WATCHLIST_LIST_ID, FINISHED_LIST_ID) }
                        watchlists.addAll(lists)
                        uiState.copy(watchlists = watchlists)
                    } else {
                        WatchlistsUiState.Ok(watchlists = lists)
                    }
                } else {
                    WatchlistsUiState.Error
                }
            }.combine(
                watchlistsRepository.watchlistsContent.filterNotNull().map { it.map }
            ) { uiState, watchlistContent ->
                var watchlists = (uiState as? WatchlistsUiState.Ok)?.watchlists ?: emptyList()
                watchlistContent.forEach { map ->
                    map.value.asSuccess { content ->
                        logger.d("custom lists, id: ${map.key}: $content", TAG)
                        val thumbnails = getThumbnails(content)
                        logger.d("id: ${map.key}, thumbnails: $thumbnails", TAG)
                        watchlists.find { it.id == map.key }?.thumbnails = thumbnails
                    }
                }
                if (uiState is WatchlistsUiState.Ok) {
                    uiState.copy(watchlists = watchlists)
                } else {
                    uiState
                }
            }.onStart { emit(WatchlistsUiState.Loading) }

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

            is WatchlistsAction.New -> {
                viewModelScope.launch(ioDispatcher) {
                    watchlistsRepository.createList(action.name)
                }
            }
        }
    }

    sealed class WatchlistsAction {
        data object Buy : WatchlistsAction()
        data object Sub : WatchlistsAction()
        data class New(val name: String) : WatchlistsAction()
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
