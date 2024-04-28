package com.free.tvtracker.base

interface Reducer<From1, From2> {
    fun reduce(from: From1, and: From2): From1
}
