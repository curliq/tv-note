package com.free.tvtracker.expect

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope as vms
import kotlinx.coroutines.CoroutineScope

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual abstract class ViewModel actual constructor(): ViewModel() {
    actual val viewModelScope: CoroutineScope
        get() = vms

    override fun onCleared() {
        super.onCleared()
        onClear()
    }

    protected actual open fun onClear() {
        super.onCleared()
    }
}
