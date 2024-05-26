package com.free.tvtracker.screens.details

import com.free.tvtracker.base.ApiError
import com.free.tvtracker.constants.TmdbShowStatus
import com.free.tvtracker.data.search.SearchRepository
import com.free.tvtracker.data.tracked.MarkEpisodeWatched
import com.free.tvtracker.data.tracked.TrackedShowsRepository
import com.free.tvtracker.discover.response.TmdbShowDetailsApiModel
import com.free.tvtracker.discover.response.TmdbShowDetailsApiResponse
import com.free.tvtracker.domain.GetTrackedShowByTmdbIdUseCase
import com.free.tvtracker.screens.details.mappers.ShowUiModelMapper
import com.free.tvtracker.tracked.response.TrackedShowApiModel
import com.free.tvtracker.utils.buildDetailsUiModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import org.junit.Assert.assertEquals
import kotlin.test.Test

@OptIn(ExperimentalCoroutinesApi::class)
class DetailsViewModelTest {
    private val dispatcher = UnconfinedTestDispatcher()
    private val trackedShowsRepository: TrackedShowsRepository = mockk(relaxed = true)
    private val mapper: ShowUiModelMapper = mockk(relaxed = true)
    private val getTrackedShowByTmdbIdUseCase: GetTrackedShowByTmdbIdUseCase =
        mockk<GetTrackedShowByTmdbIdUseCase>().apply {
            every { this@apply.invoke(any()) } returns flowOf(
                GetTrackedShowByTmdbIdUseCase.GetTrackedShowByTmdbIdResult(
                    TmdbShowDetailsApiResponse.ok(
                        TmdbShowDetailsApiModel(1, "game of thrones", TmdbShowStatus.ENDED.status)
                    ), tracked = null
                )
            )
        }
    private val viewModel =
        DetailsViewModel(mapper, trackedShowsRepository, getTrackedShowByTmdbIdUseCase, dispatcher)

    @Test
    fun `GIVEN data is available THEN ui state is ok`() {
        viewModel.setId(1)
        dispatcher.scheduler.advanceUntilIdle()
        assertEquals(DetailsUiState.Ok::class.java, viewModel.result.value::class.java)
    }

    @Test
    fun `GIVEN data fails THEN ui state is error`() {
        coEvery { getTrackedShowByTmdbIdUseCase.invoke(any()) } returns flowOf(
            GetTrackedShowByTmdbIdUseCase.GetTrackedShowByTmdbIdResult(
                TmdbShowDetailsApiResponse.error(ApiError("500")), tracked = null
            )
        )
        viewModel.setId(1)
        assertEquals(DetailsUiState.Error::class.java, viewModel.result.value::class.java)
    }

    @Test
    fun `GIVEN season with 2 watched eps WHEN marking season watched THEN 0 eps are sent`() {
        every { mapper.map(any(), any()) } returns buildDetailsUiModel(
            seasons = listOf(
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
            )
        )
        viewModel.setId(1)
        viewModel.action(DetailsViewModel.DetailsAction.MarkSeasonWatched(1, 1))
        coVerify { trackedShowsRepository.markEpisodeAsWatched(emptyList()) }
    }

    @Test
    fun `GIVEN season with 2 unwatched eps WHEN marking season watched THEN 2 eps are sent`() {
        every {
            trackedShowsRepository.getShowByTmdbId(1)
        } returns TrackedShowApiModel(1, "", emptyList(), mockk(), false)
        every { mapper.map(any(), any()) } returns buildDetailsUiModel(
            seasons = listOf(
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
            )
        )
        viewModel.setId(1)
        viewModel.action(DetailsViewModel.DetailsAction.MarkSeasonWatched(1, 1))
        assertEquals(DetailsUiState.Ok::class.java, viewModel.result.value::class.java)
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
        every { mapper.map(any(), any()) } returns buildDetailsUiModel(
            seasons = listOf(
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
            )
        )
        viewModel.setId(1)
        viewModel.action(DetailsViewModel.DetailsAction.MarkSeasonWatched(1, 1))
        coVerify { trackedShowsRepository.markEpisodeAsWatched(emptyList()) }
    }
}
