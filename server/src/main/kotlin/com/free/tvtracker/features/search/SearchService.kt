package com.free.tvtracker.features.search

import com.free.tvtracker.constants.TmdbContentType
import com.free.tvtracker.details.response.TmdbMovieDetailsApiModel
import com.free.tvtracker.logging.TvtrackerLogger
import com.free.tvtracker.tmdb.TmdbClient
import com.free.tvtracker.tmdb.data.TmdbPersonResponse
import com.free.tvtracker.tmdb.data.TmdbSearchMultiResponse
import com.free.tvtracker.tmdb.data.TmdbShowBigResponse
import com.free.tvtracker.details.response.TmdbPersonDetailsApiModel
import com.free.tvtracker.details.response.TmdbShowDetailsApiModel
import com.free.tvtracker.features.search.mappers.MovieApiModelMapper
import com.free.tvtracker.features.search.mappers.PersonApiModelMapper
import com.free.tvtracker.features.search.mappers.ShowApiModelMapper
import com.free.tvtracker.search.request.MediaType
import com.free.tvtracker.storage.shows.data.StoredEpisodeEntity
import com.free.tvtracker.storage.shows.domain.StoredEpisodesService
import com.free.tvtracker.storage.shows.domain.StoredShowsService
import com.free.tvtracker.tmdb.data.TmdbMovieBigResponse
import com.free.tvtracker.tmdb.data.TmdbMovieCollectionResponse
import org.springframework.stereotype.Service

@Service
class SearchService(
    private val logger: TvtrackerLogger,
    private val tmdbClient: TmdbClient,
    private val storedEpisodesService: StoredEpisodesService,
    private val storedShowsService: StoredShowsService,
    private val showMapper: ShowApiModelMapper,
    private val movieApiModelMapper: MovieApiModelMapper,
    private val personApiModelMapper: PersonApiModelMapper
) {
    fun searchTerm(term: String, mediaType: MediaType): TmdbSearchMultiResponse {
        val media = when (mediaType) {
            MediaType.ALL -> "multi"
            MediaType.TV_SHOWS -> TmdbContentType.SHOW.key
            MediaType.MOVIES -> TmdbContentType.MOVIE.key
            MediaType.PEOPLE -> TmdbContentType.PERSON.key
        }
        val respEntity = tmdbClient.get(
            "/3/search/$media",
            TmdbSearchMultiResponse::class.java,
            mapOf("query" to term)
        )
        return respEntity.body!!
    }

    fun getShow(tmdbShowId: Int): TmdbShowBigResponse {
        val respEntity = tmdbClient.get(
            "/3/tv/$tmdbShowId",
            TmdbShowBigResponse::class.java,
            params = mapOf(
                "append_to_response" to "aggregate_credits,watch/providers,videos,images,external_ids"
            )
        )
        return respEntity.body!!
    }

    fun getMovie(tmdbMovieId: Int): TmdbMovieBigResponse {
        val respEntity = tmdbClient.get(
            "/3/movie/$tmdbMovieId",
            TmdbMovieBigResponse::class.java,
            params = mapOf(
                "append_to_response" to "credits,watch/providers,videos,images"
            )
        )
        return respEntity.body!!
    }

    private fun getMovieCollection(collectionTmdbId: Int): TmdbMovieCollectionResponse {
        val respEntity = tmdbClient.get(
            "/3/collection/$collectionTmdbId",
            TmdbMovieCollectionResponse::class.java,
        )
        return respEntity.body!!
    }

    /**
     * @param alwaysIncludeEpisodes if true then we fetch the episodes from tmdb, if false then we just get them from
     * our db, which may or may not exist depending if someone has tracked this show
     */
    fun getShowApiModel(tmdbShowId: Int, alwaysIncludeEpisodes: Boolean, countryCode: String): TmdbShowDetailsApiModel {
        val showResponse = getShow(tmdbShowId)
        if (alwaysIncludeEpisodes) {
            // this also fetches the episodes and stores them if they aren't already stored
            storedShowsService.createOrUpdateStoredShow(showResponse)
        }
        val episodes = storedEpisodesService.getEpisodes(tmdbShowId = tmdbShowId)
        return showMapper.map(
            showResponse,
            ShowApiModelMapper.ShowApiModelMapperOptions(episodes.map { it.toApiModel() }, countryCode)
        )
    }

    fun getMovieApiModel(tmdbMovieId: Int, countryCode: String): TmdbMovieDetailsApiModel {
        val movie = getMovie(tmdbMovieId)
        val collection = if (movie.belongsToCollection?.id != null) {
            getMovieCollection(movie.belongsToCollection.id)
        } else null
        return movieApiModelMapper.map(
            getMovie(tmdbMovieId),
            MovieApiModelMapper.Options(countryCode, collection)
        )
    }

    fun getPersonApiModel(tmdbPersonId: Int): TmdbPersonDetailsApiModel {
        val res = tmdbClient.get(
            "/3/person/${tmdbPersonId}",
            TmdbPersonResponse::class.java,
            params = mapOf(
                "append_to_response" to "tv_credits,movie_credits,images,external_ids"
            )
        ).body!!
        return personApiModelMapper.map(res)
    }

    fun StoredEpisodeEntity.toApiModel(): TmdbShowDetailsApiModel.Season.Episode {
        return TmdbShowDetailsApiModel.Season.Episode(
            id = this.id,
            number = this.episodeNumber,
            thumbnail = this.thumbnail,
            name = this.episodeName,
            airDate = this.airDate,
            seasonNumber = this.seasonNumber,
        )
    }
}


