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
        moviesCast = listOf(
            PersonUiModel.Credit(1, "", "1", 1, false),
            PersonUiModel.Credit(1, "", "2", 2, false),
            PersonUiModel.Credit(1, "", "3", 3, false),
            PersonUiModel.Credit(1, "", "4", 4, false),
            PersonUiModel.Credit(1, "", "5", 5, false),
            PersonUiModel.Credit(1, "", "6", 6, false),
            PersonUiModel.Credit(1, "", "7", 7, false),
        ),
        moviesCrew = listOf(
            PersonUiModel.Credit(1, "", "1", 1, false),
            PersonUiModel.Credit(1, "", "2", 2, false),
            PersonUiModel.Credit(1, "", "3", 3, false),
            PersonUiModel.Credit(1, "", "4", 4, false),
            PersonUiModel.Credit(1, "", "5", 5, false),
            PersonUiModel.Credit(1, "", "6", 6, false),
            PersonUiModel.Credit(1, "", "7", 7, false),
        ),
        firstTwoMovies = listOf(
            PersonUiModel.Credit(1, "", "asd asdfalseasd asd asd asd asd asd ", 1, false),
            PersonUiModel.Credit(1, "", "2", 2, false),
        ),
        tvShowsCast = listOf(
            PersonUiModel.Credit(1, "", "1", 1, true),
            PersonUiModel.Credit(1, "", "2", 2, true),
            PersonUiModel.Credit(1, "", "3", 3, true),
            PersonUiModel.Credit(1, "", "4", 4, true),
            PersonUiModel.Credit(1, "", "5", 5, true),
            PersonUiModel.Credit(1, "", "6", 6, true),
            PersonUiModel.Credit(1, "", "7", 7, true),
            PersonUiModel.Credit(1, "", "8", 8, true),
        ),
        tvShowsCrew = listOf(
            PersonUiModel.Credit(1, "", "1", 1, true),
            PersonUiModel.Credit(1, "", "2", 2, true),
            PersonUiModel.Credit(1, "", "3", 3, true),
            PersonUiModel.Credit(1, "", "4", 4, true),
            PersonUiModel.Credit(1, "", "5", 5, true),
            PersonUiModel.Credit(1, "", "6", 6, true),
            PersonUiModel.Credit(1, "", "7", 7, true),
            PersonUiModel.Credit(1, "", "8", 8, true),
        ),
        firstTwoTvShows = listOf(
            PersonUiModel.Credit(1, "", "asd asd asd asd asd asd asd asd ", 1, true),
            PersonUiModel.Credit(1, "", "2", 2, true),
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
