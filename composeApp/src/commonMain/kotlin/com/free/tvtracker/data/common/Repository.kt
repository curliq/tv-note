package com.free.tvtracker.data.common

import com.free.tvtracker.Endpoint
import com.free.tvtracker.EndpointNoBody
import com.free.tvtracker.base.ApiResponse
import com.free.tvtracker.data.session.SessionRepository
import com.free.tvtracker.expect.data.TvHttpClient

open class AuthenticatedRepository(val httpClient: TvHttpClient, val sessionRepository: SessionRepository) {

    @Throws(Throwable::class)
    suspend inline fun <reified ReturnType : ApiResponse<out Any>, reified BodyType : Any> call(
        endpointType: Endpoint<ReturnType, BodyType>,
        body: BodyType
    ): ReturnType {
        return httpClient.call(endpointType, body)
    }

    @Throws(Throwable::class)
    suspend inline fun <reified ReturnType : ApiResponse<out Any>> callNoBody(
        endpointType: EndpointNoBody<ReturnType>,
    ): ReturnType {
        return httpClient.call(endpointType)
    }
}
