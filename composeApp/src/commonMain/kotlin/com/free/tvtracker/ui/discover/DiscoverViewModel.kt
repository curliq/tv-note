package com.free.tvtracker.ui.discover

import com.free.tvtracker.data.search.SearchRepository
import com.free.tvtracker.data.tracked.TrackedShowsRepository
import com.free.tvtracker.discover.response.RecommendedContentApiResponse
import com.free.tvtracker.expect.CommonStringUtils
import com.free.tvtracker.expect.ui.ViewModel
import com.free.tvtracker.ui.common.TmdbConfigData
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DiscoverViewModel(
    private val searchRepository: SearchRepository,
    private val trackedShowsRepository: TrackedShowsRepository,
    private val trendingShowsMapper: DiscoverShowUiModelMapper,
    private val trendingMoviesMapper: DiscoverMovieUiModelMapper,
    private val recommendedMapper: RecommendedShowUiModelMapper,
    private val stringUtils: CommonStringUtils = CommonStringUtils(),
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
) : ViewModel() {

    sealed class DiscoverViewModelAction {
        data class RecommendedSelectionAdded(val selectedTmdbId: Int) : DiscoverViewModelAction()
        data object RecommendedSelectionClear : DiscoverViewModelAction()
        data object RecommendedUpdate : DiscoverViewModelAction()
    }

    val data: MutableStateFlow<DiscoverUiState> = MutableStateFlow(DiscoverUiState.Loading)

    /**
     * this is seperate from the uistate because the uistate gets reset when setting to Loading
     */
    val filterTvShows = MutableStateFlow(true)

    init {
        refresh(showLoading = true)
    }

    fun refresh(showLoading: Boolean = false) {
        if (showLoading) {
            data.value = DiscoverUiState.Loading
        }
        viewModelScope.launch(ioDispatcher) {
            val trending = async {
                if (filterTvShows.value) {
                    searchRepository.getTrendingWeekly().data?.results?.map { trendingShowsMapper.map(it) }
                } else {
                    searchRepository.getTrendingWeeklyMovies().data?.results?.map { trendingMoviesMapper.map(it) }
                }
            }
            val releases = async {
                if (filterTvShows.value) {
                    searchRepository.getNewEpisodeReleasedSoon().data?.results?.map { trendingShowsMapper.map(it) }
                } else {
                    searchRepository.getNewMoviesReleasedSoon().data?.results?.map { trendingMoviesMapper.map(it) }
                }
            }
            val rec = async { getRecommended() }
            val trendingRes = trending.await()
            val releasesRes = releases.await()
            val recRes = rec.await()

            if (trendingRes == null && releasesRes == null && recRes.isError()) {
                data.value = DiscoverUiState.Error
            } else {
                val selectionActive = recRes.data?.relatedContent?.map {
                    val show = trackedShowsRepository.getByTmdbId(it.tmdbId)!!
                    DiscoverUiModel.Content(
                        it.tmdbId,
                        show.tvShow?.storedShow?.title ?: show.movie!!.storedMovie.title,
                        TmdbConfigData.get().getPosterUrl(
                            show.tvShow?.storedShow?.posterImage ?: show.movie!!.storedMovie.posterImage
                        ),
                        show.isTvShow
                    )
                } ?: emptyList()

                // Why not `stateIn()`? because this viewmodel doesn't have a lifecycleScope because it's a singleton,
                // why is it a singleton? because it needs to be shared between the discover and recommended activities
                trackedShowsRepository.allShows.collect { allShows ->
                    data.value = DiscoverUiState.Ok(
                        DiscoverUiModel(
                            showsTrendingWeekly = trendingRes ?: emptyList(),
                            showsReleasedSoon = releasesRes ?: emptyList(),
                            showsRecommended = DiscoverUiModel.RecommendedContent(
                                selectionActiveText = stringUtils.listToString(selectionActive.map { it.title }),
                                selectionAvailable = allShows
                                    .filter { it.isTvShow == filterTvShows.value }
                                    .map { show ->
                                        DiscoverUiModel.RecommendedContent.Selection(
                                            show.anyTmdbId,
                                            show.tvShow?.storedShow?.title ?: show.movie!!.storedMovie.title,
                                            TmdbConfigData.get().getPosterUrl(
                                                show.tvShow?.storedShow?.posterImage
                                                    ?: show.movie!!.storedMovie.posterImage
                                            ),
                                            isSelected = selectionActive.find { it.tmdbId == show.anyTmdbId } != null
                                        )
                                    },
                                selectionActive = selectionActive,
                                results = recRes.data?.results?.map(recommendedMapper.map()) ?: emptyList(),
                                isLoading = false
                            )
                        )
                    )
                }
            }
        }
    }

    fun toggleContentFilter(tvShows: Boolean) {
        filterTvShows.update { tvShows }
        refresh(showLoading = true)
    }

    private suspend fun getRecommended(): RecommendedContentApiResponse {
        trackedShowsRepository.updateWatching(forceUpdate = false)
        trackedShowsRepository.updateFinished(forceUpdate = false)
        trackedShowsRepository.updateWatchlisted(forceUpdate = false)
        val shows =
            (data.value as? DiscoverUiState.Ok)
                ?.uiModel?.showsRecommended?.selectionAvailable
                ?.filter { it.isSelected }
                ?.map { it.tmdbId }
                ?: getDefaultRecommendedSelection()
        return searchRepository.getRecommended(shows, filterTvShows.value)
    }

    private fun getDefaultRecommendedSelection(): List<Int> {
        val shows = trackedShowsRepository.allShows.value
        val related = shows.filter { it.isTvShow == filterTvShows.value }
            .sortedByDescending { it.tvShow?.createdAtDatetime ?: it.movie!!.createdAtDatetime }.take(2)
        return related.map { it.tvShow?.storedShow?.tmdbId ?: it.movie!!.storedMovie.tmdbId }
    }

    fun action(action: DiscoverViewModelAction) {
        when (action) {
            is DiscoverViewModelAction.RecommendedSelectionAdded -> {
                data.update {
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
                data.update {
                    // physically flinched from copying and pasting this
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
                data.update {
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
                refresh(showLoading = false)
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
        val image: String,
        val isTvShow: Boolean
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
