package com.free.tvtracker.base

/**
 * Reduce [From1] and [From2] into [From1]
 */
interface Reducer<From1, From2> {
    fun reduce(from: From1, and: From2): From1
}
