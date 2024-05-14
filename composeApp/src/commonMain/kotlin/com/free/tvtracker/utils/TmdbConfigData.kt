package com.free.tvtracker.utils

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
data class TmdbConfigData(
    @SerialName("images") var images: Images? = Images(),
    @SerialName("change_keys") var changeKeys: ArrayList<String> = arrayListOf()
) {
    @Serializable
    data class Images(
        @SerialName("base_url") var baseUrl: String? = null,
        @SerialName("secure_base_url") var secureBaseUrl: String? = null,
        @SerialName("backdrop_sizes") var backdropSizes: ArrayList<String> = arrayListOf(),
        @SerialName("logo_sizes") var logoSizes: ArrayList<String> = arrayListOf(),
        @SerialName("poster_sizes") var posterSizes: ArrayList<String> = arrayListOf(),
        @SerialName("profile_sizes") var profileSizes: ArrayList<String> = arrayListOf(),
        @SerialName("still_sizes") var stillSizes: ArrayList<String> = arrayListOf()
    )

    companion object {
        private var instance: TmdbConfigData? = null
        fun get(): TmdbConfigData {
            if (instance == null) {
                instance = Json.decodeFromString<TmdbConfigData>(data)
            }
            return instance!!
        }
    }

    fun getPosterUrl(path: String?, size: Int = 3): String {
        return "${images?.secureBaseUrl}${images?.posterSizes?.get(size)}${path ?: ""}"
    }

    fun getBackdropUrl(path: String?, size: Int = 1): String {
        return "${images?.secureBaseUrl}${images?.backdropSizes?.get(size)}${path ?: ""}"
    }

    fun getStillUrl(path: String?): String {
        return "${images?.secureBaseUrl}${images?.stillSizes?.get(1)}${path ?: ""}"
    }

    fun getLogoUrl(path: String?): String {
        return "${images?.secureBaseUrl}${images?.logoSizes?.get(4)}${path ?: ""}"
    }
}

/**
 * Get from https://developer.themoviedb.org/reference/configuration-details
 */
const val data = """
{
  "images": {
    "base_url": "http://image.tmdb.org/t/p/",
    "secure_base_url": "https://image.tmdb.org/t/p/",
    "backdrop_sizes": [
      "w300",
      "w780",
      "w1280",
      "original"
    ],
    "logo_sizes": [
      "w45",
      "w92",
      "w154",
      "w185",
      "w300",
      "w500",
      "original"
    ],
    "poster_sizes": [
      "w92",
      "w154",
      "w185",
      "w342",
      "w500",
      "w780",
      "original"
    ],
    "profile_sizes": [
      "w45",
      "w185",
      "h632",
      "original"
    ],
    "still_sizes": [
      "w92",
      "w185",
      "w300",
      "original"
    ]
  },
  "change_keys": [
    "adult",
    "air_date",
    "also_known_as",
    "alternative_titles",
    "biography",
    "birthday",
    "budget",
    "cast",
    "certifications",
    "character_names",
    "created_by",
    "crew",
    "deathday",
    "episode",
    "episode_number",
    "episode_run_time",
    "freebase_id",
    "freebase_mid",
    "general",
    "genres",
    "guest_stars",
    "homepage",
    "images",
    "imdb_id",
    "languages",
    "name",
    "network",
    "origin_country",
    "original_name",
    "original_title",
    "overview",
    "parts",
    "place_of_birth",
    "plot_keywords",
    "production_code",
    "production_companies",
    "production_countries",
    "releases",
    "revenue",
    "runtime",
    "season",
    "season_number",
    "season_regular",
    "spoken_languages",
    "status",
    "tagline",
    "title",
    "translations",
    "tvdb_id",
    "tvrage_id",
    "type",
    "video",
    "videos"
  ]
}
"""
