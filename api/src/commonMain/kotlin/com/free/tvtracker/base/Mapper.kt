package com.free.tvtracker.base

interface Mapper<F, T> {
    fun map(from: F): T
    fun map(): (F) -> T {
        return { map(it) }
    }
}

interface MapperWithOptions<From, To, Options> {
    fun map(from: From, options: Options): To
}
