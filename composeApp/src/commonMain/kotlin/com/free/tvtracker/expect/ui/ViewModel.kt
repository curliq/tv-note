package com.free.tvtracker.expect.ui

import kotlinx.coroutines.CoroutineScope

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
expect abstract class ViewModel() {
    val viewModelScope: CoroutineScope
    protected open fun onClear()
}
