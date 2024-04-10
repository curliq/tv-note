package com.free.tvtracker.screens.search

import com.free.tvtracker.base.ApiError
import com.free.tvtracker.core.ui.ViewModel
import com.free.tvtracker.data.search.SearchRepository
import com.free.tvtracker.data.tracked.TrackedShowsRepository
import com.free.tvtracker.search.response.SearchShowApiModel
import com.free.tvtracker.utils.TmdbConfigData
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch

class AddTrackedViewModel(
    private val searchRepository: SearchRepository,
    private val trackedShowsRepository: TrackedShowsRepository,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : ViewModel() {

    sealed class Action {
        data class AddToTracked(val id: Int) : Action()
        data class SeeDetails(val id: Int) : Action()
    }

    val results: MutableStateFlow<AddTrackedUiState> = MutableStateFlow(AddTrackedUiState.Ok(false, emptyList()))
    val searchQuery: MutableStateFlow<String> = MutableStateFlow("")

    init {
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
                if (results.value is AddTrackedUiState.Ok) {
                    results.value = AddTrackedUiState.Ok(
                        isSearching = false,
                        (results.value as AddTrackedUiState.Ok).data.map {
                            if (it.id == action.id) {
                                it.copy(tracked = true)
                            } else {
                                it
                            }
                        }
                    )
                    viewModelScope.launch(ioDispatcher) {
                        trackedShowsRepository.addTrackedShow(action.id)
                    }
                }
            }
        }
    }

    private suspend fun search(term: String) {
        if (results.value is AddTrackedUiState.Ok) {
            results.value = (results.value as AddTrackedUiState.Ok).copy(isSearching = true)
        } else {
            results.value = AddTrackedUiState.Ok(isSearching = true, emptyList())
        }
        val trackedShows = trackedShowsRepository.getOrUpdateWatchingShows()
        searchRepository.searchTvShows(term)
            .asSuccess { data ->
                results.value = AddTrackedUiState.Ok(
                    isSearching = false,
                    data.shows.map { show ->
                        val tracked = trackedShows.data?.firstOrNull { it.storedShow.tmdbId == show.tmdbId } != null
                        show.toUiModel(tracked)
                    })
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
}

data class AddTrackedItemUiModel(val id: Int, val title: String, val image: String, var tracked: Boolean)

fun SearchShowApiModel.toUiModel(tracked: Boolean): AddTrackedItemUiModel {
    return AddTrackedItemUiModel(
        this.tmdbId,
        this.name,
        TmdbConfigData.get().getPosterUrl(this.posterPath),
        tracked
    )
}
