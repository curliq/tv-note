package com.free.tvtracker.screens.details

import com.free.tvtracker.core.ui.ViewModel
import com.free.tvtracker.data.search.SearchRepository
import com.free.tvtracker.data.tracked.MarkEpisodeWatched
import com.free.tvtracker.data.tracked.TrackedShowsRepository
import com.free.tvtracker.discover.response.TmdbShowDetailsApiModel
import com.free.tvtracker.domain.GetTrackedShowUseCase
import com.free.tvtracker.screens.details.mappers.ShowUiModelMapper
import com.free.tvtracker.utils.TmdbConfigData
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.onEmpty
import kotlinx.coroutines.launch

class DetailsViewModel(
    private val searchRepository: SearchRepository,
    private val showUiModelMapper: ShowUiModelMapper,
    private val trackedShowsRepository: TrackedShowsRepository,
    private val getTrackedShowUseCase: GetTrackedShowUseCase,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
) : ViewModel() {

    val result: MutableStateFlow<DetailsUiState> = MutableStateFlow(DetailsUiState.Loading)

    fun setId(tmdbShowId: Int) {
        result.value = DetailsUiState.Loading
        viewModelScope.launch(ioDispatcher) {
            searchRepository.getShow(tmdbShowId)
                .coAsSuccess { data ->
                    getTrackedShowUseCase.invoke(tmdbShowId).onEmpty {
                        result.value = DetailsUiState.Ok(showUiModelMapper.map(data, null))
                    }.collect { trackedShow ->
                        result.value = DetailsUiState.Ok(showUiModelMapper.map(data, trackedShow))
                    }
                }.asError {
                    result.value = DetailsUiState.Error
                }
        }
    }

    fun action(action: DetailsAction) {
        when (action) {
            is DetailsAction.MarkSeasonWatched -> {
                val trackedShow = trackedShowsRepository.getShowByTmdbId(action.tmdbShowId) ?: return
                val season =
                    (result.value as DetailsUiState.Ok).data.seasons?.firstOrNull { it.seasonId == action.seasonId }
                if (season == null) return
                viewModelScope.launch(ioDispatcher) {
                    val episodes = season.episodes
                        .filter { !it.watched }
                        .filter { it.isWatchable }
                        .map { MarkEpisodeWatched(trackedShow.id, it.id) }
                    trackedShowsRepository.markEpisodeAsWatched(episodes)
                }
            }
        }
    }

    sealed class DetailsAction {
        data class MarkSeasonWatched(val seasonId: Int, val tmdbShowId: Int) : DetailsAction()
    }
}

sealed class DetailsUiState {
    data object Loading : DetailsUiState()
    data object Error : DetailsUiState()
    data class Ok(val data: DetailsUiModel) : DetailsUiState()
}

data class DetailsUiModel(
    val tmdbId: Int,
    val name: String,
    val posterUrl: String,
    val releaseStatus: String,
    val trackingStatus: String,
    val description: String?,
    val seasonsInfo: String?,
    val seasons: List<Season>?,
    val castFirst: Cast?,
    val castSecond: Cast?,
    val watchProviders: List<WatchProvider>,
) {
    data class Cast(val irlName: String, val characterName: String, val photo: String)

    data class Season(
        val seasonId: Int,
        val tmdbShowId: Int,
        val seasonTitle: String,
        val watched: Boolean,
        val isWatchable: Boolean,
        val episodes: List<Episode>
    ) {
        data class Episode(
            val id: Int,
            val thumbnail: String,
            val number: String,
            val name: String,
            val releaseDate: String,
            val watched: Boolean,
            val isWatchable: Boolean,
        )
    }

    data class WatchProvider(val logo: String, val deeplink: String) {
        companion object {
            fun TmdbShowDetailsApiModel.WatchProvider.toUiModel(): WatchProvider {
                return WatchProvider(
                    logo = TmdbConfigData.get().getLogoUrl(this.logoPath ?: ""),
                    deeplink = this.providerName ?: ""
                )
            }
        }
    }
}
