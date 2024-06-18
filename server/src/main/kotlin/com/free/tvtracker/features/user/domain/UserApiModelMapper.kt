package com.free.tvtracker.features.user.domain

import com.free.tvtracker.base.Mapper
import com.free.tvtracker.features.user.data.UserEntity
import com.free.tvtracker.user.response.UserApiModel
import org.springframework.stereotype.Component

@Component
class UserApiModelMapper : Mapper<UserEntity, UserApiModel> {
    override fun map(from: UserEntity): UserApiModel {
        return UserApiModel(
            createdAtDatetime = from.createdAtDatetime,
            id = from.id,
            username = from.username,
            email = from.email,
            preferencesPushAllowed = from.preferencesPushAllowed,
            isAnonymous = from.isAnon
        )
    }
}
