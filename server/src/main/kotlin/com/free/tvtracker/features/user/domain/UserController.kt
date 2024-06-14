package com.free.tvtracker.features.user.domain

import com.free.tvtracker.Endpoints
import com.free.tvtracker.base.ApiError
import com.free.tvtracker.user.response.UserApiResponse
import com.free.tvtracker.logging.TvtrackerLogger
import com.free.tvtracker.user.request.PostFcmTokenRequest
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(produces = ["application/json"])
class UserController(val logger: TvtrackerLogger, val service: UserService) {

    data class SignupRequest(
        val username: String,
        val password: String,
        val email: String? = null
    )

    @Serializable
    data class LoginRequest(
        val username: String,
        val password: String
    )
    data class CreateUserResponse(val accessToken: String, val userId: Int)

    @PostMapping(Endpoints.Path.POST_FCM_TOKEN)
    fun postFcmToken(@RequestBody body: PostFcmTokenRequest): ResponseEntity<Any> {
        service.saveFcmToken(body.fcmToken)
        return ResponseEntity(HttpStatus.OK)
    }

    @GetMapping("")
    fun root(): ResponseEntity<UserApiResponse> {
        val user = service.getAuthenticatedUser()
            ?: return ResponseEntity(UserApiResponse.error(ApiError.Unknown), HttpStatus.BAD_REQUEST)
        return ResponseEntity.ok(UserApiResponse.ok(user.toApiModel()))
    }

    /**
     * This comes from a signup form
     */
    @PostMapping(Endpoints.Path.POST_USER_CREDENTIALS)
    fun setUserCredentials(@RequestBody body: SignupRequest): CreateUserResponse {
        val result = service.setUserCredentials(body)
        return CreateUserResponse("token: ${result?.token}", result?.user?.id ?: -1)
    }

    @PostMapping(Endpoints.Path.CREATE_ANON_USER)
    fun createAnonUser(): CreateUserResponse {
        val result = service.createAnonUser()
        return CreateUserResponse("token: ${result?.token}", result?.user?.id ?: -1)
    }

    @PostMapping(Endpoints.Path.LOGIN)
    fun login(@RequestBody body: LoginRequest): Any {
        val result = service.login(body)
        result?.let {
            return CreateUserResponse("token: ${result.token}", result.user.id)
        } ?: run {
            return "not valid credentials"
        }
    }

    @PostMapping("/refresh-token")
    fun refreshToken(): Any? {
        return service.createBearerToken()
    }
}
