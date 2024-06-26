package com.free.tvtracker.ui.watching

import com.free.tvtracker.domain.GetNextUnwatchedEpisodeUseCase
import com.free.tvtracker.tracked.response.TrackedContentApiModel
import kotlin.test.Test
import kotlin.test.assertEquals

class GetNextUnwatchedEpisodeUseCaseTest {

    @Test
    fun `GIVEN 3 episodes and 2 seen THEN 3rd one is returned`() {
        val show = TrackedContentApiModel(
            id = 1,
            createdAtDatetime = "2014-02-03",
            watchedEpisodes = listOf(
                TrackedContentApiModel.WatchedEpisodeApiModel("1", 1),
                TrackedContentApiModel.WatchedEpisodeApiModel("2", 2),
            ),
            storedShow = TrackedContentApiModel.StoredShowApiModel(
                tmdbId = 1,
                title = "game of thrones",
                storedEpisodes = listOf(
                    TrackedContentApiModel.StoredEpisodeApiModel(id = 1, season = 1, episode = 1, airDate = "2015-1-01"),
                    TrackedContentApiModel.StoredEpisodeApiModel(id = 2, season = 1, episode = 2, airDate = "2015-1-01"),
                    TrackedContentApiModel.StoredEpisodeApiModel(id = 3, season = 1, episode = 3, airDate = "2015-1-01"),
                ),
                posterImage = "",
                backdropImage = null,
                status = ""
            ),
            watchlisted = false
        )
        val sut = GetNextUnwatchedEpisodeUseCase()
        assertEquals(3, sut(show = show)?.id)
    }

    @Test
    fun `GIVEN 3 episodes and 0 seen THEN 1rd one is returned`() {
        val show = TrackedContentApiModel(
            id = 1,
            createdAtDatetime = "2014-02-03",
            watchedEpisodes = listOf(
            ),
            storedShow = TrackedContentApiModel.StoredShowApiModel(
                tmdbId = 1,
                title = "game of thrones",
                storedEpisodes = listOf(
                    TrackedContentApiModel.StoredEpisodeApiModel(id = 1, season = 1, episode = 1, airDate = "2022-1-1"),
                    TrackedContentApiModel.StoredEpisodeApiModel(id = 2, season = 1, episode = 2, airDate = "2022-1-1"),
                    TrackedContentApiModel.StoredEpisodeApiModel(id = 3, season = 1, episode = 3, airDate = "2022-1-1"),
                ),
                posterImage = "",
                backdropImage = null,
                status = ""
            ),
            watchlisted = false
        )
        val sut = GetNextUnwatchedEpisodeUseCase()
        assertEquals(1, sut(show = show)?.id)
    }

    @Test
    fun `GIVEN 3 episodes and 3 seen THEN null is returned`() {
        val show = TrackedContentApiModel(
            id = 1,
            createdAtDatetime = "2014-02-03",
            watchedEpisodes = listOf(
                TrackedContentApiModel.WatchedEpisodeApiModel("1", 1),
                TrackedContentApiModel.WatchedEpisodeApiModel("2", 2),
                TrackedContentApiModel.WatchedEpisodeApiModel("3", 3),
            ),
            storedShow = TrackedContentApiModel.StoredShowApiModel(
                tmdbId = 1,
                title = "game of thrones",
                storedEpisodes = listOf(
                    TrackedContentApiModel.StoredEpisodeApiModel(id = 1, season = 1, episode = 1, airDate = "2022-1-1"),
                    TrackedContentApiModel.StoredEpisodeApiModel(id = 2, season = 1, episode = 2, airDate = "2022-1-1"),
                    TrackedContentApiModel.StoredEpisodeApiModel(id = 3, season = 1, episode = 3, airDate = "2022-1-1"),
                ),
                posterImage = "",
                backdropImage = null,
                status = ""
            ),
            watchlisted = false
        )
        val sut = GetNextUnwatchedEpisodeUseCase()
        assertEquals(null, sut(show = show))
    }

    @Test
    fun `GIVEN 3 episodes and only last one seen THEN null is returned`() {
        val show = TrackedContentApiModel(
            id = 1,
            createdAtDatetime = "2014-02-03",
            watchedEpisodes = listOf(
                TrackedContentApiModel.WatchedEpisodeApiModel("3", 3),
            ),
            storedShow = TrackedContentApiModel.StoredShowApiModel(
                tmdbId = 1,
                title = "game of thrones",
                storedEpisodes = listOf(
                    TrackedContentApiModel.StoredEpisodeApiModel(id = 1, season = 1, episode = 1, airDate = "2022-1-1"),
                    TrackedContentApiModel.StoredEpisodeApiModel(id = 2, season = 1, episode = 2, airDate = "2022-1-1"),
                    TrackedContentApiModel.StoredEpisodeApiModel(id = 3, season = 1, episode = 3, airDate = "2022-1-1"),
                ),
                posterImage = "",
                backdropImage = null,
                status = ""
            ),
            watchlisted = false
        )
        val sut = GetNextUnwatchedEpisodeUseCase()
        assertEquals(null, sut(show = show))
    }
}
