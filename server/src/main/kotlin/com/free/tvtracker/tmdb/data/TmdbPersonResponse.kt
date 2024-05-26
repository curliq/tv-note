package com.free.tvtracker.tmdb.data

import com.fasterxml.jackson.annotation.JsonProperty

data class TmdbPersonResponse(
    val id: Long,
    @JsonProperty("also_known_as")
    val alsoKnownAs: List<String>,
    val biography: String?,
    val birthday: String?,
    val deathday: String?,
    val gender: Long?,
    val homepage: String?,
    @JsonProperty("imdb_id")
    val imdbId: String?,
    @JsonProperty("known_for_department")
    val knownForDepartment: String,
    val name: String,
    @JsonProperty("place_of_birth")
    val placeOfBirth: String?,
    val popularity: Double,
    @JsonProperty("profile_path")
    val profilePath: String?,
    @JsonProperty("tv_credits")
    val tvCredits: TvCredits,
    @JsonProperty("movie_credits")
    val movieCredits: MovieCredits,
    @JsonProperty("images")
    val images: Images,
    @JsonProperty("external_ids")
    val externalIds: ExternalIds
)

data class TvCredits(
    @JsonProperty("cast")
    val cast: List<Cast>? = null,
    @JsonProperty("crew")
    val crew: List<Crew>? = null,
)

data class Cast(
    @JsonProperty("id")
    val id: Long,
    @JsonProperty("backdrop_path")
    val backdropPath: String?,
    @JsonProperty("genre_ids")
    val genreIds: List<Long>?,
    @JsonProperty("origin_country")
    val originCountry: List<String>?,
    @JsonProperty("original_language")
    val originalLanguage: String?,
    @JsonProperty("original_name")
    val originalName: String?,
    @JsonProperty("overview")
    val overview: String?,
    @JsonProperty("popularity")
    val popularity: Double?,
    @JsonProperty("poster_path")
    val posterPath: String?,
    @JsonProperty("first_air_date")
    val firstAirDate: String?,
    @JsonProperty("name")
    val name: String?,
    @JsonProperty("vote_average")
    val voteAverage: Double?,
    @JsonProperty("vote_count")
    val voteCount: Long?,
    @JsonProperty("character")
    val character: String?,
    @JsonProperty("credit_id")
    val creditId: String?,
    @JsonProperty("episode_count")
    val episodeCount: Long?,
)

data class Crew(
    @JsonProperty("backdrop_path")
    val backdropPath: String?,
    @JsonProperty("genre_ids")
    val genreIds: List<Long>?,
    @JsonProperty("id")
    val id: Long,
    @JsonProperty("origin_country")
    val originCountry: List<String>?,
    @JsonProperty("original_language")
    val originalLanguage: String?,
    @JsonProperty("original_name")
    val originalName: String?,
    @JsonProperty("overview")
    val overview: String?,
    @JsonProperty("popularity")
    val popularity: Double?,
    @JsonProperty("poster_path")
    val posterPath: String?,
    @JsonProperty("first_air_date")
    val firstAirDate: String?,
    @JsonProperty("name")
    val name: String?,
    @JsonProperty("vote_average")
    val voteAverage: Double?,
    @JsonProperty("vote_count")
    val voteCount: Long?,
    @JsonProperty("credit_id")
    val creditId: String?,
    @JsonProperty("department")
    val department: String?,
    @JsonProperty("episode_count")
    val episodeCount: Long?,
    @JsonProperty("job")
    val job: String?,
)

data class MovieCredits(
    @JsonProperty("cast")
    val cast: List<Cast2>?,
    @JsonProperty("crew")
    val crew: List<Crew2>?,
)

data class Cast2(
    val adult: Boolean?,
    @JsonProperty("backdrop_path")
    val backdropPath: String?,
    @JsonProperty("genre_ids")
    val genreIds: List<Long>?,
    @JsonProperty("id")
    val id: Long,
    @JsonProperty("original_language")
    val originalLanguage: String?,
    @JsonProperty("original_title")
    val originalTitle: String?,
    @JsonProperty("overview")
    val overview: String?,
    @JsonProperty("popularity")
    val popularity: Double?,
    @JsonProperty("poster_path")
    val posterPath: String?,
    @JsonProperty("release_date")
    val releaseDate: String?,
    @JsonProperty("title")
    val title: String?,
    @JsonProperty("video")
    val video: Boolean?,
    @JsonProperty("vote_average")
    val voteAverage: Double?,
    @JsonProperty("vote_count")
    val voteCount: Long?,
    @JsonProperty("character")
    val character: String?,
    @JsonProperty("credit_id")
    val creditId: String?,
    @JsonProperty("order")
    val order: Long?,
)

data class Crew2(
    val adult: Boolean?,
    @JsonProperty("backdrop_path")
    val backdropPath: String?,
    @JsonProperty("genre_ids")
    val genreIds: List<Long>?,
    @JsonProperty("id")
    val id: Long,
    @JsonProperty("original_language")
    val originalLanguage: String?,
    @JsonProperty("original_title")
    val originalTitle: String?,
    @JsonProperty("overview")
    val overview: String?,
    @JsonProperty("popularity")
    val popularity: Double?,
    @JsonProperty("poster_path")
    val posterPath: String?,
    @JsonProperty("release_date")
    val releaseDate: String?,
    @JsonProperty("title")
    val title: String?,
    @JsonProperty("video")
    val video: Boolean?,
    @JsonProperty("vote_average")
    val voteAverage: Double?,
    @JsonProperty("vote_count")
    val voteCount: Long?,
    @JsonProperty("credit_id")
    val creditId: String?,
    @JsonProperty("department")
    val department: String?,
    @JsonProperty("job")
    val job: String?,
)

data class Images(
    @JsonProperty("profiles")
    val profiles: List<Profile>,
)

data class Profile(
    @JsonProperty("aspect_ratio")
    val aspectRatio: Double?,
    @JsonProperty("height")
    val height: Long?,
    @JsonProperty("file_path")
    val filePath: String,
    @JsonProperty("vote_average")
    val voteAverage: Double?,
    @JsonProperty("vote_count")
    val voteCount: Long,
    @JsonProperty("width")
    val width: Long?,
)

data class ExternalIds(
    @JsonProperty("instagram_id")
    val instagramId: String?,
    @JsonProperty("tiktok_id")
    val tiktokId: String?,
    @JsonProperty("twitter_id")
    val twitterId: String?,
    @JsonProperty("imdb_id")
    val imdbId: String?,
)
