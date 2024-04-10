package com.free.tvtracker.search.request

import kotlinx.serialization.Serializable

@Serializable
enum class MediaType { ALL, TV_SHOWS, MOVIES, PEOPLE }

@Serializable
data class SearchApiRequestBody(val term: String, val mediaType: MediaType)
