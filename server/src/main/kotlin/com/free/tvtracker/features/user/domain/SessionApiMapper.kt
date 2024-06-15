package com.free.tvtracker.features.user.domain

import com.free.tvtracker.base.Mapper
import com.free.tvtracker.user.response.SessionApiModel
import org.springframework.stereotype.Component

@Component
class SessionApiMapper(private val userApiModelMapper: UserApiModelMapper) :
    Mapper<UserService.AuthenticatedUser, SessionApiModel> {
    override fun map(from: UserService.AuthenticatedUser): SessionApiModel {
        return SessionApiModel(
            user = userApiModelMapper.map(from.user),
            authToken = from.token
        )
    }
}
