package com.free.tvtracker.tracked.response

import com.free.tvtracker.base.ApiError
import com.free.tvtracker.base.ApiResponse
import kotlinx.serialization.Serializable

@Serializable
data class AddTrackedEpisodesApiResponse(
    override val data: List<TrackedContentApiModel.TvShow.WatchedEpisodeApiModel>? = null,
    override val application_error: ApiError? = null
) : ApiResponse<List<TrackedContentApiModel.TvShow.WatchedEpisodeApiModel>>() {
    companion object {
        fun ok(data: List<TrackedContentApiModel.TvShow.WatchedEpisodeApiModel>): AddTrackedEpisodesApiResponse {
            return AddTrackedEpisodesApiResponse(data = data)
        }

        fun error(application_error: ApiError): AddTrackedEpisodesApiResponse {
            return AddTrackedEpisodesApiResponse(application_error = application_error)
        }
    }
}
