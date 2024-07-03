package com.free.tvtracker.features.export

import com.free.tvtracker.Endpoints
import com.free.tvtracker.features.export.data.ExportJdbcRepository
import com.free.tvtracker.security.SessionService
import com.opencsv.CSVWriter
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import java.io.StringWriter


@Controller
@RequestMapping(
    produces = ["application/json"]
)
class ExportController(
    val repo: ExportJdbcRepository,
    val sessionRepository: SessionService
) {

    /**
     * this combines shows and movies into one table
     */
    @GetMapping(Endpoints.Path.GET_EXPORT_SHOWS)
    fun exportCsvText(): ResponseEntity<String> {
        val shows = repo.getShows(sessionRepository.getSessionUserId())
        val movies = repo.getMovies(sessionRepository.getSessionUserId())
        val writer = StringWriter()
        val csvWriter = CSVWriter(writer)
        val data: List<Array<String>> = toStringArray(shows, movies)
        csvWriter.writeAll(data)
        csvWriter.close()
        return ResponseEntity.ok(writer.toString())
    }

    private fun toStringArray(shows: List<Map<String, Any?>>, movies: List<Map<String, Any?>>): List<Array<String>> {
        val records: MutableList<Array<String>> = ArrayList()
        val showsColumns = arrayOf(
            "show_title",
            "show_tmdb_id",
            "show_status",
            "show_watchlisted",
            "show_created_at_datetime",
            "show_episodes_watched",
        )
        val moviesColumns = arrayOf(
            "movie_title",
            "movie_tmdb_id",
            "movie_release_date",
            "movie_created_at_datetime",
            "movie_watchlisted",
        )
        records.add(showsColumns.plus(moviesColumns))
        val showsIt = shows.iterator()
        while (showsIt.hasNext()) {
            val row: Map<String, String> = showsIt.next().mapValues { entry ->
                entry.value.toString()
            }
            records.add(
                showsColumns.map { row.getValue(it) }.toTypedArray().plus(moviesColumns.map { "" })
            )
        }
        val moviesIt = movies.iterator()
        while (moviesIt.hasNext()) {
            val row: Map<String, String> = moviesIt.next().mapValues { entry ->
                entry.value.toString()
            }
            records.add(
                showsColumns.map { "" }.toTypedArray().plus(moviesColumns.map { row.getValue(it) })
            )
        }
        return records
    }
}
