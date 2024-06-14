package com.free.tvtracker.features.discover

import com.free.tvtracker.discover.request.RecommendedContentApiRequestBody
import com.free.tvtracker.discover.response.RecommendedContentApiModel
import com.free.tvtracker.discover.response.RecommendedContentApiModel.RelatedContent
import com.free.tvtracker.features.discover.domain.DiscoverShowsService
import com.free.tvtracker.tmdb.TmdbClient
import com.free.tvtracker.tmdb.data.TmdbRecommendedShowsResponse
import com.free.tvtracker.tmdb.data.TmdbShowSmallResponse
import io.mockk.every
import io.mockk.mockk
import org.springframework.http.ResponseEntity
import kotlin.test.Test
import kotlin.test.assertContentEquals

class DiscoverShowsServiceRecommendationTest {
    @Test
    fun `GIVEN 2 shows that share 1 recommended show THEN 1 recommended show returned`() {
        val tmdbClient = mockk<TmdbClient>() {
            every {
                get("/3/tv/1/recommendations", TmdbRecommendedShowsResponse::class.java, any())
            } returns ResponseEntity.ok(
                TmdbRecommendedShowsResponse().apply {
                    results = arrayListOf(
                        TmdbShowSmallResponse(id = 40, name = ""),
                        TmdbShowSmallResponse(id = 101, name = ""),
                    )
                }
            )
            every {
                get("/3/tv/2/recommendations", TmdbRecommendedShowsResponse::class.java, any())
            } returns ResponseEntity.ok(
                TmdbRecommendedShowsResponse().apply {
                    results = arrayListOf(
                        TmdbShowSmallResponse(id = 101, name = ""),
                        TmdbShowSmallResponse(id = 200, name = ""),
                    )
                }
            )
        }
        val sut = DiscoverShowsService(mockk(), tmdbClient)
        val data = RecommendedContentApiRequestBody(listOf(1, 2))
        val res = sut.getRecommended(data)
        assertContentEquals(listOf(RecommendedContentApiModel.Data(101, "")), res?.body?.data?.results)
        assertContentEquals(listOf(RelatedContent(1), RelatedContent(2)), res?.body?.data?.relatedContent)
    }

    @Test
    fun `GIVEN 3 shows that share 1 recommended show THEN 1 recommended show returned`() {
        val tmdbClient = mockk<TmdbClient>() {
            every {
                get("/3/tv/1/recommendations", TmdbRecommendedShowsResponse::class.java, any())
            } returns ResponseEntity.ok(
                TmdbRecommendedShowsResponse().apply {
                    results = arrayListOf(
                        TmdbShowSmallResponse(id = 50, name = ""),
                        TmdbShowSmallResponse(id = 101, name = ""),
                    )
                }
            )
            every {
                get("/3/tv/2/recommendations", TmdbRecommendedShowsResponse::class.java, any())
            } returns ResponseEntity.ok(
                TmdbRecommendedShowsResponse().apply {
                    results = arrayListOf(
                        TmdbShowSmallResponse(id = 101, name = ""),
                        TmdbShowSmallResponse(id = 200, name = ""),
                    )
                }
            )
            every {
                get("/3/tv/3/recommendations", TmdbRecommendedShowsResponse::class.java, any())
            } returns ResponseEntity.ok(
                TmdbRecommendedShowsResponse().apply {
                    results = arrayListOf(
                        TmdbShowSmallResponse(id = 101, name = ""),
                        TmdbShowSmallResponse(id = 500, name = ""),
                    )
                }
            )
        }
        val sut = DiscoverShowsService(mockk(), tmdbClient)
        val data = RecommendedContentApiRequestBody(listOf(1, 2, 3))
        val res = sut.getRecommended(data)
        assertContentEquals(listOf(RecommendedContentApiModel.Data(101, "")), res?.body?.data?.results)
        assertContentEquals(
            listOf(RelatedContent(1), RelatedContent(2), RelatedContent(3)),
            res?.body?.data?.relatedContent
        )
    }

}
