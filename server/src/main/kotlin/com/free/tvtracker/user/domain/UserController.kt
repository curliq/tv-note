package com.free.tvtracker.user.domain

import com.free.tvtracker.core.logging.TvtrackerLogger
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
    fun root(): Any {
        return service.getAuthenticatedUser() ?: "user not found"
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
