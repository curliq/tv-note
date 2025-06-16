package com.free.tvtracker.ui.watching

import com.free.tvtracker.domain.GetNextUnwatchedEpisodeUseCase
import com.free.tvtracker.tracked.response.TrackedContentApiModel
import kotlin.test.Test
import kotlin.test.assertEquals

class WatchingShowUiModelMapperTest {
    @Test
    fun `GIVEN show with all (1) episodes watched WHEN get watchable THEN its empty`() {
        val sut = WatchingShowUiModelMapper(GetNextUnwatchedEpisodeUseCase())
        val res = sut.map(
            from = TrackedContentApiModel(
                watchlisted = false,
                tvShow = TrackedContentApiModel.TvShow(
                    1, "2014-02-03",
                    watchedEpisodes = listOf(
                        TrackedContentApiModel.TvShow.WatchedEpisodeApiModel("t_1", storedEpisodeId = 1),
                    ),
                    TrackedContentApiModel.TvShow.StoredShowApiModel(
                        1, "game of thrones", listOf(
                            TrackedContentApiModel.TvShow.StoredEpisodeApiModel(1, 1, 1, "2014-02-03"),
                            TrackedContentApiModel.TvShow.StoredEpisodeApiModel(2, 1, 2, "2014-02-03"),
                        ), "", "", ""
                    )
                ),
                mediaType = TrackedContentApiModel.ContentType.TvShow,
                movie = null,
                watchlists = emptyList()
            )
        )
        assertEquals("Available on Mon 3rd Feb 2014", res.nextEpisodeCountdown)
    }
}
