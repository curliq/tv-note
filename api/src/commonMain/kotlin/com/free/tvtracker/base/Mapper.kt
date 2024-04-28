package com.free.tvtracker.base

interface Mapper<F, T> {
    fun map(from: F): T
}

interface MapperWithOptions<From, To, Options> {
    fun map(from: From, options: Options): To
}
