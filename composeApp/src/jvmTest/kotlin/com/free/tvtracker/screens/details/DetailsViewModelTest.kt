package com.free.tvtracker.screens.details

import com.free.tvtracker.base.ApiError
import com.free.tvtracker.data.search.SearchRepository
import com.free.tvtracker.data.tracked.MarkEpisodeWatched
import com.free.tvtracker.data.tracked.TrackedShowsRepository
import com.free.tvtracker.discover.response.TmdbShowDetailsApiModel
import com.free.tvtracker.discover.response.TmdbShowDetailsApiResponse
import com.free.tvtracker.domain.GetTrackedShowUseCase
import com.free.tvtracker.screens.details.mappers.ShowUiModelMapper
import com.free.tvtracker.tracked.response.TmdbShowStatus
import com.free.tvtracker.tracked.response.TrackedShowApiModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import org.junit.Assert.assertEquals
import kotlin.test.Test

@OptIn(ExperimentalCoroutinesApi::class)
class DetailsViewModelTest {
    private val dispatcher = UnconfinedTestDispatcher()
    private val searchRepository: SearchRepository = mockk()
    private val trackedShowsRepository: TrackedShowsRepository = mockk(relaxed = true)
    private val mapper: ShowUiModelMapper = mockk(relaxed = true)
    private val getTrackedShowUseCase: GetTrackedShowUseCase = mockk<GetTrackedShowUseCase>().apply {
        every { this@apply.invoke(any()) } returns emptyFlow()
    }
    private val viewModel =
        DetailsViewModel(searchRepository, mapper, trackedShowsRepository, getTrackedShowUseCase, dispatcher)

    @Test
    fun `GIVEN data is available THEN ui state is ok`() {
        coEvery { searchRepository.getShow(any()) } returns TmdbShowDetailsApiResponse.ok(
            TmdbShowDetailsApiModel(1, "game of thrones", TmdbShowStatus.ENDED.status)
        )
        every { getTrackedShowUseCase.invoke(any()) } returns emptyFlow()
        viewModel.setId(1)
        dispatcher.scheduler.advanceUntilIdle()
        assertEquals(DetailsUiState.Ok::class.java, viewModel.result.value::class.java)
    }

    @Test
    fun `GIVEN data fails THEN ui state is error`() {
        coEvery { searchRepository.getShow(any()) } returns TmdbShowDetailsApiResponse.error(ApiError("500"))
        viewModel.setId(1)
        dispatcher.scheduler.advanceUntilIdle()
        assertEquals(DetailsUiState.Error::class.java, viewModel.result.value::class.java)
    }

    @Test
    fun `GIVEN season with 2 watched eps WHEN marking season watched THEN 0 eps are sent`() {
        coEvery { searchRepository.getShow(any()) } returns TmdbShowDetailsApiResponse.ok(
            TmdbShowDetailsApiModel(1, "game of thrones", TmdbShowStatus.ENDED.status)
        )
        every { mapper.map(any(), any()) } returns DetailsUiModel(
            0, "", "", "", "", null, null, seasons = listOf(
                DetailsUiModel.Season(
                    seasonId = 1, tmdbShowId = 1, "", false, true, episodes = listOf(
                        DetailsUiModel.Season.Episode(
                            id = 1, "", "", "", "2000-01-01", watched = true, isWatchable = true
                        ),
                        DetailsUiModel.Season.Episode(
                            id = 2, "", "", "", "2000-01-01", watched = true, isWatchable = true
                        ),
                    )
                )
            ), castFirst = null, castSecond = null, watchProviders = emptyList()
        )
        viewModel.setId(1)
        viewModel.action(DetailsViewModel.DetailsAction.MarkSeasonWatched(1, 1))
        coVerify { trackedShowsRepository.markEpisodeAsWatched(emptyList()) }
    }

    @Test
    fun `GIVEN season with 2 unwatched eps WHEN marking season watched THEN 2 eps are sent`() {
        coEvery { searchRepository.getShow(any()) } returns TmdbShowDetailsApiResponse.ok(
            TmdbShowDetailsApiModel(1, "game of thrones", TmdbShowStatus.ENDED.status)
        )
        every {
            trackedShowsRepository.getShowByTmdbId(1)
        } returns TrackedShowApiModel(1, "", emptyList(), mockk(), false)
        every { mapper.map(any(), any()) } returns DetailsUiModel(
            0, "", "", "", "", null, null, seasons = listOf(
                DetailsUiModel.Season(
                    seasonId = 1, tmdbShowId = 1, "", false, true, episodes = listOf(
                        DetailsUiModel.Season.Episode(
                            id = 1, "", "", "", "2000-01-01", watched = false, isWatchable = true
                        ),
                        DetailsUiModel.Season.Episode(
                            id = 2, "", "", "", "2000-01-01", watched = false, isWatchable = true
                        ),
                    )
                )
            ), castFirst = null, castSecond = null, watchProviders = emptyList()
        )
        viewModel.setId(1)
        viewModel.action(DetailsViewModel.DetailsAction.MarkSeasonWatched(1, 1))
        coVerify {
            trackedShowsRepository.markEpisodeAsWatched(
                listOf(
                    MarkEpisodeWatched(1, 1),
                    MarkEpisodeWatched(1, 2)
                )
            )
        }
    }

    @Test
    fun `GIVEN season with 2 unwatchable eps WHEN marking season watched THEN 0 eps are sent`() {
        coEvery { searchRepository.getShow(any()) } returns TmdbShowDetailsApiResponse.ok(
            TmdbShowDetailsApiModel(1, "game of thrones", TmdbShowStatus.ENDED.status)
        )
        every { mapper.map(any(), any()) } returns DetailsUiModel(
            0, "", "", "", "", null, null, seasons = listOf(
                DetailsUiModel.Season(
                    seasonId = 1, tmdbShowId = 1, "", false, true, episodes = listOf(
                        DetailsUiModel.Season.Episode(
                            id = 1, "", "", "", "2000-01-01", watched = false, isWatchable = false
                        ),
                        DetailsUiModel.Season.Episode(
                            id = 2, "", "", "", "2000-01-01", watched = false, isWatchable = false
                        ),
                    )
                )
            ), castFirst = null, castSecond = null, watchProviders = emptyList()
        )
        viewModel.setId(1)
        viewModel.action(DetailsViewModel.DetailsAction.MarkSeasonWatched(1, 1))
        coVerify { trackedShowsRepository.markEpisodeAsWatched(emptyList()) }
    }
}
