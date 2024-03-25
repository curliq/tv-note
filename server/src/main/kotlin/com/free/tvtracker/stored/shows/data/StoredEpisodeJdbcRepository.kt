package com.free.tvtracker.stored.shows.data

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Component
import java.sql.PreparedStatement


@Component
class StoredEpisodeJdbcRepository {

    @Autowired
    var jdbcTemplate: JdbcTemplate? = null

    fun saveBatch(episodes: List<StoredEpisodeEntity>) {
        val sql = "insert into stored_episodes (id, season_number, episode_number, storedshow_id) values(uuid_generate_v4(), ?,?,?)"

        jdbcTemplate!!.batchUpdate(
            sql,
            episodes,
            episodes.size
        ) { ps: PreparedStatement, episode: StoredEpisodeEntity ->
            ps.setInt(1, episode.seasonNumber)
            ps.setInt(2, episode.episodeNumber)
            ps.setInt(3, episode.storedShow.id)
        }
    }
}
