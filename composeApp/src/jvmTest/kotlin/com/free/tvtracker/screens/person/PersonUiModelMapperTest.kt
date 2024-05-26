package com.free.tvtracker.screens.person

import com.free.tvtracker.discover.response.MovieCredits
import com.free.tvtracker.discover.response.TmdbPersonDetailsApiModel
import com.free.tvtracker.discover.response.TvCredits
import io.mockk.mockk
import kotlinx.datetime.Instant
import org.junit.Assert.assertEquals
import kotlin.test.Test

class PersonUiModelMapperTest {
    @Test
    fun `GIVEN actor born in 1987 AND it's 2024 THEN age is 37`() {
        val sut = PersonUiModelMapper(mockk(), mockk(), mockk(), clock = Instant.parse("2024-05-01T00:00:00Z"))
        val data = TmdbPersonDetailsApiModel(
            id = 1,
            biography = "",
            birthday = "1987-01-09",
            deathday = "",
            imdbId = "",
            knownForDepartment = "",
            name = "",
            placeOfBirth = "",
            profilePath = "",
            tvCredits = TvCredits(emptyList(), emptyList()),
            movieCredits = MovieCredits(emptyList(), emptyList()),
            images = emptyList(),
            instagramId = "",
            tiktokId = "",
            twitterId = ""
        )
        val res = sut.map(data)
        assertEquals("9 January 1987 (age 37)", res.dob)
    }
}
