package com.free.tvtracker.screens.search

import com.free.tvtracker.data.search.SearchRepository
import com.free.tvtracker.data.tracked.TrackedShowsRepository
import com.free.tvtracker.search.response.SearchApiModel
import com.free.tvtracker.search.response.SearchApiResponse
import com.free.tvtracker.search.response.SearchShowApiModel
import com.free.tvtracker.tracked.response.TrackedShowApiModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.emptyFlow
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
        coEvery { getOrUpdateWatchingShows() } returns emptyList()
        every { allShows } returns MutableStateFlow(emptyList())
    }

    @Test
    fun testPreviewsSearchIsSubbed() = runTest {
        val vm = AddTrackedViewModel(repo, trackedRepo, ShowSearchUiModelMapper(), ioDispatcher = dispatcher)
        vm.setSearchQuery("helo")

        dispatcher.scheduler.advanceUntilIdle()
        assertEquals(1, (vm.results.value as AddTrackedUiState.Ok).data.size)
        coVerify { repo.searchTvShows(eq("helo")) }
    }

    @Test
    fun testPreviewsSearchIsCancelled() = runTest {
        val vm = AddTrackedViewModel(repo, trackedRepo, ShowSearchUiModelMapper(), ioDispatcher = dispatcher)
        vm.setSearchQuery("helo")
        vm.setSearchQuery("helo1")
        vm.setSearchQuery("helo2")
        vm.setSearchQuery("helo3")

        dispatcher.scheduler.advanceUntilIdle()
        assertEquals(1, (vm.results.value as AddTrackedUiState.Ok).data.size)
        coVerify(exactly = 1) { repo.searchTvShows(eq("helo3")) }
    }

    @Test
    fun testSetQuery() {
        val vm = AddTrackedViewModel(repo, trackedRepo, ShowSearchUiModelMapper(), ioDispatcher = dispatcher)
        vm.searchQuery.value = "helo"
        assertEquals(vm.searchQuery.value, "helo")
    }
}
