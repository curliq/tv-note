package com.free.tvtracker.features.user.domain

import com.free.tvtracker.base.ApiError
import com.free.tvtracker.user.response.UserApiResponse
import com.free.tvtracker.logging.TvtrackerLogger
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(
    "/user",
    produces = ["application/json"]
)
class UserController(val logger: TvtrackerLogger, val service: UserService) {

    data class CreateUserRequest(val email: String, val password: String)
    data class LoginRequest(val email: String, val password: String)
    data class CreateUserResponse(val accessToken: String, val userId: Int)

    @GetMapping("")
    fun root(): ResponseEntity<UserApiResponse> {
        val user = service.getAuthenticatedUser()
            ?: return ResponseEntity(UserApiResponse.error(ApiError.Unknown), HttpStatus.BAD_REQUEST)
        return ResponseEntity.ok(UserApiResponse.ok(user.toApiModel()))
    }

    @PostMapping("/create")
    fun signup(@RequestBody body: CreateUserRequest): CreateUserResponse {
        val result = service.createUser(body.email, body.password)
        return CreateUserResponse("token: ${result?.token}", result?.user?.id ?: -1)
    }

    @PostMapping("/login")
    fun login(@RequestBody body: LoginRequest): Any {
        val result = service.login(body.email, body.password)
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
