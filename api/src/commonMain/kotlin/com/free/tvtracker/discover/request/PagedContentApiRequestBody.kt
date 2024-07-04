package com.free.tvtracker.discover.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PagedContentApiRequestBody(
    @SerialName("page")
    val page: Int,
)
