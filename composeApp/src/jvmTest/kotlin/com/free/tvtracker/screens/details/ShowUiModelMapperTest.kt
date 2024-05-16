package com.free.tvtracker.screens.details

import com.free.tvtracker.discover.response.TmdbShowDetailsApiModel
import com.free.tvtracker.screens.details.mappers.ShowUiModelMapper
import com.free.tvtracker.constants.TmdbShowStatus
import io.mockk.mockk
import org.junit.Assert.assertEquals
import kotlin.test.Test

class ShowUiModelMapperTest {

    private val sut =
        ShowUiModelMapper(mockk(relaxed = true), mockk(relaxed = true), mockk(relaxed = true), mockk(relaxed = true))

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
        assertEquals("2015 - 2020", uiModel.releaseStatus)
    }
}
