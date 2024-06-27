package com.free.tvtracker.ui.watching

import com.free.tvtracker.domain.GetNextUnwatchedEpisodeUseCase
import com.free.tvtracker.tracked.response.TrackedContentApiModel
import kotlin.test.Test
import kotlin.test.assertEquals

class GetNextUnwatchedEpisodeUseCaseTest {

    companion object {
        fun buildTrackedContent(episodeDates: List<String>, watchedEpisodeIds: List<Int>): TrackedContentApiModel {
            return TrackedContentApiModel(
                tvShow = TrackedContentApiModel.TvShow(
                    id = 1,
                    createdAtDatetime = "2014-02-03",
                    watchedEpisodes = watchedEpisodeIds.map {
                        TrackedContentApiModel.TvShow.WatchedEpisodeApiModel(it.toString(), it)
                    },
                    storedShow = TrackedContentApiModel.TvShow.StoredShowApiModel(
                        tmdbId = 1,
                        title = "game of thrones",
                        storedEpisodes = episodeDates.mapIndexed { index, date ->
                            TrackedContentApiModel.TvShow.StoredEpisodeApiModel(
                                id = index + 1,
                                season = 1,
                                episode = index + 1,
                                airDate = date
                            )
                        },
                        posterImage = "",
                        backdropImage = null,
                        status = ""
                    ),
                ),
                movie = null,
                mediaType = TrackedContentApiModel.ContentType.TvShow,
                watchlisted = false
            )
        }
    }

    @Test
    fun `GIVEN 3 episodes and 2 seen THEN 3rd one is returned`() {
        val show = buildTrackedContent(
            episodeDates = listOf("2020-01-01", "2020-01-01", "2020-01-01"),
            watchedEpisodeIds = listOf(1, 2)
        )
        val sut = GetNextUnwatchedEpisodeUseCase()
        assertEquals(3, sut(show = show)?.id)
    }

    @Test
    fun `GIVEN 3 episodes and 0 seen THEN 1rd one is returned`() {
        val show = buildTrackedContent(
            episodeDates = listOf("2020-01-01", "2020-01-01", "2020-01-01"),
            watchedEpisodeIds = listOf(0)
        )
        val sut = GetNextUnwatchedEpisodeUseCase()
        assertEquals(1, sut(show = show)?.id)
    }

    @Test
    fun `GIVEN 3 episodes and 3 seen THEN null is returned`() {
        val show = buildTrackedContent(
            episodeDates = listOf("2020-01-01", "2020-01-01", "2020-01-01"),
            watchedEpisodeIds = listOf(1, 2, 3)
        )
        val sut = GetNextUnwatchedEpisodeUseCase()
        assertEquals(null, sut(show = show))
    }

    @Test
    fun `GIVEN 3 episodes and only last one seen THEN null is returned`() {
        val show = buildTrackedContent(
            episodeDates = listOf("2020-01-01", "2020-01-01", "2020-01-01"),
            watchedEpisodeIds = listOf(3)
        )
        val sut = GetNextUnwatchedEpisodeUseCase()
        assertEquals(null, sut(show = show))
    }
}
