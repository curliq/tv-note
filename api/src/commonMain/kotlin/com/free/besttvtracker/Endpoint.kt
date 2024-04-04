package com.free.besttvtracker

import kotlin.reflect.KClass

object Endpoints {
    val watching = Endpoint<TrackedShowApiModel>("/", TrackedShowApiModel::class)
}

data class Endpoint<ReturnType : Any>(val path: String, val returnType: KClass<ReturnType>)
