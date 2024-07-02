package com.free.tvtracker.features.export

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
    @GetMapping("export-text")
    fun exportCsvText(): ResponseEntity<String> {
        val shows = repo.getShows(sessionRepository.getSessionUserId())
        val writer = StringWriter()
        val csvWriter = CSVWriter(writer)
        val data: List<Array<String>> = toStringArray(shows)
        csvWriter.writeAll(data)
        csvWriter.close()
        return ResponseEntity.ok(writer.toString())
    }

    private fun toStringArray(data: List<Map<String, Any?>>): List<Array<String>> {
        val records: MutableList<Array<String>> = ArrayList()
        val columns = arrayOf(
            "title",
            "tmdb_id",
            "status",
            "watchlisted",
            "created_at_datetime",
            "episodes_watched",
        )
        records.add(columns)
        val it = data.iterator()
        while (it.hasNext()) {
            val row: Map<String, String> = it.next().mapValues { entry ->
                entry.value.toString()
            }
            records.add(
                arrayOf(
                    row.getValue(columns[0]),
                    row.getValue(columns[1]),
                    row.getValue(columns[2]),
                    row.getValue(columns[3]),
                    row.getValue(columns[4]),
                    row.getValue(columns[5]),
                )
            )
        }
        return records
    }
}
