package com.free.tvtracker.ui.watching

import com.free.tvtracker.domain.GetNextUnwatchedEpisodeUseCase
import com.free.tvtracker.domain.IsTrackedShowWatchableUseCase
import com.free.tvtracker.tracked.response.TrackedContentApiModel
import com.free.tvtracker.ui.watching.GetNextUnwatchedEpisodeUseCaseTest.Companion.buildTrackedContent
import kotlinx.datetime.Instant
import kotlin.test.Test
import kotlin.test.assertEquals

class IsTrackedShowWatchableUseCaseTest {
    @Test
    fun `GIVEN show with all (1) episodes watched WHEN get watchable THEN its empty`() {
        val shows = listOf(buildTrackedContent(
            episodeDates = listOf("2010-01-01"),
            watchedEpisodeIds = listOf(1)
        ))
        val sut = IsTrackedShowWatchableUseCase(GetNextUnwatchedEpisodeUseCase())
        assertEquals(0, sut.canWatchSoon(shows).size)
        assertEquals(0, sut.canWatchNow(shows).size)
        assertEquals(1, sut.unwatchable(shows).size)
    }

    @Test
    fun `GIVEN show with 1 ep watched and 1 unwatched WHEN get watchable THEN show is shown`() {
        val shows = listOf(buildTrackedContent(
            episodeDates = listOf("2010-01-01", "2010-01-01"),
            watchedEpisodeIds = listOf(1)
        ))
        val sut = IsTrackedShowWatchableUseCase(GetNextUnwatchedEpisodeUseCase())
        assertEquals(1, sut.canWatchNow(shows).size)
        assertEquals(0, sut.canWatchSoon(shows).size)
    }

    @Test
    fun `GIVEN show with 3 ep watched and 1 unwatched and available WHEN get upcoming THEN show is not shown`() {
        val shows = listOf(buildTrackedContent(
            episodeDates = listOf("2010-01-01", "2010-01-01", "2010-01-01"),
            watchedEpisodeIds = listOf(1, 2)
        ))
        val sut = IsTrackedShowWatchableUseCase(GetNextUnwatchedEpisodeUseCase())
        assertEquals(0, sut.canWatchSoon(shows).size)
        assertEquals(1, sut.canWatchNow(shows).size)
    }

    @Test
    fun `GIVEN show with 3 ep watched and 1 unwatched coming out in 2 weeks WHEN get upcoming THEN show is upcoming`() {
        val shows = listOf(buildTrackedContent(
            episodeDates = listOf("2010-01-01", "2010-01-01", "2020-01-15"),
            watchedEpisodeIds = listOf(1, 2)
        ))
        val sut = IsTrackedShowWatchableUseCase(
            GetNextUnwatchedEpisodeUseCase(),
            clockNow = Instant.parse("2020-01-01T00:00:00Z")
        )
        assertEquals(1, sut.canWatchSoon(shows).size)
        assertEquals(0, sut.canWatchNow(shows).size)
    }

    @Test
    fun `GIVEN show with 3 ep watched and 1 unwatched coming out in 6 weeks WHEN get upcoming THEN show is upcoming`() {
        val shows = listOf(buildTrackedContent(
            episodeDates = listOf("2010-01-01", "2010-01-01", "2020-02-15"),
            watchedEpisodeIds = listOf(1, 2)
        ))
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
        val shows = listOf(buildTrackedContent(
            episodeDates = listOf("2010-01-01", "2010-01-01", "2020-03-07"),
            watchedEpisodeIds = listOf(1, 2)
        ))
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
        val shows = listOf(buildTrackedContent(
            episodeDates = listOf("2010-01-01", "2010-01-01", "2020-07-07"),
            watchedEpisodeIds = listOf(1, 2)
        ))
        val sut = IsTrackedShowWatchableUseCase(
            GetNextUnwatchedEpisodeUseCase(),
            clockNow = Instant.parse("2020-01-01T00:00:00Z")
        )
        assertEquals(0, sut.canWatchSoon(shows).size)
        assertEquals(0, sut.canWatchNow(shows).size)
        assertEquals(1, sut.unwatchable(shows).size)
    }
}
