package com.free.tvtracker.data.tracked

import app.cash.turbine.test
import com.free.tvtracker.base.ApiError
import com.free.tvtracker.data.common.sql.LocalSqlDataProvider
import com.free.tvtracker.data.tracked.entities.StoredShowClientEntity
import com.free.tvtracker.data.tracked.entities.TrackedShowClientEntity
import com.free.tvtracker.expect.data.TvHttpClientEndpoints
import com.free.tvtracker.tracked.response.TrackedShowsApiResponse
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class TrackedShowsRepositoryTest {

    @Test
    fun `GIVEN local shows exist WHEN fetching remote shows fails THEN local shows are returned with no error`() =
        runTest {
            val httpClient: TvHttpClientEndpoints = mockk {
                coEvery { getWatching() } returns TrackedShowsApiResponse.error(ApiError("boop"))
            }
            val localDataSource: LocalSqlDataProvider = mockk {
                every { getTrackedShows() } returns listOf(
                    TrackedShowClientEntity(
                        1,
                        "",
                        emptyList(),
                        StoredShowClientEntity(1, "", emptyList(), null, null, ""),
                        false
                    )
                )
            }
            val taskQueue: WatchedEpisodesTaskQueue = mockk(relaxed = true)
            val sut = TrackedShowsRepository(httpClient, localDataSource, taskQueue, mockk())
            sut.watchingShows.test {
                sut.updateWatching()
                val init = awaitItem()
                val onLocalFetched = awaitItem()
                val onDataCombine = awaitItem()
                assertEquals(false, init.status.fetched)
                assertEquals(false, onLocalFetched.status.fetched)
                assertEquals(true, onLocalFetched.status.success)
                assertEquals(1, onDataCombine.data.size)
                cancelAndConsumeRemainingEvents()
            }
        }
}
