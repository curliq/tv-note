package com.free.tvtracker.ui.watching

import com.free.tvtracker.domain.GetNextUnwatchedEpisodeUseCase
import com.free.tvtracker.domain.IsTrackedShowWatchableUseCase
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
                watchedEpisodes = listOf(TrackedShowApiModel.WatchedEpisodeApiModel("1", 1)),
                storedShow = TrackedShowApiModel.StoredShowApiModel(
                    1, "title",
                    storedEpisodes = listOf(
                        TrackedShowApiModel.StoredEpisodeApiModel(1, 1, 1, "2011-01-01")
                    ), "", ""
                ),
                false
            )
        )
        val sut = IsTrackedShowWatchableUseCase(GetNextUnwatchedEpisodeUseCase())
        assertEquals(0, sut.canWatchSoon(shows).size)
        assertEquals(0, sut.canWatchNow(shows).size)
        assertEquals(1, sut.unwatchable(shows).size)
    }

    @Test
    fun `GIVEN show with 1 ep watched and 1 unwatched WHEN get watchable THEN show is shown`() {
        val shows = listOf(
            TrackedShowApiModel(
                1, "",
                watchedEpisodes = listOf(TrackedShowApiModel.WatchedEpisodeApiModel("1", 1)),
                storedShow = TrackedShowApiModel.StoredShowApiModel(
                    1, "title",
                    storedEpisodes = listOf(
                        TrackedShowApiModel.StoredEpisodeApiModel(1, 1, 1, "2011-01-01"),
                        TrackedShowApiModel.StoredEpisodeApiModel(2, 1, 2, "2011-01-01"),
                    ), "", ""
                ),
                false
            )
        )
        val sut = IsTrackedShowWatchableUseCase(GetNextUnwatchedEpisodeUseCase())
        assertEquals(1, sut.canWatchNow(shows).size)
        assertEquals(0, sut.canWatchSoon(shows).size)
    }

    @Test
    fun `GIVEN show with 3 ep watched and 1 unwatched and available WHEN get upcoming THEN show is not shown`() {
        val shows = listOf(
            TrackedShowApiModel(
                1, "",
                watchedEpisodes = listOf(
                    TrackedShowApiModel.WatchedEpisodeApiModel("1", 1),
                    TrackedShowApiModel.WatchedEpisodeApiModel("2", 2),
                ),
                storedShow = TrackedShowApiModel.StoredShowApiModel(
                    1, "title",
                    storedEpisodes = listOf(
                        TrackedShowApiModel.StoredEpisodeApiModel(1, 1, 1, "2011-01-01"),
                        TrackedShowApiModel.StoredEpisodeApiModel(2, 1, 2, "2011-01-01"),
                        TrackedShowApiModel.StoredEpisodeApiModel(3, 1, 3, "2011-01-01"),
                    ), "", ""
                ),
                false
            )
        )
        val sut = IsTrackedShowWatchableUseCase(GetNextUnwatchedEpisodeUseCase())
        assertEquals(0, sut.canWatchSoon(shows).size)
        assertEquals(1, sut.canWatchNow(shows).size)
    }

    @Test
    fun `GIVEN show with 3 ep watched and 1 unwatched coming out in 2 weeks WHEN get upcoming THEN show is upcoming`() {
        val shows = listOf(
            TrackedShowApiModel(
                1, "",
                watchedEpisodes = listOf(
                    TrackedShowApiModel.WatchedEpisodeApiModel("1", 1),
                    TrackedShowApiModel.WatchedEpisodeApiModel("2", 2),
                ),
                storedShow = TrackedShowApiModel.StoredShowApiModel(
                    1, "title",
                    storedEpisodes = listOf(
                        TrackedShowApiModel.StoredEpisodeApiModel(1, 1, 1, "2011-01-01"),
                        TrackedShowApiModel.StoredEpisodeApiModel(2, 1, 2, "2011-01-01"),
                        TrackedShowApiModel.StoredEpisodeApiModel(3, 1, 3, "2020-01-15"),
                    ), "", ""
                ),
                false
            )
        )
        val sut = IsTrackedShowWatchableUseCase(
            GetNextUnwatchedEpisodeUseCase(),
            clockNow = Instant.parse("2020-01-01T00:00:00Z")
        )
        assertEquals(1, sut.canWatchSoon(shows).size)
        assertEquals(0, sut.canWatchNow(shows).size)
    }

    @Test
    fun `GIVEN show with 3 ep watched and 1 unwatched coming out in 6 weeks WHEN get upcoming THEN show is upcoming`() {
        val shows = listOf(
            TrackedShowApiModel(
                1, "",
                watchedEpisodes = listOf(
                    TrackedShowApiModel.WatchedEpisodeApiModel("1", 1),
                    TrackedShowApiModel.WatchedEpisodeApiModel("2", 2),
                ),
                storedShow = TrackedShowApiModel.StoredShowApiModel(
                    1, "title",
                    storedEpisodes = listOf(
                        TrackedShowApiModel.StoredEpisodeApiModel(1, 1, 1, "2011-01-01"),
                        TrackedShowApiModel.StoredEpisodeApiModel(2, 1, 2, "2011-01-01"),
                        TrackedShowApiModel.StoredEpisodeApiModel(3, 1, 3, "2020-02-15"),
                    ), "", ""
                ),
                false
            )
        )
        val sut = IsTrackedShowWatchableUseCase(
            GetNextUnwatchedEpisodeUseCase(),
            clockNow = Instant.parse("2020-01-01T00:00:00Z")
        )
        assertEquals(1, sut.canWatchSoon(shows).size)
        assertEquals(0, sut.canWatchNow(shows).size)
        assertEquals(0, sut.unwatchable(shows).size)
    }

    @Test
    fun `GIVEN show with 3 ep watched and 1 unwatched coming out in 3 months WHEN get upcoming THEN show not shown`() {
        val shows = listOf(
            TrackedShowApiModel(
                1, "",
                watchedEpisodes = listOf(
                    TrackedShowApiModel.WatchedEpisodeApiModel("1", 1),
                    TrackedShowApiModel.WatchedEpisodeApiModel("2", 2),
                ),
                storedShow = TrackedShowApiModel.StoredShowApiModel(
                    1, "title",
                    storedEpisodes = listOf(
                        TrackedShowApiModel.StoredEpisodeApiModel(1, 1, 1, "2011-01-01"),
                        TrackedShowApiModel.StoredEpisodeApiModel(2, 1, 2, "2011-01-01"),
                        TrackedShowApiModel.StoredEpisodeApiModel(3, 1, 3, "2020-03-07"),
                    ), "", ""
                ),
                false
            )
        )
        val sut = IsTrackedShowWatchableUseCase(
            GetNextUnwatchedEpisodeUseCase(),
            clockNow = Instant.parse("2020-01-01T00:00:00Z")
        )
        assertEquals(1, sut.canWatchSoon(shows).size)
        assertEquals(0, sut.canWatchNow(shows).size)
        assertEquals(0, sut.unwatchable(shows).size)
    }

    @Test
    fun `GIVEN show with 3 ep watched and 1 unwatched coming out in 7 months WHEN get upcoming THEN show not shown`() {
        val shows = listOf(
            TrackedShowApiModel(
                1, "",
                watchedEpisodes = listOf(
                    TrackedShowApiModel.WatchedEpisodeApiModel("1", 1),
                    TrackedShowApiModel.WatchedEpisodeApiModel("2", 2),
                ),
                storedShow = TrackedShowApiModel.StoredShowApiModel(
                    1, "title",
                    storedEpisodes = listOf(
                        TrackedShowApiModel.StoredEpisodeApiModel(1, 1, 1, "2011-01-01"),
                        TrackedShowApiModel.StoredEpisodeApiModel(2, 1, 2, "2011-01-01"),
                        TrackedShowApiModel.StoredEpisodeApiModel(3, 1, 3, "2020-07-07"),
                    ), "", ""
                ),
                false
            )
        )
        val sut = IsTrackedShowWatchableUseCase(
            GetNextUnwatchedEpisodeUseCase(),
            clockNow = Instant.parse("2020-01-01T00:00:00Z")
        )
        assertEquals(0, sut.canWatchSoon(shows).size)
        assertEquals(0, sut.canWatchNow(shows).size)
        assertEquals(1, sut.unwatchable(shows).size)
    }
}
