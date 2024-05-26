package com.free.tvtracker.data.tracked

import com.free.tvtracker.tracked.response.TrackedShowApiModel
import com.free.tvtracker.tracked.response.TrackedShowApiResponse
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class TrackedShowsRepositoryTest {

    @Test
    fun testWatchingShowsEmits() = runTest {
        val sut = TrackedShowsRepository(
            remoteDataSource = mockk(relaxed = true) {
                coEvery { getTrackedShows() } returns TrackedShowApiResponse.ok(
                    listOf(
                        TrackedShowApiModel(
                            0,
                            "2014-01-01T00:00:00.000",
                            emptyList(),
                            TrackedShowApiModel.StoredShowApiModel(0, "got", emptyList(), "", "ended"),
                            false
                        )
                    )
                )
            },
            localDataSource = mockk(relaxed = true),
            watchedEpisodesTaskQueue = mockk(relaxed = true),
        )
        sut.updateWatching()
        assertEquals(0, sut.watchingShows.first().data.data?.first()?.id)
    }
}
