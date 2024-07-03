package com.free.tvtracker.features.export.data

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository

@Repository
class ExportJdbcRepository {
    @Autowired
    var jdbcTemplate: JdbcTemplate? = null

    fun getShows(userId: Int): List<Map<String, Any?>> {
        val sql = """
            select stored_shows.title as show_title,
                   stored_shows.tmdb_id as show_tmdb_id,
                   stored_shows.status as show_status,
                   tracked_shows.watchlisted as show_watchlisted,
                   tracked_shows.created_at_datetime as show_created_at_datetime,
                   CASE
                       WHEN COUNT(stored_episodes.id) > 0 THEN STRING_AGG(
                               CONCAT('s', stored_episodes.season_number, 'e', stored_episodes.episode_number), '; '
                               order by stored_episodes.id)
                       ELSE NULL
                       END AS show_episodes_watched
            from tracked_shows
                     join stored_shows on tracked_shows.storedshow_id = stored_shows.id
                     left join tracked_episodes on tracked_episodes.trackedshow_id = tracked_shows.id
                     left join stored_episodes on stored_episodes.id = tracked_episodes.storedepisode_id
            where tracked_shows.user_id = $userId
            group by tracked_shows.id, stored_shows.id;
        """.trimIndent()
        return jdbcTemplate!!.queryForList(sql)
    }

    fun getMovies(userId: Int): List<Map<String, Any?>> {
        val sql = """
            select stored_movies.title as movie_title,
                   stored_movies.tmdb_id as movie_tmdb_id,
                   stored_movies.release_date as movie_release_date,
                   tracked_movies.created_at_datetime as movie_created_at_datetime,
                   tracked_movies.watchlisted as movie_watchlisted
            from tracked_movies
                     join stored_movies on tracked_movies.storedmovie_id = stored_movies.id
            where tracked_movies.user_id = $userId;
        """.trimIndent()
        return jdbcTemplate!!.queryForList(sql)
    }
}
