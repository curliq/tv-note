package com.free.tvtracker.discover.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TmdbPersonDetailsApiModel(
    val id: Long,
    val biography: String?,
    val birthday: String?,
    val deathday: String?,
    @SerialName("imdb_id")
    val imdbId: String?,
    @SerialName("known_for_department")
    val knownForDepartment: String,
    val name: String,
    @SerialName("place_of_birth")
    val placeOfBirth: String?,
    @SerialName("profile_path")
    val profilePath: String?,
    @SerialName("tv_credits")
    val tvCredits: TvCredits,
    @SerialName("movie_credits")
    val movieCredits: MovieCredits,
    @SerialName("images")
    val images: List<Profile>,
    @SerialName("instagram_id")
    val instagramId: String?,
    @SerialName("tiktok_id")
    val tiktokId: String?,
    @SerialName("twitter_id")
    val twitterId: String?,
)

@Serializable
data class TvCredits(
    val cast: List<Cast>,
    val crew: List<Crew>,
) {
    @Serializable
    data class Cast(
        val id: Long,
        @SerialName("poster_path")
        val posterPath: String?,
        @SerialName("name")
        val name: String?,
        @SerialName("vote_count")
        val voteCount: Long?,
    )

    @Serializable
    data class Crew(
        val id: Long,
        @SerialName("poster_path")
        val posterPath: String?,
        @SerialName("name")
        val name: String?,
        @SerialName("vote_count")
        val voteCount: Long?,
    )
}

@Serializable
data class MovieCredits(
    val cast: List<Cast>?,
    val crew: List<Crew>?,
) {

    @Serializable
    data class Cast(
        val id: Long,
        @SerialName("poster_path")
        val posterPath: String?,
        @SerialName("title")
        val title: String?,
        @SerialName("vote_count")
        val voteCount: Long?,
    )

    @Serializable
    data class Crew(
        val id: Long,
        @SerialName("poster_path")
        val posterPath: String?,
        @SerialName("title")
        val title: String?,
        @SerialName("vote_count")
        val voteCount: Long?,
    )
}

@Serializable
data class Profile(
    @SerialName("file_path")
    val filePath: String,
    @SerialName("vote_count")
    val voteCount: Long,
)
