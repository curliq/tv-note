package com.free.tvtracker.screens.search

import com.free.tvtracker.data.search.SearchRepository
import com.free.tvtracker.data.tracked.TrackedShowsRepository
import com.free.tvtracker.search.response.SearchApiModel
import com.free.tvtracker.search.response.SearchApiResponse
import com.free.tvtracker.search.response.SearchShowApiModel
import com.free.tvtracker.tracked.response.TrackedShowApiResponse
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class AddTrackedViewModelTest {
    private val dispatcher = UnconfinedTestDispatcher()
    private val repo: SearchRepository = mockk {
        coEvery { searchTvShows(any()) } returns SearchApiResponse.ok(
            SearchApiModel(
                listOf(SearchShowApiModel(1, "name", "overview", "poster")),
                emptyList(),
                emptyList()
            )
        )
    }
    private val trackedRepo: TrackedShowsRepository = mockk(relaxed = true) {
        coEvery { getOrUpdateWatchingShows() } returns TrackedShowApiResponse.ok(emptyList())
    }

    @Test
    fun testPreviewsSearchIssubbed() = runTest {
        val vm = AddTrackedViewModel(repo, trackedRepo, ioDispatcher = dispatcher)
        vm.searchQuery.value = "helo"

        dispatcher.scheduler.advanceUntilIdle()
        assertEquals(1, (vm.results.value as AddTrackedUiState.Ok).data.size)
        coVerify { repo.searchTvShows(eq("helo")) }
    }

    @Test
    fun testPreviewsSearchIsCancelled() = runTest {
        val vm = AddTrackedViewModel(repo, trackedRepo, ioDispatcher = dispatcher)
        vm.searchQuery.value = "helo"
        vm.searchQuery.value = "helo1"
        vm.searchQuery.value = "helo2"
        vm.searchQuery.value = "helo3"

        dispatcher.scheduler.advanceUntilIdle()
        assertEquals(1, (vm.results.value as AddTrackedUiState.Ok).data.size)
        coVerify(exactly = 1) { repo.searchTvShows(eq("helo3")) }
    }

    @Test
    fun testSetQuery() {
        val vm = AddTrackedViewModel(repo, trackedRepo, ioDispatcher = dispatcher)
        vm.searchQuery.value = "helo"
        assertEquals(vm.searchQuery.value, "helo")
    }
}
