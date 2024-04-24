package com.free.tvtracker.screens.watching

import com.free.tvtracker.tracked.response.TrackedShowApiModel
import kotlinx.datetime.Instant
import kotlin.test.Test
import kotlin.test.assertEquals

class IsTrackedShowWatchableUseCaseTest {
    @Test
    fun `GIVEN show with all (1) episodes watched WHEN get watchable THEN its empty`() {
        val shows = listOf(
            TrackedShowApiModel(
                1, "",
                watchedEpisodes = listOf(TrackedShowApiModel.WatchedEpisodeApiModel(1, "a")),
                storedShow = TrackedShowApiModel.StoredShowApiModel(
                    1, "title",
                    storedEpisodes = listOf(
                        TrackedShowApiModel.StoredEpisodeApiModel("a", 1, 1, "2011-01-01")
                    ), "", ""
                ),
                false
            )
        )
        val sut = IsTrackedShowWatchableUseCase(GetNextUnwatchedEpisodeUseCase())
        assertEquals(0, sut.waitingShortTerm(shows).size)
    }

    @Test
    fun `GIVEN show with 1 ep watched and 1 unwatched WHEN get watchable THEN show is shown`() {
        val shows = listOf(
            TrackedShowApiModel(
                1, "",
                watchedEpisodes = listOf(TrackedShowApiModel.WatchedEpisodeApiModel(1, "a")),
                storedShow = TrackedShowApiModel.StoredShowApiModel(
                    1, "title",
                    storedEpisodes = listOf(
                        TrackedShowApiModel.StoredEpisodeApiModel("a", 1, 1, "2011-01-01"),
                        TrackedShowApiModel.StoredEpisodeApiModel("b", 1, 2, "2011-01-01"),
                    ), "", ""
                ),
                false
            )
        )
        val sut = IsTrackedShowWatchableUseCase(GetNextUnwatchedEpisodeUseCase())
        assertEquals(1, sut.canWatch(shows).size)
    }

    @Test
    fun `GIVEN show with 3 ep watched and 1 unwatched and available WHEN get upcoming THEN show is now shown`() {
        val shows = listOf(
            TrackedShowApiModel(
                1, "",
                watchedEpisodes = listOf(
                    TrackedShowApiModel.WatchedEpisodeApiModel(1, "a"),
                    TrackedShowApiModel.WatchedEpisodeApiModel(2, "b"),
                ),
                storedShow = TrackedShowApiModel.StoredShowApiModel(
                    1, "title",
                    storedEpisodes = listOf(
                        TrackedShowApiModel.StoredEpisodeApiModel("a", 1, 1, "2011-01-01"),
                        TrackedShowApiModel.StoredEpisodeApiModel("b", 1, 2, "2011-01-01"),
                        TrackedShowApiModel.StoredEpisodeApiModel("c", 1, 3, "2011-01-01"),
                    ), "", ""
                ),
                false
            )
        )
        val sut = IsTrackedShowWatchableUseCase(GetNextUnwatchedEpisodeUseCase())
        assertEquals(0, sut.waitingShortTerm(shows).size)
    }

    @Test
    fun `GIVEN show with 3 ep watched and 1 unwatched coming out in 2 weeks WHEN get upcoming THEN show is not shown`() {
        val shows = listOf(
            TrackedShowApiModel(
                1, "",
                watchedEpisodes = listOf(
                    TrackedShowApiModel.WatchedEpisodeApiModel(1, "a"),
                    TrackedShowApiModel.WatchedEpisodeApiModel(2, "b"),
                ),
                storedShow = TrackedShowApiModel.StoredShowApiModel(
                    1, "title",
                    storedEpisodes = listOf(
                        TrackedShowApiModel.StoredEpisodeApiModel("a", 1, 1, "2011-01-01"),
                        TrackedShowApiModel.StoredEpisodeApiModel("b", 1, 2, "2011-01-01"),
                        TrackedShowApiModel.StoredEpisodeApiModel("c", 1, 3, "2020-01-15"),
                    ), "", ""
                ),
                false
            )
        )
        val sut = IsTrackedShowWatchableUseCase(
            GetNextUnwatchedEpisodeUseCase(),
            clockNow = Instant.parse("2020-01-01T00:00:00Z")
        )
        assertEquals(0, sut.waitingShortTerm(shows).size)
    }

    @Test
    fun `GIVEN show with 3 ep watched and 1 unwatched coming out in 6 weeks WHEN get upcoming THEN show is shown`() {
        val shows = listOf(
            TrackedShowApiModel(
                1, "",
                watchedEpisodes = listOf(
                    TrackedShowApiModel.WatchedEpisodeApiModel(1, "a"),
                    TrackedShowApiModel.WatchedEpisodeApiModel(2, "b"),
                ),
                storedShow = TrackedShowApiModel.StoredShowApiModel(
                    1, "title",
                    storedEpisodes = listOf(
                        TrackedShowApiModel.StoredEpisodeApiModel("a", 1, 1, "2011-01-01"),
                        TrackedShowApiModel.StoredEpisodeApiModel("b", 1, 2, "2011-01-01"),
                        TrackedShowApiModel.StoredEpisodeApiModel("c", 1, 3, "2020-02-15"),
                    ), "", ""
                ),
                false
            )
        )
        val sut = IsTrackedShowWatchableUseCase(
            GetNextUnwatchedEpisodeUseCase(),
            clockNow = Instant.parse("2020-01-01T00:00:00Z")
        )
        assertEquals(1, sut.waitingShortTerm(shows).size)
    }
}
