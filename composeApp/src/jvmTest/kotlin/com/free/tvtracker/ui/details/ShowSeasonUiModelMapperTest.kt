package com.free.tvtracker.ui.details

import com.free.tvtracker.details.response.TmdbShowDetailsApiModel
import com.free.tvtracker.tracked.response.TrackedContentApiModel
import com.free.tvtracker.ui.details.mappers.ShowEpisodeUiModelMapper
import com.free.tvtracker.ui.details.mappers.ShowSeasonUiModelMapper
import org.junit.Assert.assertEquals
import kotlin.test.Test

class ShowSeasonUiModelMapperTest {

    @Test
    fun `GIVEN all watched episodes THEN season is not watchable`() {
        val trackedShow = TrackedContentApiModel(
            watchlisted = false,
            tvShow = TrackedContentApiModel.TvShow(
                1, "2014-02-03",
                watchedEpisodes = listOf(
                    TrackedContentApiModel.TvShow.WatchedEpisodeApiModel("t_1", storedEpisodeId = 1),
                ),
                TrackedContentApiModel.TvShow.StoredShowApiModel(1, "game of thrones", emptyList(), "", "", "")
            ),
            mediaType = TrackedContentApiModel.ContentType.TvShow,
            movie = null,
            watchlists = emptyList(),
        )
        val sut = ShowSeasonUiModelMapper(ShowEpisodeUiModelMapper())
        val data = TmdbShowDetailsApiModel.Season(
            0, 0, "2000-01-01", null, "", null, null, 0.0,
            episodes = listOf(
                TmdbShowDetailsApiModel.Season.Episode(
                    id = 1, 0, "", "2020-03-02", "", 0,
                )
            ),
        )
        val uiModel = sut.map(data, ShowSeasonUiModelMapper.O(1, trackedShow))
        assertEquals(false, uiModel.isWatchable)
        assertEquals(true, uiModel.watched)
    }

    @Test
    fun `GIVEN 1 unwatched ep and 1 watched ep THEN season is watchable`() {
        val trackedShow = TrackedContentApiModel(
            watchlisted = false,
            tvShow = TrackedContentApiModel.TvShow(
                1, "2014-02-03",
                watchedEpisodes = listOf(
                    TrackedContentApiModel.TvShow.WatchedEpisodeApiModel("t_1", storedEpisodeId = 1),
                ),
                TrackedContentApiModel.TvShow.StoredShowApiModel(1, "game of thrones", emptyList(), "", "", "")
            ),
            mediaType = TrackedContentApiModel.ContentType.TvShow,
            movie = null,
            watchlists = emptyList(),
        )
        val sut = ShowSeasonUiModelMapper(ShowEpisodeUiModelMapper())
        val data = TmdbShowDetailsApiModel.Season(
            0, 0, "2000-01-01", null, "", null, null, 0.0,
            episodes = listOf(
                TmdbShowDetailsApiModel.Season.Episode(
                    id = 1, 0, "", "2020-03-02", "", 0,
                ),
                TmdbShowDetailsApiModel.Season.Episode(
                    id = 2, 0, "", "2020-03-02", "", 0,
                ),
            ),
        )
        val uiModel = sut.map(data, ShowSeasonUiModelMapper.O(1, trackedShow))
        assertEquals(true, uiModel.isWatchable)
        assertEquals(false, uiModel.watched)
    }
}
