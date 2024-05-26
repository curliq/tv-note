package com.free.tvtracker.storage.shows.data

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Component
import java.sql.PreparedStatement


@Component
class StoredEpisodeJdbcRepository {

    @Autowired
    var jdbcTemplate: JdbcTemplate? = null

    fun saveBatch(episodes: List<StoredEpisodeEntity>) {
        val sql =
            "insert into stored_episodes (id, season_number, episode_number, storedshow_id, air_date, episode_name, thumbnail, updated_at_datetime) " +
                "values(?, ?, ?, ?, ?, ?, ?, NOW()) " +
                "ON CONFLICT(id) " +
                "DO UPDATE SET air_date = excluded.air_date, updated_at_datetime = excluded.updated_at_datetime, episode_name = excluded.episode_name, thumbnail=excluded.thumbnail"

        jdbcTemplate!!.batchUpdate(
            sql,
            episodes,
            episodes.size
        ) { ps: PreparedStatement, episode: StoredEpisodeEntity ->
            ps.setInt(1, episode.id)
            ps.setInt(2, episode.seasonNumber)
            ps.setInt(3, episode.episodeNumber)
            ps.setInt(4, episode.storedShow.id)
            ps.setString(5, episode.airDate)
            ps.setString(6, episode.episodeName)
            ps.setString(7, episode.thumbnail)
        }
    }
}
