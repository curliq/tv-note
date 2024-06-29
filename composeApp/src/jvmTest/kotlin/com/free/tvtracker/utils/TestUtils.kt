package com.free.tvtracker.utils

import com.free.tvtracker.ui.details.DetailsUiModel
import com.free.tvtracker.ui.details.DetailsUiModel.Cast
import com.free.tvtracker.ui.details.DetailsUiModel.Crew
import com.free.tvtracker.ui.details.DetailsUiModel.Season
import com.free.tvtracker.ui.details.DetailsUiModel.Video
import com.free.tvtracker.ui.details.DetailsUiModel.WatchProvider

fun buildDetailsUiModel(
    tmdbId: Int = 0,
    name: String = "",
    posterUrl: String = "",
    releaseStatus: String = "",
    trackingStatus: DetailsUiModel.TrackingStatus = DetailsUiModel.TrackingStatus(null, null),
    trackingId: Int? = null,
    homepageUrl: String? = null,
    description: String? = null,
    duration: String? = null,
    genres: String = "",
    seasonsInfo: String? = null,
    seasons: List<Season>? = null,
    castFirst: Cast? = null,
    castSecond: Cast? = null,
    casts: List<Cast> = emptyList(),
    crew: List<Crew> = emptyList(),
    watchProviders: List<WatchProvider> = emptyList(),
    watchProvidersCountry: String = "",
    mediaTrailer: Video? = null,
    mediaVideosTrailers: List<Video> = emptyList(),
    mediaVideosTeasers: List<Video> = emptyList(),
    mediaVideosBehindTheScenes: List<Video> = emptyList(),
    mediaMostPopularImage: String? = null,
    mediaImagesPosters: List<String> = emptyList(),
    mediaImagesBackdrops: List<String> = emptyList(),
    ratingTmdbVoteAverage: String = "",
    ratingTmdbVoteCount: String = "",
    isTvShow: Boolean = true,
) = DetailsUiModel(
    tmdbId = tmdbId,
    name = name,
    posterUrl = posterUrl,
    releaseStatus = releaseStatus,
    trackingStatus = trackingStatus,
    trackedContentId = trackingId,
    duration = duration,
    homepageUrl = homepageUrl,
    description = description,
    genres = genres,
    seasonsInfo = seasonsInfo,
    seasons = seasons,
    castFirst = castFirst,
    castSecond = castSecond,
    cast = casts,
    crew = crew,
    watchProviders = watchProviders,
    watchProviderCountry = watchProvidersCountry,
    mediaTrailer = mediaTrailer,
    mediaVideosTrailers = mediaVideosTrailers,
    mediaVideosTeasers = mediaVideosTeasers,
    mediaVideosBehindTheScenes = mediaVideosBehindTheScenes,
    mediaMostPopularImage = mediaMostPopularImage,
    mediaImagesPosters = mediaImagesPosters,
    mediaImagesBackdrops = mediaImagesBackdrops,
    ratingTmdbVoteAverage = ratingTmdbVoteAverage,
    ratingTmdbVoteCount = ratingTmdbVoteCount,
    isTvShow = isTvShow
)
