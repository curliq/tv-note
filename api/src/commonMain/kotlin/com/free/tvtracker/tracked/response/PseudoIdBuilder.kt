package com.free.tvtracker.tracked.response

fun buildPseudoId(contentType: TrackedContentApiModel.ContentType, tmdbId: Int): String {
    return "${contentType}_${tmdbId}"
}
