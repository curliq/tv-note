package com.free.tvtracker.storage.shows.domain

import com.free.tvtracker.storage.shows.data.StoredEpisodeEntity
import com.free.tvtracker.storage.shows.data.StoredShowEntity
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import java.time.Clock
import java.time.Instant
import java.time.ZoneId
import kotlin.test.Test

class StoredEpisodesServiceTest {
    @Test
    fun `GIVEN 1 season with 1 upcoming ep THEN season refreshed`() {
        val eps = listOf(
            StoredEpisodeEntity(seasonNumber = 1, airDate = "2020-01-01"),
            StoredEpisodeEntity(seasonNumber = 1, airDate = "2020-01-01"),
            StoredEpisodeEntity(seasonNumber = 1, airDate = "2025-01-01"),
        )
        val data = StoredShowEntity(storedEpisodes = eps.toSet())
        val sut = StoredEpisodesService(
            mockk(),
            mockk(),
            mockk(),
            Clock.fixed(Instant.ofEpochSecond(1704067200), ZoneId.of("UTC")) // 2024-01-01
        )
        assertEquals(true, sut.shouldFetchSeason(1, data))
    }

    @Test
    fun `GIVEN 1 season with all past eps THEN season not refreshed`() {
        val eps = listOf(
            StoredEpisodeEntity(seasonNumber = 1, airDate = "2020-01-01"),
            StoredEpisodeEntity(seasonNumber = 1, airDate = "2020-01-01"),
            StoredEpisodeEntity(seasonNumber = 1, airDate = "2020-01-01"),
        )
        val data = StoredShowEntity(storedEpisodes = eps.toSet())
        val sut = StoredEpisodesService(
            mockk(),
            mockk(),
            mockk(),
            Clock.fixed(Instant.ofEpochSecond(1704067200), ZoneId.of("UTC")) // 2024-01-01
        )
        assertEquals(false, sut.shouldFetchSeason(1, data))
    }

    @Test
    fun `GIVEN only 1 season with all past eps THEN only season 2 refreshed`() {
        val eps = listOf(
            StoredEpisodeEntity(seasonNumber = 1, airDate = "2020-01-01"),
            StoredEpisodeEntity(seasonNumber = 1, airDate = "2020-01-01"),
            StoredEpisodeEntity(seasonNumber = 1, airDate = "2020-01-01"),
        )
        val data = StoredShowEntity(storedEpisodes = eps.toSet())
        val sut = StoredEpisodesService(
            mockk(),
            mockk(),
            mockk(),
            Clock.fixed(Instant.ofEpochSecond(1704067200), ZoneId.of("UTC")) // 2024-01-01
        )
        assertEquals(false, sut.shouldFetchSeason(1, data))
        assertEquals(true, sut.shouldFetchSeason(2, data))
    }

    @Test
    fun `GIVEN 1 season with all past eps and 1 season with upcoming ep THEN only season 2 refreshed`() {
        val eps = listOf(
            StoredEpisodeEntity(seasonNumber = 1, airDate = "2020-01-01"),
            StoredEpisodeEntity(seasonNumber = 1, airDate = "2020-01-01"),
            StoredEpisodeEntity(seasonNumber = 1, airDate = "2020-01-01"),
            StoredEpisodeEntity(seasonNumber = 2, airDate = "2020-01-01"),
            StoredEpisodeEntity(seasonNumber = 2, airDate = "2025-01-01"),
        )
        val data = StoredShowEntity(storedEpisodes = eps.toSet())
        val sut = StoredEpisodesService(
            mockk(),
            mockk(),
            mockk(),
            Clock.fixed(Instant.ofEpochSecond(1704067200), ZoneId.of("UTC")) // 2024-01-01
        )
        assertEquals(false, sut.shouldFetchSeason(1, data))
        assertEquals(true, sut.shouldFetchSeason(2, data))
    }
}
