package com.free.tvtracker.ui.search

import com.free.tvtracker.base.ApiError
import com.free.tvtracker.expect.ui.ViewModel
import com.free.tvtracker.data.search.SearchRepository
import com.free.tvtracker.data.tracked.TrackedShowsRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AddTrackedViewModel(
    private val searchRepository: SearchRepository,
    private val trackedShowsRepository: TrackedShowsRepository,
    private val mapper: ShowSearchUiModelMapper,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : ViewModel() {

    sealed class Action {
        data class AddToTracked(val id: Int, val originScreen: AddTrackedScreenOriginScreen) : Action()
        data class SeeDetails(val id: Int) : Action()
    }

    val results: MutableStateFlow<AddTrackedUiState> = MutableStateFlow(AddTrackedUiState.Ok(false, emptyList()))
    val searchQuery: MutableStateFlow<String> = MutableStateFlow("")
    val focusSearch: MutableStateFlow<Boolean> = MutableStateFlow(false)

    fun clearFocus() {
        focusSearch.value = false
    }

    init {
        viewModelScope.launch(ioDispatcher) {
            trackedShowsRepository.allShows.collect { shows ->
                results.update {
                    if (it is AddTrackedUiState.Ok) {
                        // This can only be false if user tracks from the pdp, goes back to search, starts searching,
                        // and the api result comes back while search is going.
                        // To fix: check latest tracked shows on search result too
                        AddTrackedUiState.Ok(
                            isSearching = false,
                            data = it.data.map { show ->
                                if (shows.map { it.storedShow.tmdbId }.contains(show.tmdbId)) {
                                    show.copy(tracked = true)
                                } else {
                                    show
                                }
                            }
                        )
                    } else {
                        it
                        // todo: track event
                    }
                }
            }
        }
        viewModelScope.launch(ioDispatcher) {
            searchQuery.filter { it.isNotEmpty() }.collectLatest { term ->
                delay(250)
                search(term)
            }
        }
    }

    fun setSearchQuery(query: String) {
        searchQuery.value = query
    }

    fun searchRefresh() {
        viewModelScope.launch(ioDispatcher) {
            search(searchQuery.value)
        }
    }

    fun action(action: Action) {
        when (action) {
            is Action.SeeDetails -> {}
            is Action.AddToTracked -> {
                results.update {
                    if (it is AddTrackedUiState.Ok) {
                        it.copy(
                            isSearching = false,
                            data = it.data.map { show ->
                                if (show.tmdbId == action.id) {
                                    show.copy(tracked = true)
                                } else {
                                    show
                                }
                            }
                        )
                    } else it
                }
                viewModelScope.launch(ioDispatcher) {
                    when (action.originScreen) {
                        AddTrackedScreenOriginScreen.Watching, AddTrackedScreenOriginScreen.Finished -> {
                            trackedShowsRepository.addTrackedShow(action.id, watchlisted = false)
                        }

                        AddTrackedScreenOriginScreen.Watchlist -> {
                            trackedShowsRepository.addTrackedShow(action.id, watchlisted = true)
                        }

                        AddTrackedScreenOriginScreen.Discover -> { }
                    }
                }
                setSearchQuery("")
            }
        }
    }

    private suspend fun search(term: String) {
        when (results.value) {
            is AddTrackedUiState.Ok -> {
                results.update { (it as AddTrackedUiState.Ok).copy(isSearching = true) }
            }

            is AddTrackedUiState.Empty -> {
                results.update { (it as AddTrackedUiState.Empty).copy(isSearching = true) }
            }

            else -> {
                results.update { AddTrackedUiState.Ok(isSearching = true, emptyList()) }
            }
        }
        val trackedShows = trackedShowsRepository.getWatchingShows()
        searchRepository.searchTvShows(term)
            .asSuccess { data ->
                if (data.shows.isEmpty()) {
                    results.value = AddTrackedUiState.Empty(isSearching = false)
                } else {
                    results.value = AddTrackedUiState.Ok(
                        isSearching = false,
                        data.shows.map { show ->
                            val tracked = trackedShows.firstOrNull { it.storedShow.tmdbId == show.tmdbId } != null
                            mapper.map(show, tracked)
                        })
                }
            }
            .asError { error ->
                if (error.code != ApiError.Cancelled.code) {
                    results.value = AddTrackedUiState.Error
                }
            }
    }
}

sealed class AddTrackedUiState {
    data object Error : AddTrackedUiState()
    data class Ok(val isSearching: Boolean, val data: List<AddTrackedItemUiModel>) : AddTrackedUiState()
    data class Empty(val isSearching: Boolean) : AddTrackedUiState()
}

data class AddTrackedItemUiModel(val tmdbId: Int, val title: String, val image: String, val tracked: Boolean)
