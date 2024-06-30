package com.free.tvtracker.tmdb.data

import com.fasterxml.jackson.annotation.JsonProperty

data class TmdbMovieCollectionResponse(
    val id:Int,
    val name:String?,
    val overview:String?,
    @JsonProperty("backdrop_path")
    val backdropPath: String?,
    @JsonProperty("poster_path")
    val posterPath: String?,
    val parts: List<TmdbMovieSmallResponse> = emptyList()
)
