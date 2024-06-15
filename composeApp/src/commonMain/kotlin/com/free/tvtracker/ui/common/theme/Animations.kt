package com.free.tvtracker.ui.common.theme

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith

fun <S> ScreenContentAnimation(): AnimatedContentTransitionScope<S>.() -> ContentTransform {
    return {
        fadeIn(animationSpec = tween(220)) togetherWith fadeOut(
            animationSpec = tween(
                220
            )
        )
    }
}
