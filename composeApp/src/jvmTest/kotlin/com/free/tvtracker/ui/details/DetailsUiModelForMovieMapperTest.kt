package com.free.tvtracker.ui.details

import com.free.tvtracker.details.response.TmdbMovieDetailsApiModel
import com.free.tvtracker.ui.details.mappers.DetailsRatingsUiModelMapper
import com.free.tvtracker.ui.details.mappers.DetailsUiModelForMovieMapper
import io.mockk.mockk
import org.junit.Assert.assertEquals
import kotlin.test.Test

class DetailsUiModelForMovieMapperTest {

    private val sut =
        DetailsUiModelForMovieMapper(
            mockk(relaxed = true),
            mockk(relaxed = true),
            mockk(relaxed = true),
            mockk(relaxed = true),
            mockk(relaxed = true),
            ratingsUiModelMapper = DetailsRatingsUiModelMapper()
        )

    @Test
    fun `GIVEN movie from 2015 THEN copy is date range`() {
        val data = TmdbMovieDetailsApiModel(
            id = 1,
            title = "Goodfellas",
            releaseDate = "2015-01-01",
        )
        val uiModel = sut.map(data, null)
        assertEquals("1 January 2015", uiModel.releaseStatus)
    }

    @Test
    fun `GIVEN movie with 400 votes THEN vote count is 400`() {
        val data = TmdbMovieDetailsApiModel(
            id = 1,
            title = "Goodfellas",
            voteCount = 400,
        )
        val uiModel = sut.map(data, null)
        assertEquals("400", uiModel.ratingTmdbVoteCount)
    }

    @Test
    fun `GIVEN movie with 2000 votes THEN vote count is 2,0k`() {
        val data = TmdbMovieDetailsApiModel(id = 1, title = "goodfellas", voteCount = 2000)
        val uiModel = sut.map(data, null)
        assertEquals("2.0k", uiModel.ratingTmdbVoteCount)
    }

    @Test
    fun `GIVEN movie with 2500 votes THEN vote count is 2,5k`() {
        val data = TmdbMovieDetailsApiModel(id = 1, title = "goodfellas", voteCount = 2500)
        val uiModel = sut.map(data, null)
        assertEquals("2.5k", uiModel.ratingTmdbVoteCount)
    }

    @Test
    fun `GIVEN movie with 40000 votes THEN vote count is 40,0k`() {
        val data = TmdbMovieDetailsApiModel(id = 1, title = "goodfellas", voteCount = 40000)
        val uiModel = sut.map(data, null)
        assertEquals("40.0k", uiModel.ratingTmdbVoteCount)
    }

    @Test
    fun `GIVEN movie with 23145 votes THEN vote count is 23,1k`() {
        val data = TmdbMovieDetailsApiModel(id = 1, title = "goodfellas", voteCount = 23145)
        val uiModel = sut.map(data, null)
        assertEquals("23.1k", uiModel.ratingTmdbVoteCount)
    }

    @Test
    fun `GIVEN movie with 7,56 avg rate THEN rating is 7,6 out of 10`() {
        val data = TmdbMovieDetailsApiModel(id = 1, title = "goodfellas", voteAverage = 7.56)
        val uiModel = sut.map(data, null)
        assertEquals("7.6/10", uiModel.ratingTmdbVoteAverage)
    }
}
