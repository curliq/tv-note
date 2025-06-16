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
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
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

    val stateAsFlow: Flow<WatchlistsUiState> = combine(
        getShowsUseCase(trackedShowsRepository.watchlistedShows).filter { it.status.fetched == true },
        getShowsUseCase(trackedShowsRepository.finishedShows).filter { it.status.fetched == true },
        watchlistsRepository.watchlists,
        watchlistsRepository.watchlistsContent
    ) { watchlistedShows, finishedShows, watchlists, watchlistContent ->
        logger.d(
            "watchlisted shows updated: ${watchlistedShows.data.map { it.typedId }}",
            TAG
        )

        // Handle watchlisted shows
        if (!watchlistedShows.status.success) return@combine WatchlistsUiState.Error

        val watchlistedShowsModel = run {
            val res = getWatchlistedShowsUseCase(watchlistedShows.data)
            WatchlistsUiModel(
                id = WATCHLIST_LIST_ID,
                name = WATCHLIST,
                thumbnails = getThumbnails(res),
                tvShowCount = res.filter { it.isTvShow }.size,
                movieCount = res.filter { !it.isTvShow }.size
            )
        }

        // Handle finished shows
        logger.d(
            "finished shows updated: ${finishedShows.data.map { it.typedId }}",
            TAG
        )
        if (!finishedShows.status.success) return@combine WatchlistsUiState.Error

        val finishedShowsModel = run {
            val res = isTrackedShowWatchableUseCase.unwatchable(finishedShows.data).filter {
                if (!it.isTvShow) {
                    !it.watchlisted
                } else true
            }
            WatchlistsUiModel(
                id = FINISHED_LIST_ID,
                name = FINISHED,
                thumbnails = getThumbnails(res),
                tvShowCount = res.filter { it.isTvShow }.size,
                movieCount = res.filter { !it.isTvShow }.size
            )
        }

        // Handle watchlists
        logger.d(
            "watchlists updated: ${watchlists.data.orEmpty().map { "${it.id}, ${it.name}, ${it.showsCount}" }}",
            TAG
        )
        if (!watchlists.isSuccess()) return@combine WatchlistsUiState.Error

        watchlists.data!!.forEach { watchlistApiModel ->
            viewModelScope.launch(ioDispatcher) {
                watchlistsRepository.fetchContent(watchlistId = watchlistApiModel.id)
            }
        }

        val customLists = watchlists.data!!.map {
            WatchlistsUiModel(
                id = it.id,
                name = it.name,
                thumbnails = emptyList(),
                tvShowCount = it.showsCount,
                movieCount = it.moviesCount
            )
        }

        // Combine all lists and update thumbnails from watchlistContent
        val allWatchlists = mutableListOf(watchlistedShowsModel, finishedShowsModel).apply {
            addAll(customLists)
        }

        // Update thumbnails from watchlistContent
        logger.d(
            "watchlistcontent updated: $watchlistContent",
            TAG
        )
        watchlistContent.map.forEach { (key, value) ->
            value.asSuccess { content ->
                logger.d("custom lists, id: $key: $content", TAG)
                val thumbnails = getThumbnails(content)
                logger.d("id: $key, thumbnails: $thumbnails", TAG)
                allWatchlists.find { it.id == key }?.thumbnails = thumbnails
            }
        }

        WatchlistsUiState.Ok(watchlists = allWatchlists)
    }
        .onStart { emit(WatchlistsUiState.Loading) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = WatchlistsUiState.Loading
        )


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
