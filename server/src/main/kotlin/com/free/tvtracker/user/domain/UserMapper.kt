package com.free.tvtracker.user.domain

import com.free.tvtracker.user.response.UserApiModel
import com.free.tvtracker.user.data.UserEntity

fun UserEntity.toApiModel(): UserApiModel {
    return UserApiModel(
        createdAtDatetime = this.createdAtDatetime,
        id = this.id,
        email = this.email
    )
}
