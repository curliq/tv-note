package com.free.tvtracker.ui.discover

import com.free.tvtracker.expect.ui.ViewModel
import com.free.tvtracker.data.search.SearchRepository
import com.free.tvtracker.data.tracked.TrackedShowsRepository
import com.free.tvtracker.discover.response.RecommendedContentApiResponse
import com.free.tvtracker.expect.CommonStringUtils
import com.free.tvtracker.ui.common.TmdbConfigData
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.onEmpty
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DiscoverViewModel(
    private val searchRepository: SearchRepository,
    private val trackedShowsRepository: TrackedShowsRepository,
    private val trendingMapper: DiscoverShowUiModelMapper,
    private val recommendedMapper: RecommendedShowUiModelMapper,
    private val stringUtils: CommonStringUtils = CommonStringUtils(),
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
) : ViewModel() {

    sealed class DiscoverViewModelAction {
        data class RecommendedSelectionAdded(val selectedTmdbId: Int) : DiscoverViewModelAction()
        data object RecommendedSelectionClear : DiscoverViewModelAction()
        data object RecommendedUpdate : DiscoverViewModelAction()
    }

    val uiModel: MutableStateFlow<DiscoverUiState> = MutableStateFlow(DiscoverUiState.Loading)

    init {
        refresh(showLoading = true)
    }

    fun refresh(showLoading: Boolean = false) {
        if (showLoading) {
            uiModel.value = DiscoverUiState.Loading
        }
        viewModelScope.launch(ioDispatcher) {
            val trending = async { searchRepository.getTrendingWeekly() }
            val releases = async { searchRepository.getNewEpisodeReleasedSoon() }
            val rec = async { getRecommended() }
            val trendingRes = trending.await()
            val releasesRes = releases.await()
            val recRes = rec.await()

            if (trendingRes.isError() && releasesRes.isError() && recRes.isError()) {
                uiModel.value = DiscoverUiState.Error
            } else {
                val selectionActive = recRes.data?.relatedContent?.map {
                    val show = trackedShowsRepository.getShowByTmdbId(it.tmdbId)
                    DiscoverUiModel.Content(
                        it.tmdbId,
                        show?.storedShow?.title ?: "",
                        TmdbConfigData.get().getPosterUrl(show?.storedShow?.posterImage)
                    )
                } ?: emptyList()

                // Why not `stateIn()`? because this viewmodel doesn't have a lifecycleScope because it's a singleton,
                // why is it a singleton? because it needs to be shared between the discover and recommended activities
                trackedShowsRepository.allShows.onEmpty {
                    uiModel.value = DiscoverUiState.Error
                }.collect { allShows ->
                    val uiModel = DiscoverUiState.Ok(
                        DiscoverUiModel(
                            showsTrendingWeekly = trendingRes.data?.results?.map(trendingMapper.map()) ?: emptyList(),
                            showsReleasedSoon = releasesRes.data?.results?.map(trendingMapper.map()) ?: emptyList(),
                            showsRecommended = DiscoverUiModel.RecommendedContent(
                                selectionActiveText = stringUtils.listToString(selectionActive.map { it.title }),
                                selectionAvailable = allShows.map { show ->
                                    DiscoverUiModel.RecommendedContent.Selection(
                                        show.storedShow.tmdbId,
                                        show.storedShow.title,
                                        TmdbConfigData.get().getPosterUrl(show.storedShow.posterImage),
                                        isSelected = selectionActive
                                            .find { it.tmdbId == show.storedShow.tmdbId } != null
                                    )
                                },
                                selectionActive = selectionActive,
                                results = recRes.data?.results?.map(recommendedMapper.map()) ?: emptyList(),
                                isLoading = false
                            )
                        ),
                    )
                    this@DiscoverViewModel.uiModel.value = uiModel
                }
            }
        }
    }

    private suspend fun getRecommended(): RecommendedContentApiResponse {
        trackedShowsRepository.updateWatching(forceUpdate = false)
        trackedShowsRepository.updateFinished(forceUpdate = false)
        trackedShowsRepository.updateWatchlisted(forceUpdate = false)
        val shows =
            (uiModel.value as? DiscoverUiState.Ok)
                ?.uiModel?.showsRecommended?.selectionAvailable
                ?.filter { it.isSelected }
                ?.map { it.tmdbId }
                ?: getDefaultRecommendedSelection()
        return searchRepository.getRecommended(shows)
    }

    private fun getDefaultRecommendedSelection(): List<Int> {
        val shows = trackedShowsRepository.allShows.value
        val related = shows.sortedByDescending { it.createdAtDatetime }.take(2)
        return related.map { it.storedShow.tmdbId }
    }

    fun action(action: DiscoverViewModelAction) {
        when (action) {
            is DiscoverViewModelAction.RecommendedSelectionAdded -> {
                uiModel.update {
                    // yes all this code just to make one flag true, such are the banes of a single UI model object
                    if (it is DiscoverUiState.Ok) {
                        it.copy(
                            uiModel = it.uiModel.copy(
                                showsRecommended = it.uiModel.showsRecommended.copy(
                                    selectionAvailable = it.uiModel.showsRecommended.selectionAvailable.map {
                                        if (it.tmdbId == action.selectedTmdbId) {
                                            it.copy(isSelected = !it.isSelected)
                                        } else {
                                            it
                                        }
                                    }
                                )
                            )
                        )
                    } else {
                        it
                    }
                }
            }

            DiscoverViewModelAction.RecommendedSelectionClear -> {
                uiModel.update {
                    // physically flinched from copy pasting this
                    if (it is DiscoverUiState.Ok) {
                        it.copy(
                            uiModel = it.uiModel.copy(
                                showsRecommended = it.uiModel.showsRecommended.copy(
                                    selectionAvailable = it.uiModel.showsRecommended.selectionAvailable.map {
                                        it.copy(isSelected = false)
                                    }
                                )
                            )
                        )
                    } else {
                        it
                    }
                }
            }

            DiscoverViewModelAction.RecommendedUpdate -> {
                uiModel.update {
                    if (it is DiscoverUiState.Ok) {
                        it.copy(
                            uiModel = it.uiModel.copy(
                                showsRecommended = it.uiModel.showsRecommended.copy(isLoading = true)
                            )
                        )
                    } else {
                        it
                    }
                }
                refresh()
            }
        }
    }
}

sealed class DiscoverUiState {
    data object Loading : DiscoverUiState()
    data object Error : DiscoverUiState()
    data class Ok(val uiModel: DiscoverUiModel) : DiscoverUiState() {
        val showsTrendingWeeklyPreview get() = uiModel.showsTrendingWeekly.take(3)
        val showsReleasedSoonPreview get() = uiModel.showsReleasedSoon.take(3)
    }
}

data class DiscoverUiModel(
    val showsTrendingWeekly: List<Content>,
    val showsReleasedSoon: List<Content>,
    val showsRecommended: RecommendedContent
) {
    data class Content(
        val tmdbId: Int,
        val title: String,
        val image: String
    )

    data class RecommendedContent(
        val selectionAvailable: List<Selection>,
        val selectionActive: List<Content>,
        val selectionActiveText: String,
        val results: List<Content>,
        val isLoading: Boolean
    ) {
        val resultsPreview get() = results.take(3)
        val selectionCountLive get() = selectionAvailable.count { it.isSelected }
        val isMissingSelection get() = selectionCountLive == 0

        data class Selection(
            val tmdbId: Int,
            val title: String,
            val image: String,
            var isSelected: Boolean,
        )
    }
}
