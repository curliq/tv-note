package com.free.tvtracker.discover.response

import com.free.tvtracker.search.response.SmallMovieApiModel
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TrendingMoviesApiModel(
    @SerialName("page")
    val page: Int,
    @SerialName("total_pages")
    val totalPages: Int,
    @SerialName("results")
    val results: List<SmallMovieApiModel>
)
