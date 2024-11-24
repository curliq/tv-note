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
        data object LoadPageTrending : DiscoverViewModelAction()
        data object LoadPageNewReleases : DiscoverViewModelAction()
    }

    val data: MutableStateFlow<DiscoverUiState> = MutableStateFlow(DiscoverUiState.Loading)

    /**
     * this is seperate from the uistate because the uistate gets reset when setting to Loading
     */
    val filterTvShows = MutableStateFlow(true)

    fun refresh(showLoading: Boolean = false) {
        if (showLoading) {
            data.value = DiscoverUiState.Loading
        }
        viewModelScope.launch(ioDispatcher) {
            val trending = async {
                if (filterTvShows.value) {
                    searchRepository.getTrendingWeeklyShows(1).data?.results?.map { trendingShowsMapper.map(it) }
                } else {
                    searchRepository.getTrendingWeeklyMovies(1).data?.results?.map { trendingMoviesMapper.map(it) }
                }
            }
            val releases = async {
                if (filterTvShows.value) {
                    searchRepository.getNewEpisodeReleasedSoon(1).data?.results?.map { trendingShowsMapper.map(it) }
                } else {
                    searchRepository.getNewMoviesReleasedSoon(1).data?.results?.map { trendingMoviesMapper.map(it) }
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
                            contentTrendingWeekly = DiscoverUiModel.ContentPaged(
                                trendingRes ?: emptyList(),
                                page = 1,
                                isLastPage = false
                            ),
                            contentReleasedSoon = DiscoverUiModel.ContentPaged(
                                releasesRes ?: emptyList(),
                                page = 1,
                                isLastPage = false
                            ),
                            contentRecommended = DiscoverUiModel.RecommendedContent(
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
                ?.uiModel?.contentRecommended?.selectionAvailable
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
                                contentRecommended = it.uiModel.contentRecommended.copy(
                                    selectionAvailable = it.uiModel.contentRecommended.selectionAvailable.map {
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
                                contentRecommended = it.uiModel.contentRecommended.copy(
                                    selectionAvailable = it.uiModel.contentRecommended.selectionAvailable.map {
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
                                contentRecommended = it.uiModel.contentRecommended.copy(isLoading = true)
                            )
                        )
                    } else {
                        it
                    }
                }
                refresh(showLoading = false)
            }


            DiscoverViewModelAction.LoadPageTrending -> {
                if ((data.value as DiscoverUiState.Ok).uiModel.contentTrendingWeekly.isLastPage) return
                viewModelScope.launch(ioDispatcher) {
                    val page = (data.value as DiscoverUiState.Ok).uiModel.contentTrendingWeekly.page + 1
                    var totalPages = page
                    var responseMapped: List<DiscoverUiModel.Content> = emptyList()
                    if (filterTvShows.value) {
                        searchRepository.getTrendingWeeklyShows(page).asSuccess { response ->
                            responseMapped = response.results.map { trendingShowsMapper.map(it) }
                            totalPages = response.totalPages
                        }
                    } else {
                        searchRepository.getTrendingWeeklyMovies(page).asSuccess { response ->
                            responseMapped = response.results.map { trendingMoviesMapper.map(it) }
                            totalPages = response.totalPages
                        }
                    }
                    data.update {
                        if (it is DiscoverUiState.Ok) {
                            it.copy(
                                uiModel = it.uiModel.copy(
                                    contentTrendingWeekly = it.uiModel.contentTrendingWeekly.copy(
                                        data = it.uiModel.contentTrendingWeekly.data.plus(responseMapped),
                                        page = page,
                                        isLastPage = page == totalPages
                                    )
                                )
                            )
                        } else {
                            it
                        }
                    }
                }
            }

            DiscoverViewModelAction.LoadPageNewReleases -> {
                if ((data.value as DiscoverUiState.Ok).uiModel.contentReleasedSoon.isLastPage) return
                viewModelScope.launch(ioDispatcher) {
                    val page = (data.value as DiscoverUiState.Ok).uiModel.contentReleasedSoon.page + 1
                    var totalPages = page
                    var responseMapped: List<DiscoverUiModel.Content> = emptyList()
                    if (filterTvShows.value) {
                        searchRepository.getNewEpisodeReleasedSoon(page).asSuccess { response ->
                            responseMapped = response.results.map { trendingShowsMapper.map(it) }
                            totalPages = response.totalPages
                        }
                    } else {
                        searchRepository.getNewMoviesReleasedSoon(page).asSuccess { response ->
                            responseMapped = response.results.map { trendingMoviesMapper.map(it) }
                            totalPages = response.totalPages
                        }
                    }
                    data.update {
                        if (it is DiscoverUiState.Ok) {
                            it.copy(
                                uiModel = it.uiModel.copy(
                                    contentReleasedSoon = it.uiModel.contentReleasedSoon.copy(
                                        data = it.uiModel.contentReleasedSoon.data.plus(responseMapped),
                                        page = page,
                                        isLastPage = page == totalPages
                                    )
                                )
                            )
                        } else {
                            it
                        }
                    }
                }
            }
        }
    }
}

sealed class DiscoverUiState {
    data object Loading : DiscoverUiState()
    data object Error : DiscoverUiState()
    data class Ok(val uiModel: DiscoverUiModel) : DiscoverUiState() {
        val showsTrendingWeeklyPreview get() = uiModel.contentTrendingWeekly.data.take(3)
        val showsReleasedSoonPreview get() = uiModel.contentReleasedSoon.data.take(3)
    }
}

data class DiscoverUiModel(
    val contentTrendingWeekly: ContentPaged,
    val contentReleasedSoon: ContentPaged,
    val contentRecommended: RecommendedContent
) {
    data class ContentPaged(val data: List<Content>, val page: Int, val isLastPage: Boolean)
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
