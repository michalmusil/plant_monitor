package com.icontio.senscare_peresonal_mobile.ui.components.templates

import androidx.compose.animation.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import kotlinx.coroutines.delay

@Composable
fun DelayedAnimatedAppear(
    appearDelayMillis: Long = 50L,
    enterTransition: EnterTransition = slideInHorizontally() + fadeIn(),
    content: @Composable() AnimatedVisibilityScope.() -> Unit
){
    val visible = remember {
        mutableStateOf(false)
    }
    LaunchedEffect(true){
        delay(appearDelayMillis)
        visible.value = true
    }

    AnimatedVisibility(
        visible = visible.value,
        enter = enterTransition,
    ) {
        content()
    }
}