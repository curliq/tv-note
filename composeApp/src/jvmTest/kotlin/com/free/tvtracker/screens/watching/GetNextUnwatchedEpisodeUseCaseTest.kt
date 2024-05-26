package com.free.tvtracker.screens.watching

import com.free.tvtracker.domain.GetNextUnwatchedEpisodeUseCase
import com.free.tvtracker.tracked.response.TrackedShowApiModel
import kotlin.test.Test
import kotlin.test.assertEquals

class GetNextUnwatchedEpisodeUseCaseTest {

    @Test
    fun `GIVEN 3 episodes and 2 seen THEN 3rd one is returned`() {
        val show = TrackedShowApiModel(
            id = 1,
            createdAtDatetime = "2014-02-03",
            watchedEpisodes = listOf(
                TrackedShowApiModel.WatchedEpisodeApiModel("1", 1),
                TrackedShowApiModel.WatchedEpisodeApiModel("2", 2),
            ),
            storedShow = TrackedShowApiModel.StoredShowApiModel(
                tmdbId = 1,
                title = "game of thrones",
                storedEpisodes = listOf(
                    TrackedShowApiModel.StoredEpisodeApiModel(id = 1, season = 1, episode = 1, airDate = "2015-1-01"),
                    TrackedShowApiModel.StoredEpisodeApiModel(id = 2, season = 1, episode = 2, airDate = "2015-1-01"),
                    TrackedShowApiModel.StoredEpisodeApiModel(id = 3, season = 1, episode = 3, airDate = "2015-1-01"),
                ),
                posterImage = "",
                status = ""
            ),
            watchlisted = false
        )
        val sut = GetNextUnwatchedEpisodeUseCase()
        assertEquals(3, sut(show = show)?.id)
    }

    @Test
    fun `GIVEN 3 episodes and 0 seen THEN 1rd one is returned`() {
        val show = TrackedShowApiModel(
            id = 1,
            createdAtDatetime = "2014-02-03",
            watchedEpisodes = listOf(
            ),
            storedShow = TrackedShowApiModel.StoredShowApiModel(
                tmdbId = 1,
                title = "game of thrones",
                storedEpisodes = listOf(
                    TrackedShowApiModel.StoredEpisodeApiModel(id = 1, season = 1, episode = 1, airDate = "2022-1-1"),
                    TrackedShowApiModel.StoredEpisodeApiModel(id = 2, season = 1, episode = 2, airDate = "2022-1-1"),
                    TrackedShowApiModel.StoredEpisodeApiModel(id = 3, season = 1, episode = 3, airDate = "2022-1-1"),
                ),
                posterImage = "",
                status = ""
            ),
            watchlisted = false
        )
        val sut = GetNextUnwatchedEpisodeUseCase()
        assertEquals(1, sut(show = show)?.id)
    }

    @Test
    fun `GIVEN 3 episodes and 3 seen THEN null is returned`() {
        val show = TrackedShowApiModel(
            id = 1,
            createdAtDatetime = "2014-02-03",
            watchedEpisodes = listOf(
                TrackedShowApiModel.WatchedEpisodeApiModel("1", 1),
                TrackedShowApiModel.WatchedEpisodeApiModel("2", 2),
                TrackedShowApiModel.WatchedEpisodeApiModel("3", 3),
            ),
            storedShow = TrackedShowApiModel.StoredShowApiModel(
                tmdbId = 1,
                title = "game of thrones",
                storedEpisodes = listOf(
                    TrackedShowApiModel.StoredEpisodeApiModel(id = 1, season = 1, episode = 1, airDate = "2022-1-1"),
                    TrackedShowApiModel.StoredEpisodeApiModel(id = 2, season = 1, episode = 2, airDate = "2022-1-1"),
                    TrackedShowApiModel.StoredEpisodeApiModel(id = 3, season = 1, episode = 3, airDate = "2022-1-1"),
                ),
                posterImage = "",
                status = ""
            ),
            watchlisted = false
        )
        val sut = GetNextUnwatchedEpisodeUseCase()
        assertEquals(null, sut(show = show))
    }

    @Test
    fun `GIVEN 3 episodes and only last one seen THEN null is returned`() {
        val show = TrackedShowApiModel(
            id = 1,
            createdAtDatetime = "2014-02-03",
            watchedEpisodes = listOf(
                TrackedShowApiModel.WatchedEpisodeApiModel("3", 3),
            ),
            storedShow = TrackedShowApiModel.StoredShowApiModel(
                tmdbId = 1,
                title = "game of thrones",
                storedEpisodes = listOf(
                    TrackedShowApiModel.StoredEpisodeApiModel(id = 1, season = 1, episode = 1, airDate = "2022-1-1"),
                    TrackedShowApiModel.StoredEpisodeApiModel(id = 2, season = 1, episode = 2, airDate = "2022-1-1"),
                    TrackedShowApiModel.StoredEpisodeApiModel(id = 3, season = 1, episode = 3, airDate = "2022-1-1"),
                ),
                posterImage = "",
                status = ""
            ),
            watchlisted = false
        )
        val sut = GetNextUnwatchedEpisodeUseCase()
        assertEquals(null, sut(show = show))
    }
}