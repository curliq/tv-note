package com.free.tvtracker.ui.details

import com.free.tvtracker.details.response.TmdbShowDetailsApiModel
import com.free.tvtracker.ui.details.mappers.ContentDetailsUiModelForShowMapper
import com.free.tvtracker.constants.TmdbShowStatus
import com.free.tvtracker.domain.GetShowStatusUseCase
import com.free.tvtracker.ui.details.mappers.ContentDetailsRatingsUiModelMapper
import io.mockk.mockk
import org.junit.Assert.assertEquals
import kotlin.test.Test

class DetailsUiModelForShowMapperTest {

    private val sut =
        ContentDetailsUiModelForShowMapper(
            mockk(relaxed = true),
            mockk(relaxed = true),
            mockk(relaxed = true),
            mockk(relaxed = true),
            mockk(relaxed = true),
            mockk(relaxed = true),
            GetShowStatusUseCase(),
            mockk(relaxed = true),
            ContentDetailsRatingsUiModelMapper()
        )

    @Test
    fun `GIVEN ongoing show from 2015 THEN copy is ongoing`() {
        val data = TmdbShowDetailsApiModel(
            id = 1,
            name = "game of thrones",
            status = TmdbShowStatus.RETURNING.status,
            firstAirDate = "2015-01-01"
        )
        val uiModel = sut.map(data, null)
        assertEquals("2015 - Ongoing", uiModel.releaseStatus)
    }

    @Test
    fun `GIVEN ended show from 2015 THEN copy is date range`() {
        val data = TmdbShowDetailsApiModel(
            id = 1,
            name = "game of thrones",
            status = TmdbShowStatus.ENDED.status,
            firstAirDate = "2015-01-01",
            lastAirDate = "2020-01-01"
        )
        val uiModel = sut.map(data, null)
        assertEquals("2015 - 2020 (Ended)", uiModel.releaseStatus)
    }

    @Test
    fun `GIVEN show with 400 votes THEN vote count is 400` () {
        val data  = TmdbShowDetailsApiModel(1,"","", voteCount = 400)
        val uiModel = sut.map(data, null)
        assertEquals("400", uiModel.ratingTmdbVoteCount)
    }

    @Test
    fun `GIVEN show with 2000 votes THEN vote count is 2,0k` () {
        val data  = TmdbShowDetailsApiModel(1,"","", voteCount = 2000)
        val uiModel = sut.map(data, null)
        assertEquals("2.0k", uiModel.ratingTmdbVoteCount)
    }

    @Test
    fun `GIVEN show with 2500 votes THEN vote count is 2,5k` () {
        val data  = TmdbShowDetailsApiModel(1,"","", voteCount = 2500)
        val uiModel = sut.map(data, null)
        assertEquals("2.5k", uiModel.ratingTmdbVoteCount)
    }

    @Test
    fun `GIVEN show with 40000 votes THEN vote count is 40,0k` () {
        val data  = TmdbShowDetailsApiModel(1,"","", voteCount = 40000)
        val uiModel = sut.map(data, null)
        assertEquals("40.0k", uiModel.ratingTmdbVoteCount)
    }

    @Test
    fun `GIVEN show with 23145 votes THEN vote count is 23,1k` () {
        val data  = TmdbShowDetailsApiModel(1,"","", voteCount = 23145)
        val uiModel = sut.map(data, null)
        assertEquals("23.1k", uiModel.ratingTmdbVoteCount)
    }

    @Test
    fun `GIVEN show with 7,56 avg rate THEN rating is 7,6 out of 10` () {
        val data  = TmdbShowDetailsApiModel(1,"","", voteAverage = 7.56)
        val uiModel = sut.map(data, null)
        assertEquals("7.6/10", uiModel.ratingTmdbVoteAverage)
    }
}
