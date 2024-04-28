package com.free.tvtracker.core.tmdb.data

import com.fasterxml.jackson.annotation.JsonProperty
import com.free.tvtracker.discover.response.TmdbShowDetailsApiModel.Images

data class TmdbShowImagesResponse(
    @JsonProperty("backdrops") var backdrops: List<Backdrops> = emptyList(),
    @JsonProperty("logos") var logos: List<Logos> = emptyList(),
    @JsonProperty("posters") var posters: List<Posters> = emptyList()
) {
    data class Backdrops(
        @JsonProperty("aspect_ratio") var aspectRatio: Double? = null,
        @JsonProperty("height") var height: Int? = null,
        @JsonProperty("iso_639_1") var iso6391: String? = null,
        @JsonProperty("file_path") var filePath: String? = null,
        @JsonProperty("vote_average") var voteAverage: Double? = null,
        @JsonProperty("vote_count") var voteCount: Int? = null,
        @JsonProperty("width") var width: Int? = null
    )

    data class Logos(
        @JsonProperty("aspect_ratio") var aspectRatio: Double? = null,
        @JsonProperty("height") var height: Int? = null,
        @JsonProperty("iso_639_1") var iso6391: String? = null,
        @JsonProperty("file_path") var filePath: String? = null,
        @JsonProperty("vote_average") var voteAverage: Double? = null,
        @JsonProperty("vote_count") var voteCount: Int? = null,
        @JsonProperty("width") var width: Int? = null
    )

    data class Posters(
        @JsonProperty("aspect_ratio") var aspectRatio: Double? = null,
        @JsonProperty("height") var height: Int? = null,
        @JsonProperty("iso_639_1") var iso6391: String? = null,
        @JsonProperty("file_path") var filePath: String? = null,
        @JsonProperty("vote_average") var voteAverage: Double? = null,
        @JsonProperty("vote_count") var voteCount: Int? = null,
        @JsonProperty("width") var width: Int? = null
    )

    fun toApiModel(): Images {
        return Images(
            backdrops = backdrops.map {
                Images.Backdrops(
                    aspectRatio = it.aspectRatio,
                    height = it.height,
                    filePath = it.filePath,
                    voteAverage = it.voteAverage,
                    voteCount = it.voteCount,
                    width = it.width,
                )
            },
            logos = logos.map {
                Images.Logos(
                    aspectRatio = it.aspectRatio,
                    height = it.height,
                    filePath = it.filePath,
                    voteAverage = it.voteAverage,
                    voteCount = it.voteCount,
                    width = it.width,
                )
            },
            posters = posters.map {
                Images.Posters(
                    aspectRatio = it.aspectRatio,
                    height = it.height,
                    filePath = it.filePath,
                    voteAverage = it.voteAverage,
                    voteCount = it.voteCount,
                    width = it.width,
                )
            }
        )
    }
}
