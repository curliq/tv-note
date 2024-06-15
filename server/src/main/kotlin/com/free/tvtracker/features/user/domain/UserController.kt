package com.free.tvtracker.features.user.domain

import com.free.tvtracker.Endpoints
import com.free.tvtracker.base.ApiError
import com.free.tvtracker.logging.TvtrackerLogger
import com.free.tvtracker.user.request.LoginApiRequestBody
import com.free.tvtracker.user.request.PostFcmTokenApiRequestBody
import com.free.tvtracker.user.request.SignupApiRequestBody
import com.free.tvtracker.user.request.UpdatePreferencesApiRequestBody
import com.free.tvtracker.user.response.ErrorInvalidCredentials
import com.free.tvtracker.user.response.SessionApiResponse
import com.free.tvtracker.user.response.UserApiResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(produces = ["application/json"])
class UserController(
    val logger: TvtrackerLogger,
    val userService: UserService,
    val userApiModelMapper: UserApiModelMapper,
    val sessionApiMapper: SessionApiMapper,
) {

    @PostMapping(Endpoints.Path.POST_FCM_TOKEN)
    fun postFcmToken(@RequestBody body: PostFcmTokenApiRequestBody): ResponseEntity<Any> {
        userService.saveFcmToken(body.fcmToken)
        return ResponseEntity(HttpStatus.OK)
    }

    /**
     * This gets called from the signup form
     */
    @PostMapping(Endpoints.Path.POST_USER_CREDENTIALS)
    fun setUserCredentials(@RequestBody body: SignupApiRequestBody): ResponseEntity<SessionApiResponse> {
        val result = userService.setUserCredentials(body)
        return if (result != null) {
            ResponseEntity.ok(SessionApiResponse.ok(sessionApiMapper.map(result)))
        } else {
            ResponseEntity(SessionApiResponse.error(ApiError.Unknown), HttpStatus.BAD_REQUEST)
        }
    }

    @PostMapping(Endpoints.Path.CREATE_ANON_USER)
    fun createAnonUser(): ResponseEntity<SessionApiResponse> {
        val result = userService.createAnonUser()
        return if (result != null) {
            ResponseEntity.ok(SessionApiResponse.ok(sessionApiMapper.map(result)))
        } else {
            ResponseEntity(SessionApiResponse.error(ApiError.Unknown), HttpStatus.BAD_REQUEST)
        }
    }

    @PostMapping(Endpoints.Path.LOGIN)
    fun login(@RequestBody body: LoginApiRequestBody): ResponseEntity<SessionApiResponse> {
        val result = userService.login(body)
        return if (result != null) {
            ResponseEntity.ok(SessionApiResponse.ok(sessionApiMapper.map(result)))
        } else {
            ResponseEntity(SessionApiResponse.error(ErrorInvalidCredentials), HttpStatus.BAD_REQUEST)
        }
    }

    @PostMapping(Endpoints.Path.UPDATE_PREFERENCES)
    fun updatePreferences(@RequestBody body : UpdatePreferencesApiRequestBody): ResponseEntity<UserApiResponse> {
        val result = userService.updatePreferences(body)
        return if (result != null) {
            ResponseEntity.ok(UserApiResponse.ok(userApiModelMapper.map(result)))
        } else {
            ResponseEntity(UserApiResponse.error(ApiError.Unknown), HttpStatus.BAD_REQUEST)
        }
    }
}
