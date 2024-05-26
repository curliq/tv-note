package com.free.tvtracker.tmdb.data

import com.fasterxml.jackson.annotation.JsonProperty
import com.free.tvtracker.discover.response.TmdbShowDetailsApiModel


data class TmdbShowVideosResponse(
    @JsonProperty("results") var results: List<Results> = arrayListOf()
) {
    data class Results(
        @JsonProperty("iso_639_1") var iso6391: String? = null,
        @JsonProperty("iso_3166_1") var iso31661: String? = null,
        @JsonProperty("name") var name: String? = null,
        @JsonProperty("key") var key: String? = null,
        @JsonProperty("site") var site: String? = null,
        @JsonProperty("size") var size: Int? = null,
        @JsonProperty("type") var type: String? = null,
        @JsonProperty("official") var official: Boolean? = null,
        @JsonProperty("published_at") var publishedAt: String? = null,
        @JsonProperty("id") var id: String? = null
    ) {
        fun toApiModel(): TmdbShowDetailsApiModel.Video {
            return TmdbShowDetailsApiModel.Video(
                name = this.name,
                key = this.key,
                site = this.site,
                size = this.size,
                type = this.type,
                official = this.official,
                publishedAt = this.publishedAt,
                id = this.id,
            )
        }
    }
}
