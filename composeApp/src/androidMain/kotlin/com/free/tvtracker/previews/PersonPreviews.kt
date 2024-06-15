package com.free.tvtracker.previews

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.free.tvtracker.previews.PersonPreviews.personUiModel
import com.free.tvtracker.ui.person.PersonContent
import com.free.tvtracker.ui.person.PersonUiModel

object PersonPreviews {
    val personUiModel = PersonUiModel(
        photoUrl = "123",
        name = "timoti",
        job = "actor",
        dob = "10 feb 2020",
        bornIn = "france",
        bio = "lad",
        movies = listOf(
            PersonUiModel.Movie("", "1", 1, 0),
            PersonUiModel.Movie("", "2", 2, 0),
            PersonUiModel.Movie("", "3", 3, 0),
            PersonUiModel.Movie("", "4", 4, 0),
            PersonUiModel.Movie("", "5", 5, 0),
            PersonUiModel.Movie("", "6", 6, 0),
            PersonUiModel.Movie("", "7", 7, 0),
        ),
        firstTwoMovies = listOf(
            PersonUiModel.Movie("", "asd asd asd asd asd asd asd asd ", 1, 0),
            PersonUiModel.Movie("", "2", 2, 0),
        ),
        tvShowsCast = listOf(
            PersonUiModel.TvShow("", "1", 1, 0),
            PersonUiModel.TvShow("", "2", 2, 0),
            PersonUiModel.TvShow("", "3", 3, 0),
            PersonUiModel.TvShow("", "4", 4, 0),
            PersonUiModel.TvShow("", "5", 5, 0),
            PersonUiModel.TvShow("", "6", 6, 0),
            PersonUiModel.TvShow("", "7", 7, 0),
            PersonUiModel.TvShow("", "8", 8, 0),
        ),
        tvShowsCrew = listOf(
            PersonUiModel.TvShow("", "1", 1, 0),
            PersonUiModel.TvShow("", "2", 2, 0),
            PersonUiModel.TvShow("", "3", 3, 0),
            PersonUiModel.TvShow("", "4", 4, 0),
            PersonUiModel.TvShow("", "5", 5, 0),
            PersonUiModel.TvShow("", "6", 6, 0),
            PersonUiModel.TvShow("", "7", 7, 0),
            PersonUiModel.TvShow("", "8", 8, 0),
        ),
        firstTwoTvShows = listOf(
            PersonUiModel.TvShow("", "asd asd asd asd asd asd asd asd ", 1, 0),
            PersonUiModel.TvShow("", "2", 2, 0),
        ),
        photos = listOf("1", "2", "3", "4", "5", "6", "7", "8", "9"),
        firstTwoPhotos = listOf("1", "2"),
        tvShowsCount = 4,
        moviesCount = 3,
        instagramUrl = "",
        instagramTag = "@penelope"
    )
}

@Preview(heightDp = 1000)
@Composable
fun PersonScreenPreview() {
    MaterialTheme {
        Scaffold { a ->
            PersonContent(
                personUiModel,
                { }
            )
        }
    }
}
