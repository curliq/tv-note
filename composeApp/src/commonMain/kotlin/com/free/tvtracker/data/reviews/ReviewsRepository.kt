package com.free.tvtracker.data.reviews

import com.free.tvtracker.expect.data.OmdbHttpClient
import com.free.tvtracker.expect.data.RapidApiHttpClient

class ReviewsRepository(
    private val omdbHttpClient: OmdbHttpClient,
    private val rapidHttpClient: RapidApiHttpClient,
) {
    suspend fun getRatings(imdbId: String): OmdbReviewsResponse? {
        return try {
            omdbHttpClient.getRatings<OmdbReviewsResponse>(imdbId)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    suspend fun getReviews(imdbId: String): RapidImdbReviewsResponse? {
        return try {
            rapidHttpClient.getReviews<RapidImdbReviewsResponse>(imdbId)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
