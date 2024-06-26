package com.free.tvtracker.features.notifications

import com.free.tvtracker.features.tracked.data.shows.EpisodesReelaseTodayQueryResult
import com.free.tvtracker.features.tracked.data.shows.TrackedShowJdbcRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlin.test.Test

class NotificationTaskTest {

    @Test
    fun `GIVEN 1 show, 1 episode today, 1 user THEN 1 push sent to 1 device`() {
        val trackedShowJdbcRepository: TrackedShowJdbcRepository = mockk {
            every { getEpisodesReleasedToday() } returns listOf(
                EpisodesReelaseTodayQueryResult("suits", 1, "token1")
            )
        }
        val fcmService: FcmService = mockk(relaxed = true)
        val sut = NotificationTask(trackedShowJdbcRepository, fcmService)
        sut.notifications()
        verify(exactly = 1) { fcmService.sendPush(any(), any(), eq(listOf("token1"))) }
    }

    @Test
    fun `GIVEN 1 show, 1 episode today, 3 users THEN 1 push sent to 3 devices`() {
        val trackedShowJdbcRepository: TrackedShowJdbcRepository = mockk {
            every { getEpisodesReleasedToday() } returns listOf(
                EpisodesReelaseTodayQueryResult("suits", 1, "token1"),
                EpisodesReelaseTodayQueryResult("suits", 1, "token2"),
                EpisodesReelaseTodayQueryResult("suits", 1, "token3")
            )
        }
        val fcmService: FcmService = mockk(relaxed = true)
        val sut = NotificationTask(trackedShowJdbcRepository, fcmService)
        sut.notifications()
        verify(exactly = 1) { fcmService.sendPush(any(), any(), eq(listOf("token1", "token2", "token3"))) }
    }

    @Test
    fun `GIVEN 1 show, 3 episode today, 1 user THEN 1 push sent to 1 device`() {
        val trackedShowJdbcRepository: TrackedShowJdbcRepository = mockk {
            every { getEpisodesReleasedToday() } returns listOf(
                EpisodesReelaseTodayQueryResult("suits", 1, "token1"),
                EpisodesReelaseTodayQueryResult("suits", 1, "token1"),
                EpisodesReelaseTodayQueryResult("suits", 1, "token1"),
            )
        }
        val fcmService: FcmService = mockk(relaxed = true)
        val sut = NotificationTask(trackedShowJdbcRepository, fcmService)
        sut.notifications()
        verify(exactly = 1) { fcmService.sendPush(any(), any(), eq(listOf("token1"))) }
    }

    @Test
    fun `GIVEN 1 show, 3 episode today, 3 users THEN 1 push sent to 3 devices`() {
        val trackedShowJdbcRepository: TrackedShowJdbcRepository = mockk {
            every { getEpisodesReleasedToday() } returns listOf(
                EpisodesReelaseTodayQueryResult("suits", 1, "token1"),
                EpisodesReelaseTodayQueryResult("suits", 1, "token1"),
                EpisodesReelaseTodayQueryResult("suits", 1, "token1"),
                EpisodesReelaseTodayQueryResult("suits", 1, "token2"),
                EpisodesReelaseTodayQueryResult("suits", 1, "token2"),
                EpisodesReelaseTodayQueryResult("suits", 1, "token2"),
                EpisodesReelaseTodayQueryResult("suits", 1, "token3"),
                EpisodesReelaseTodayQueryResult("suits", 1, "token3"),
                EpisodesReelaseTodayQueryResult("suits", 1, "token3"),
            )
        }
        val fcmService: FcmService = mockk(relaxed = true)
        val sut = NotificationTask(trackedShowJdbcRepository, fcmService)
        sut.notifications()
        verify(exactly = 1) { fcmService.sendPush(any(), any(), eq(listOf("token1", "token2", "token3"))) }
    }

    @Test
    fun `GIVEN 2 shows, 1 episode today each, 1 users THEN 2 push sent to 1 devices`() {
        val trackedShowJdbcRepository: TrackedShowJdbcRepository = mockk {
            every { getEpisodesReleasedToday() } returns listOf(
                EpisodesReelaseTodayQueryResult("suits", 1, "token1"),
                EpisodesReelaseTodayQueryResult("game of throne", 2, "token1"),
            )
        }
        val fcmService: FcmService = mockk(relaxed = true)
        val sut = NotificationTask(trackedShowJdbcRepository, fcmService)
        sut.notifications()
        verify(exactly = 2) { fcmService.sendPush(any(), any(), eq(listOf("token1"))) }
    }

    @Test
    fun `GIVEN 2 shows, 1 episode today each, 3 users THEN 2 push sent to 3 devices`() {
        val trackedShowJdbcRepository: TrackedShowJdbcRepository = mockk {
            every { getEpisodesReleasedToday() } returns listOf(
                EpisodesReelaseTodayQueryResult("suits", 1, "token1"),
                EpisodesReelaseTodayQueryResult("game of throne", 2, "token1"),
                EpisodesReelaseTodayQueryResult("suits", 1, "token2"),
                EpisodesReelaseTodayQueryResult("game of throne", 2, "token2"),
                EpisodesReelaseTodayQueryResult("suits", 1, "token3"),
                EpisodesReelaseTodayQueryResult("game of throne", 2, "token3"),
            )
        }
        val fcmService: FcmService = mockk(relaxed = true)
        val sut = NotificationTask(trackedShowJdbcRepository, fcmService)
        sut.notifications()
        verify(exactly = 2) { fcmService.sendPush(any(), any(), eq(listOf("token1", "token2", "token3"))) }
    }

    @Test
    fun `GIVEN 2 shows, 2 episode today each, 3 users THEN 2 push sent to 3 devices`() {
        val trackedShowJdbcRepository: TrackedShowJdbcRepository = mockk {
            every { getEpisodesReleasedToday() } returns listOf(
                EpisodesReelaseTodayQueryResult("suits", 1, "token1"),
                EpisodesReelaseTodayQueryResult("suits", 1, "token1"),
                EpisodesReelaseTodayQueryResult("game of throne", 2, "token1"),
                EpisodesReelaseTodayQueryResult("game of throne", 2, "token1"),
                EpisodesReelaseTodayQueryResult("suits", 1, "token2"),
                EpisodesReelaseTodayQueryResult("suits", 1, "token2"),
                EpisodesReelaseTodayQueryResult("game of throne", 2, "token2"),
                EpisodesReelaseTodayQueryResult("game of throne", 2, "token2"),
                EpisodesReelaseTodayQueryResult("suits", 1, "token3"),
                EpisodesReelaseTodayQueryResult("suits", 1, "token3"),
                EpisodesReelaseTodayQueryResult("game of throne", 2, "token3"),
                EpisodesReelaseTodayQueryResult("game of throne", 2, "token3"),
            )
        }
        val fcmService: FcmService = mockk(relaxed = true)
        val sut = NotificationTask(trackedShowJdbcRepository, fcmService)
        sut.notifications()
        verify(exactly = 2) { fcmService.sendPush(any(), any(), eq(listOf("token1", "token2", "token3"))) }
    }
}
