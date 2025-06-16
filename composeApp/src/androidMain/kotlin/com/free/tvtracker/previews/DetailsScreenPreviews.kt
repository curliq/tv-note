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
        isTvShow = true,
        tmdbId = 1,
        name = "game of thrones thrones thornes",
        posterUrl = "",
        releaseStatus = "2014 - Ongoing",
        duration = null,
        trackingStatus = DetailsUiModel.TrackingStatus(
            DetailsUiModel.TrackingStatus.Action.MoveToWatching,
            DetailsUiModel.TrackingStatus.Action.TrackWatchlist
        ),
        trackedContentId = null,
        homepageUrl = "",
        description = "game of thrones is a show about society",
        genres = listOf("action", "anime", "drama", "thriller", "pg13"),
        seasonsInfo = "2 seasons - 16 episodes",
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
        movieSeries = DetailsUiModel.MovieSeries(
            "movie about stuff", listOf(
                DetailsUiModel.MovieSeries.Movie(1, "", "movie 1", "2020"),
                DetailsUiModel.MovieSeries.Movie(1, "", "movie 2", "2020"),
                DetailsUiModel.MovieSeries.Movie(1, "", "movie 3", "2020"),
            )
        ),
        castFirst = DetailsUiModel.Cast(1, "William Dicksdoor 2 lines", "King Joffrey", ""),
        castSecond = DetailsUiModel.Cast(1, "Peter O'mo", "Joffrey's brother", ""),
        cast = listOf(
            DetailsUiModel.Cast(1, "William Dicksdoor 2 lines", "King Joffrey", ""),
            DetailsUiModel.Cast(1, "William Dicksdoor 2 lines", "King Joffrey", ""),
            DetailsUiModel.Cast(1, "William Dicksdoor 2 lines", "King Joffrey", ""),
            DetailsUiModel.Cast(1, "William Dicksdoor 2 lines", "King Joffrey", ""),
        ),
        watchProviders = listOf(
            DetailsUiModel.WatchProvider("", ""),
            DetailsUiModel.WatchProvider("", ""),
            DetailsUiModel.WatchProvider("", ""),
            DetailsUiModel.WatchProvider("", ""),
            DetailsUiModel.WatchProvider("", ""),
            DetailsUiModel.WatchProvider("", ""),
        ),
        crew = listOf(
            DetailsUiModel.Crew(1, "William Dicksdoor 2 lines", "Director", ""),
            DetailsUiModel.Crew(1, "William Dicksdoor 2 lines", "Director", ""),
            DetailsUiModel.Crew(1, "William Dicksdoor 2 lines", "Director", ""),
            DetailsUiModel.Crew(1, "William Dicksdoor 2 lines", "Director", ""),
        ),
        watchProviderCountry = "UK",
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
        mediaVideosClipsAndOther = listOf(),
        mediaMostPopularImage = "url",
        mediaImagesPosters = listOf("url", "url", "url", "url", "url", "url", "url"),
        mediaImagesBackdrops = listOf("url", "url", "url", "url", "url", "url", "url"),
        ratingTmdbVoteAverage = "9/10",
        ratingTmdbVoteCount = "40k",
        budget = "10k",
        revenue = "20k",
        website = "www.google.com",
        omdbRatings = DetailsUiModel.Ratings(
            imdbVoteCount = "200,131",
            imdbRating = "8.4",
            tomatoesRatingPercentage = null
        ),
        reviews = DetailsUiModel.Reviews(
            reviews = listOf(
                DetailsUiModel.Reviews.Review(
                    id = "1",
                    authorName = "William Dicksdoor",
                    title = "great",
                    content = "This is a great movie",
                    created = "10/10/2023",
                ),
                DetailsUiModel.Reviews.Review(
                    id = "2",
                    authorName = "William Dicksdoor",
                    title = "great x2",
                    content = "This is a great movie!!",
                    created = "10/10/2023",
                ),
            ),
            total = 30
        ),
        watchlists = emptyList(),
        watchlisted = true,
        isFinished = false,
        isWatching = true
    )
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Preview(heightDp = 2000)
@Composable
fun DetailsScreenShowPreview() {
    TvTrackerTheme {
        Scaffold { padding ->
            DetailsScreenContent(showDetailsUiModel, true, {}, {})
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Preview(heightDp = 2000)
@Composable
fun DetailsScreenMoviePreview() {
    TvTrackerTheme {
        Scaffold { padding ->
            DetailsScreenContent(showDetailsUiModel.copy(isTvShow = false), false, {}, {})
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Preview(heightDp = 2000)
@Composable
fun DetailsScreenShowLoadingPreview() {
    TvTrackerTheme {
        Scaffold { padding ->
            DetailsScreenContent(
                showDetailsUiModel.copy(
                    trackingStatus = DetailsUiModel.TrackingStatus(
                        DetailsUiModel.TrackingStatus.Action.MoveToWatching,
                        DetailsUiModel.TrackingStatus.Action.TrackWatchlist,
                        isLoading = true
                    )
                ), true, {}, {}
            )
        }
    }
}
