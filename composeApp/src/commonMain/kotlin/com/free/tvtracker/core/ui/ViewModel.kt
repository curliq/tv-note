package com.free.tvtracker.core.ui

import kotlinx.coroutines.CoroutineScope

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
expect abstract class ViewModel() {
    val viewModelScope: CoroutineScope
    protected open fun onClear()
}
