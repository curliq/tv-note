package com.free.tvtracker.utils

import com.free.tvtracker.screens.details.DetailsUiModel
import com.free.tvtracker.screens.details.DetailsUiModel.Cast
import com.free.tvtracker.screens.details.DetailsUiModel.Crew
import com.free.tvtracker.screens.details.DetailsUiModel.Season
import com.free.tvtracker.screens.details.DetailsUiModel.Video
import com.free.tvtracker.screens.details.DetailsUiModel.WatchProvider

fun buildDetailsUiModel(
    tmdbId: Int = 0,
    name: String = "",
    posterUrl: String = "",
    releaseStatus: String = "",
    trackingStatus: String = "",
    homepageUrl: String? = null,
    description: String? = null,
    seasonsInfo: String? = null,
    seasons: List<Season>? = null,
    castFirst: Cast? = null,
    castSecond: Cast? = null,
    casts: List<Cast>? = null,
    crew: List<Crew>? = null,
    watchProviders: List<WatchProvider> = emptyList(),
    mediaTrailer: Video? = null,
    mediaVideosTrailers: List<Video> = emptyList(),
    mediaVideosTeasers: List<Video> = emptyList(),
    mediaVideosBehindTheScenes: List<Video> = emptyList(),
    mediaMostPopularImage: String? = null,
    mediaImagesPosters: List<String> = emptyList(),
    mediaImagesBackdrops: List<String> = emptyList()
) = DetailsUiModel(
    tmdbId = tmdbId,
    name = name,
    posterUrl = posterUrl,
    releaseStatus = releaseStatus,
    trackingStatus = trackingStatus,
    homepageUrl = homepageUrl,
    description = description,
    seasonsInfo = seasonsInfo,
    seasons = seasons,
    castFirst = castFirst,
    castSecond = castSecond,
    cast = casts,
    crew = crew,
    watchProviders = watchProviders,
    mediaTrailer = mediaTrailer,
    mediaVideosTrailers = mediaVideosTrailers,
    mediaVideosTeasers = mediaVideosTeasers,
    mediaVideosBehindTheScenes = mediaVideosBehindTheScenes,
    mediaMostPopularImage = mediaMostPopularImage,
    mediaImagesPosters = mediaImagesPosters,
    mediaImagesBackdrops = mediaImagesBackdrops,
)
