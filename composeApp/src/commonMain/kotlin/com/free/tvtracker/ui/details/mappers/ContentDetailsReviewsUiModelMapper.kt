package com.free.tvtracker.ui.details.mappers

import com.free.tvtracker.base.Mapper
import com.free.tvtracker.data.reviews.RapidImdbReviewsResponse
import com.free.tvtracker.ui.details.DetailsUiModel

class ContentDetailsReviewsUiModelMapper() : Mapper<RapidImdbReviewsResponse?, DetailsUiModel.Reviews?> {
    override fun map(from: RapidImdbReviewsResponse?): DetailsUiModel.Reviews? {
        return DetailsUiModel.Reviews(
            reviews = from?.data?.title?.reviews?.edges?.filter { it.node?.id != null }?.map { review ->
                DetailsUiModel.Reviews.Review(
                    id = review.node?.id!!,
                    authorName = review.node.author?.nickName ?: "",
                    title = review.node.summary?.originalText ?: "",
                    content = review.node.text?.originalText?.plainText ?: "",
                    created = review.node.submissionDate ?: "",
                )
            } ?: emptyList(),
            total = from?.data?.title?.reviews?.total ?: 0,
        )
    }
}
