package com.free.tvtracker.features.user.domain

import com.free.tvtracker.base.Mapper
import com.free.tvtracker.user.response.UserApiModel
import org.springframework.stereotype.Component

@Component
class UserApiModelMapper : Mapper<UserService.AuthenticatedUser, UserApiModel> {
    override fun map(from: UserService.AuthenticatedUser): UserApiModel {
        return UserApiModel(
            createdAtDatetime = from.user.createdAtDatetime,
            id = from.user.id,
            username = from.user.username,
            email = from.user.email,
            authToken = from.token
        )
    }
}
