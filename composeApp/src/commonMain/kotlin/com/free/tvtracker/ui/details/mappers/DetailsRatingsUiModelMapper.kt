package com.free.tvtracker.ui.details.mappers

import com.free.tvtracker.base.Mapper
import com.free.tvtracker.data.reviews.OmdbReviewsResponse
import com.free.tvtracker.expect.CommonStringUtils
import com.free.tvtracker.ui.details.DetailsUiModel
import kotlin.math.ln
import kotlin.math.pow

class DetailsRatingsUiModelMapper(
    private val stringUtils: CommonStringUtils = CommonStringUtils(),
) : Mapper<OmdbReviewsResponse?, DetailsUiModel.Ratings?> {
    override fun map(from: OmdbReviewsResponse?): DetailsUiModel.Ratings? {
        if (from == null) return null
        return DetailsUiModel.Ratings(
            imdbRating = formatVoteAverage(from.imdbRating?.toDoubleOrNull()),
            imdbVoteCount = formatVoteCount(from.imdbVotes?.replace(",", "")?.toIntOrNull() ?: 0),
            tomatoesRatingPercentage = from.ratings?.firstOrNull { it.source == "Rotten Tomatoes" }?.value
        )
    }


    fun formatVoteAverage(average: Double?): String {
        return stringUtils.roundDouble((average ?: 0.toDouble()), 1) + "/10"
    }

    fun formatVoteCount(count: Int): String {
        if (count < 1000) return "" + count
        val exp = (ln(count.toDouble()) / ln(1000.0)).toInt()
        val shortenChar = count / 1000.0.pow(exp.toDouble())
        return stringUtils.shortenDouble(
            d = shortenChar,
            shortenCharacter = "kMGTPE"[exp - 1],
            decimalPoints = 1
        )
    }

}
