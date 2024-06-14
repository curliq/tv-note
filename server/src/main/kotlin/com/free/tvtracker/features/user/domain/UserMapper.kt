package com.free.tvtracker.features.user.domain

import com.free.tvtracker.user.response.UserApiModel
import com.free.tvtracker.features.user.data.UserEntity

fun UserEntity.toApiModel(): UserApiModel {
    return UserApiModel(
        createdAtDatetime = this.createdAtDatetime,
        id = this.id,
        username = this.username,
        email = this.email
    )
}
