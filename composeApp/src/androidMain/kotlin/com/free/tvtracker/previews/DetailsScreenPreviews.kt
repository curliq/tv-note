package com.free.tvtracker.previews

import android.annotation.SuppressLint
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.free.tvtracker.ui.common.theme.TvTrackerTheme
import com.free.tvtracker.previews.DetailsScreenPreviews.showDetailsUiModel
import com.free.tvtracker.ui.details.DetailsScreenContent
import com.free.tvtracker.ui.details.DetailsUiModel

object DetailsScreenPreviews {
    val showDetailsUiModel = DetailsUiModel(
        tmdbId = 1,
        name = "game of thrones thrones thornes",
        posterUrl = "",
        homepageUrl = "",
        releaseStatus = "2014 - Ongoing",
        trackingStatus = DetailsUiModel.TrackingStatus(null, null),
        trackedShowId = null,
        description = "game of thrones is a show about society",
        seasonsInfo = "2 seasons - 16 episodes - 0h40m each",
        castFirst = DetailsUiModel.Cast(1, "William Dicksdoor 2 lines", "King Joffrey", ""),
        castSecond = DetailsUiModel.Cast(1, "Peter O'mo", "Joffrey's brother", ""),
        cast = listOf(
            DetailsUiModel.Cast(1, "William Dicksdoor 2 lines", "King Joffrey", ""),
            DetailsUiModel.Cast(1, "William Dicksdoor 2 lines", "King Joffrey", ""),
            DetailsUiModel.Cast(1, "William Dicksdoor 2 lines", "King Joffrey", ""),
            DetailsUiModel.Cast(1, "William Dicksdoor 2 lines", "King Joffrey", ""),
        ),
        crew = listOf(
            DetailsUiModel.Crew(1, "William Dicksdoor 2 lines", "Director", ""),
            DetailsUiModel.Crew(1, "William Dicksdoor 2 lines", "Director", ""),
            DetailsUiModel.Crew(1, "William Dicksdoor 2 lines", "Director", ""),
            DetailsUiModel.Crew(1, "William Dicksdoor 2 lines", "Director", ""),
        ),
        watchProviders = listOf(
            DetailsUiModel.WatchProvider("", ""),
            DetailsUiModel.WatchProvider("", ""),
            DetailsUiModel.WatchProvider("", ""),
            DetailsUiModel.WatchProvider("", ""),
            DetailsUiModel.WatchProvider("", ""),
            DetailsUiModel.WatchProvider("", ""),
        ),
        seasons = listOf(
            DetailsUiModel.Season(
                1, 1, "", false, true,
                listOf(
                    DetailsUiModel.Season.Episode(
                        1, "ep 1", "1", "name", "date", false, true,
                    ),
                    DetailsUiModel.Season.Episode(
                        1, "ep 1", "1", "name", "date", false, true,
                    ),
                    DetailsUiModel.Season.Episode(
                        1, "ep 1", "1", "name", "date", false, true,
                    ),
                ),
            ),
            DetailsUiModel.Season(
                1, 1, "", false, true,
                listOf(
                    DetailsUiModel.Season.Episode(
                        1, "ep 1", "1", "name", "date", true, true,
                    ),
                    DetailsUiModel.Season.Episode(
                        1,
                        "",
                        "1",
                        "ep 1123 12 3123123123 3231323 3 3dasd asd asdasdasd 2",
                        "date",
                        true,
                        true
                    ),
                    DetailsUiModel.Season.Episode(
                        1, "ep 1", "1", "name", "date", true, true,
                    ),
                ),
            )
        ),
        mediaTrailer = DetailsUiModel.Video("thumbnail url", "video url", "video title"),
        mediaVideosTrailers = listOf(
            DetailsUiModel.Video("thumbnail url", "video url", "video title"),
            DetailsUiModel.Video("thumbnail url", "video url", "video title title itle rijelrj er"),
            DetailsUiModel.Video("thumbnail url", "video url", "video title"),
            DetailsUiModel.Video("thumbnail url", "video url", "video title"),
            DetailsUiModel.Video("thumbnail url", "video url", "video title"),
            DetailsUiModel.Video("thumbnail url", "video url", "video title"),
        ),
        mediaVideosTeasers = listOf(
            DetailsUiModel.Video("thumbnail url", "video url", "video title"),
            DetailsUiModel.Video("thumbnail url", "video url", "video title"),
        ),
        mediaVideosBehindTheScenes = listOf(
            DetailsUiModel.Video("thumbnail url", "video url", "video title"),
            DetailsUiModel.Video("thumbnail url", "video url", "video title"),
        ),
        mediaMostPopularImage = "url",
        mediaImagesPosters = listOf("url", "url", "url", "url", "url", "url", "url"),
        mediaImagesBackdrops = listOf("url", "url", "url", "url", "url", "url", "url"),
        genres = "action, anime",
        ratingTmdbVoteAverage = "9/10",
        ratingTmdbVoteCount = "40k"
    )
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Preview(heightDp = 1600)
@Composable
fun DetailsScreenPreview() {
    TvTrackerTheme {
        Scaffold { padding ->
            DetailsScreenContent(showDetailsUiModel, {}, {})
        }
    }
}
