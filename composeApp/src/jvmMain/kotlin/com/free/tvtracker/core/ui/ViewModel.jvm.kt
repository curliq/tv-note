package com.free.tvtracker.core.ui

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual abstract class ViewModel actual constructor() {
    actual val viewModelScope: CoroutineScope
        get() = CoroutineScope(Dispatchers.Default)

    protected actual open fun onClear() {
    }
}
