package com.free.tvtracker.ui.watchlists.details

import com.free.tvtracker.core.Logger
import com.free.tvtracker.data.tracked.TrackedShowsRepository
import com.free.tvtracker.data.watchlists.WatchlistsRepository
import com.free.tvtracker.domain.GetShowsUseCase
import com.free.tvtracker.domain.GetWatchlistedShowsUseCase
import com.free.tvtracker.domain.IsTrackedShowWatchableUseCase
import com.free.tvtracker.expect.ViewModel
import com.free.tvtracker.watchlists.response.WatchlistApiModel.Companion.FINISHED_LIST_ID
import com.free.tvtracker.watchlists.response.WatchlistApiModel.Companion.WATCHLIST_LIST_ID
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
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

    val loadContent: MutableStateFlow<LoadContent> = MutableStateFlow(LoadContent(-1, ""))

    fun refresh() {
        viewModelScope.launch(ioDispatcher) {
            watchlistsRepository.fetchContent(loadContent.value.watchlistId)
        }
    }

    fun loadContent(watchlistId: Int, watchlistName: String) {
        loadContent.value = LoadContent(watchlistId, watchlistName)
    }

    val filterFlow = MutableStateFlow(true)

    @OptIn(ExperimentalCoroutinesApi::class)
    val loadContentFlow2: Flow<WatchlistDetailsUiState> =
        loadContent.flatMapLatest { loadContentValue ->
                when (loadContentValue.watchlistId) {
                    FINISHED_LIST_ID -> {
                        getShowsUseCase(trackedShowsRepository.finishedShows)
                            .filter { it.status.fetched == true }
                            .map { data ->
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
                                    WatchlistDetailsUiState.Ok(
                                        watchlistId = loadContentValue.watchlistId,
                                        watchlistName = loadContentValue.watchlistName,
                                        _shows = res.map { mapper.map(it) },
                                        filterTvShows = true
                                    )
                                } else {
                                    WatchlistDetailsUiState.Error
                                }
                            }
                    }
                    WATCHLIST_LIST_ID -> {
                        getShowsUseCase(trackedShowsRepository.watchlistedShows)
                            .filter { it.status.fetched == true }
                            .map { data ->
                                if (data.status.success) {
                                    logger.d(
                                        "watchlist shows updated: ${data.data.map { it.typedId }}",
                                        this@WatchlistDetailsViewModel::class.simpleName!!
                                    )
                                    val res = getWatchlistedShowsUseCase(data.data)
                                    WatchlistDetailsUiState.Ok(
                                        watchlistId = loadContentValue.watchlistId,
                                        watchlistName = loadContentValue.watchlistName,
                                        _shows = res.map { mapper.map(it) },
                                        filterTvShows = true
                                    )
                                } else {
                                    WatchlistDetailsUiState.Error
                                }
                            }
                    }
                    else -> {
                        watchlistsRepository.watchlistsContent
                            .map { it.map }
                            .map { it[loadContentValue.watchlistId] }
                            .filterNotNull()
                            .map {
                                logger.d("collect watchlist $it", "WatchlistDetailsViewModel")
                                if (it.data != null) {
                                    WatchlistDetailsUiState.Ok(
                                        watchlistId = loadContentValue.watchlistId,
                                        watchlistName = loadContentValue.watchlistName,
                                        _shows = it.data!!.map { mapper.map(it) },
                                        filterTvShows = true
                                    )
                                } else {
                                    WatchlistDetailsUiState.Error
                                }
                            }
                    }
                }.map {
                    if (loadContentValue.watchlistId == -1) {
                        WatchlistDetailsUiState.Error
                    } else {
                        it
                    }
                }
            }
            .combine(filterFlow) { uiState, filter ->
                if (uiState is WatchlistDetailsUiState.Ok) {
                    uiState.copy(filterTvShows = filter)
                } else {
                    uiState
                }
            }
            .onStart {
                emit(WatchlistDetailsUiState.Loading)
            }

    fun action(action: WatchlistDetailsAction) {
        when (action) {
            WatchlistDetailsAction.ToggleMovies -> {
                filterFlow.value = !filterFlow.value
            }

            WatchlistDetailsAction.ToggleTvShows -> {
                filterFlow.value = !filterFlow.value
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
