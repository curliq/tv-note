package com.free.tvtracker.data.session

import com.free.tvtracker.data.common.sql.LocalSqlDataProvider
import com.free.tvtracker.expect.data.TvHttpClientEndpoints

class SessionRepository(
    private val httpClient: TvHttpClientEndpoints,
    private val localDataSource: LocalSqlDataProvider,
    private val sessionStore: SessionStore
) {

    /**
     * @return success
     *
     * Used from the welcome screen
     */
    suspend fun createAnonSession(): Boolean {
        val session = try {
            httpClient.createAnonUser()
        } catch (e: Throwable) {
            e.printStackTrace()
            null
        }
        if (session?.isSuccess() == true) {
            val res = session.data!!
            try {
                localDataSource.saveSession(
                    SessionClientEntity(
                        token = res.authToken,
                        createdAtDatetime = res.user.createdAtDatetime,
                        id = res.user.id.toLong(),
                        username = res.user.username,
                        email = res.user.email,
                    )
                )
            } catch (e: Exception) {
                return false
            }
            sessionStore.token = session.data!!.authToken
            return true
        } else {
            return false
        }
    }

    /**
     * @return success
     *
     * Used from the splash screen
     */
    fun loadSession(): Boolean {
        val storedSession = localDataSource.getSession()
        if (storedSession != null) {
            sessionStore.token = storedSession.token
            return true
        } else {
            return false
        }
    }
}
