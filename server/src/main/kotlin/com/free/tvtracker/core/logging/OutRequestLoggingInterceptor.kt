package com.free.tvtracker.core.logging

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpRequest
import org.springframework.http.client.ClientHttpRequestExecution
import org.springframework.http.client.ClientHttpRequestInterceptor
import org.springframework.http.client.ClientHttpResponse
import org.springframework.stereotype.Component
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

@Component
class OutRequestLoggingInterceptor : ClientHttpRequestInterceptor {
    companion object {
        private val logger: Logger = LoggerFactory.getLogger(OutRequestLoggingInterceptor::class.java)
    }

    val get: Logger = logger

    @Throws(IOException::class)
    override fun intercept(
        request: HttpRequest,
        body: ByteArray,
        execution: ClientHttpRequestExecution
    ): ClientHttpResponse {
        traceRequest(request, body)
        val response = execution.execute(request, body)
        traceResponse(response)
        return response
    }

    @Throws(IOException::class)
    private fun traceRequest(request: HttpRequest, body: ByteArray) {
        logger.info("=================request begin============================")
        logger.debug("URI         : {}", request.uri)
        logger.debug("Method      : {}", request.method)
        logger.debug("Request body: {}", String(body, charset("UTF-8")))
        logger.info("================request end============================")
    }

    @Throws(IOException::class)
    private fun traceResponse(response: ClientHttpResponse) {
        val inputStringBuilder = StringBuilder()
        val bufferedReader = BufferedReader(InputStreamReader(response.body, "UTF-8"))
        var line = bufferedReader.readLine()
        while (line != null) {
            inputStringBuilder.append(line)
            inputStringBuilder.append('\n')
            line = bufferedReader.readLine()
        }
        logger.info("============================response begin==========================================")
        logger.debug("Status code  : {}", response.statusCode)
        logger.debug("Status text  : {}", response.statusText)
        logger.debug("Headers      : {}", response.headers)
        logger.debug("Response body: {}", inputStringBuilder.toString())
        logger.info("=======================response end=================================================")
    }
}

