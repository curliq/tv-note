package com.free.tvtracker.search

import com.free.tvtracker.core.data.ApiResponse
import com.free.tvtracker.core.logging.TvtrackerLogger
import com.free.tvtracker.core.tmdb.data.enums.TmdbContentType
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(
    "/search",
    produces = ["application/json"]
)
class SearchController(
    val logger: TvtrackerLogger,
    val searchService: SearchService,
) {

    data class SearchBody(val term: String)

    data class SearchResponse(
        val shows: List<SearchShowApiModel>,
        val movies: List<SearchMovieApiModel>,
        val persons: List<SearchPersonApiModel>
    )

    @PostMapping("")
    fun search(@RequestBody body: SearchBody): ApiResponse<SearchResponse> {
        val apiModel = searchService.searchByTerm(body.term).results.map { content ->
            when (TmdbContentType.entries.find { it.field == content.mediaType!! }) {
                TmdbContentType.SHOW -> content.toShowApiModel()
                TmdbContentType.MOVIE -> content.toMovieApiModel()
                TmdbContentType.PERSON -> content.toPersonApiModel()
                null -> null
            }
        }
        val response = SearchResponse(
            shows = apiModel.filterIsInstance<SearchShowApiModel>(),
            movies = apiModel.filterIsInstance<SearchMovieApiModel>(),
            persons = apiModel.filterIsInstance<SearchPersonApiModel>()
        )
        return ApiResponse.ok(response)
    }
}
