package com.free.tvtracker.ui.search

import app.cash.turbine.test
import com.free.tvtracker.data.search.SearchRepository
import com.free.tvtracker.data.tracked.TrackedShowsRepository
import com.free.tvtracker.search.response.SearchApiModel
import com.free.tvtracker.search.response.SearchApiResponse
import com.free.tvtracker.search.response.SearchMultiApiModel
import com.free.tvtracker.search.response.SearchShowApiModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.count
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class AddTrackedViewModelTest {
    private val dispatcher = UnconfinedTestDispatcher()
    private val repo: SearchRepository = mockk {
        coEvery { searchAll(any()) } returns SearchApiResponse.ok(
            SearchApiModel(
                listOf(SearchMultiApiModel(1, name = "name", mediaType = "tv", overview = "overview", posterPath = "poster")),
            )
        )
    }
    private val trackedRepo: TrackedShowsRepository = mockk(relaxed = true) {
        every { allShows } returns MutableStateFlow(emptyList())
    }
    private lateinit var vm: AddTrackedViewModel

    @BeforeTest
    fun setup() {
        vm = AddTrackedViewModel(
            repo,
            trackedRepo,
            ShowSearchUiModelMapper(),
            MovieSearchUiModelMapper(),
            PersonSearchUiModelMapper(),
            ioDispatcher = dispatcher
        )
        vm.setOriginScreen(AddTrackedScreenOriginScreen.Watching)
    }

    @Test
    fun testPreviewsSearchIsSubbed() = runTest {
        vm.setSearchQuery("helo")

        dispatcher.scheduler.advanceUntilIdle()
        assertEquals(1, (vm.results.value as AddTrackedUiState.Ok).data.size)
        coVerify { repo.searchAll(eq("helo")) }
    }

    @Test
    fun testPreviewsSearchIsCancelled() = runTest {
        vm.setSearchQuery("helo")
        vm.setSearchQuery("helo1")
        vm.setSearchQuery("helo2")
        vm.setSearchQuery("helo3")

        dispatcher.scheduler.advanceUntilIdle()
        assertEquals(1, (vm.results.value as AddTrackedUiState.Ok).data.size)
        coVerify(exactly = 1) { repo.searchAll(eq("helo3")) }
    }

    @Test
    fun testSetQuery() {
        vm.searchQuery.value = "helo"
        assertEquals(vm.searchQuery.value, "helo")
    }
}
