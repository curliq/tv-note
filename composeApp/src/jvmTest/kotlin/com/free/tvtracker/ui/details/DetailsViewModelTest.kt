package com.free.tvtracker.ui.details

import com.free.tvtracker.base.ApiError
import com.free.tvtracker.constants.TmdbShowStatus
import com.free.tvtracker.data.tracked.MarkEpisodeWatched
import com.free.tvtracker.data.tracked.TrackedShowsRepository
import com.free.tvtracker.details.response.TmdbShowDetailsApiModel
import com.free.tvtracker.details.response.TmdbShowDetailsApiResponse
import com.free.tvtracker.domain.GetShowByTmdbIdUseCase
import com.free.tvtracker.ui.details.mappers.DetailsUiModelForMovieMapper
import com.free.tvtracker.ui.details.mappers.DetailsUiModelForShowMapper
import com.free.tvtracker.ui.watching.GetNextUnwatchedEpisodeUseCaseTest.Companion.buildTrackedContent
import com.free.tvtracker.utils.buildDetailsUiModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import org.junit.Assert.assertEquals
import kotlin.test.Test

@OptIn(ExperimentalCoroutinesApi::class)
class DetailsViewModelTest {
    private val dispatcher = UnconfinedTestDispatcher()
    private val trackedShowsRepository: TrackedShowsRepository = mockk(relaxed = true)
    private val showMapper: DetailsUiModelForShowMapper = mockk(relaxed = true)
    private val movieMapper: DetailsUiModelForMovieMapper = mockk(relaxed = true)
    private val getShowByTmdbIdUseCase: GetShowByTmdbIdUseCase =
        mockk<GetShowByTmdbIdUseCase>().apply {
            every { this@apply.invoke(any()) } returns flowOf(
                GetShowByTmdbIdUseCase.GetTrackedShowByTmdbIdResult(
                    TmdbShowDetailsApiResponse.ok(
                        TmdbShowDetailsApiModel(1, "game of thrones", TmdbShowStatus.ENDED.status)
                    ), tracked = null
                )
            )
        }
    private val viewModel =
        DetailsViewModel(
            showMapper,
            movieMapper,
            trackedShowsRepository,
            getShowByTmdbIdUseCase,
            mockk(relaxed = true),
            mockk(relaxed = true),
            mockk(relaxed = true),
            dispatcher
        )

    @Test
    fun `GIVEN data is available THEN ui state is ok`() {
        viewModel.loadContent(DetailsViewModel.LoadContent(1, true))
        dispatcher.scheduler.advanceUntilIdle()
        assertEquals(DetailsUiState.Ok::class.java, viewModel.result.value::class.java)
    }

    @Test
    fun `GIVEN data fails THEN ui state is error`() {
        coEvery { getShowByTmdbIdUseCase.invoke(any()) } returns flowOf(
            GetShowByTmdbIdUseCase.GetTrackedShowByTmdbIdResult(
                TmdbShowDetailsApiResponse.error(ApiError("500")), tracked = null
            )
        )
        viewModel.loadContent(DetailsViewModel.LoadContent(1, true))
        assertEquals(DetailsUiState.Error::class.java, viewModel.result.value::class.java)
    }

    @Test
    fun `GIVEN season with 2 watched eps WHEN marking season watched THEN 0 eps are sent`() {
        every { showMapper.map(any(), any()) } returns buildDetailsUiModel(
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
        viewModel.loadContent(DetailsViewModel.LoadContent(1, true))
        viewModel.action(DetailsViewModel.DetailsAction.MarkSeasonWatched(1, 1))
        coVerify { trackedShowsRepository.markEpisodeAsWatched(emptyList()) }
    }

    @Test
    fun `GIVEN season with 2 unwatched eps WHEN marking season watched THEN 2 eps are sent`() {
        every {
            trackedShowsRepository.getByTmdbId(1)
        } returns buildTrackedContent(emptyList(), emptyList())
        every { showMapper.map(any(), any()) } returns buildDetailsUiModel(
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
        viewModel.loadContent(DetailsViewModel.LoadContent(1, true))
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
        every { showMapper.map(any(), any()) } returns buildDetailsUiModel(
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
        viewModel.loadContent(DetailsViewModel.LoadContent(1, true))
        viewModel.action(DetailsViewModel.DetailsAction.MarkSeasonWatched(1, 1))
        coVerify { trackedShowsRepository.markEpisodeAsWatched(emptyList()) }
    }
}
