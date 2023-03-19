package cz.mendelu.xmusil5.plantmonitor.ui.components.ui_elements

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun SmallLoadingIndicator(
    isShown: MutableState<Boolean>,
    size: Dp = 30.dp
){
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .background(Color.Transparent)
            .padding(16.dp)
    ){
        AnimatedVisibility(
            visible = isShown.value,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            CircularProgressIndicator(
                color = MaterialTheme.colorScheme.secondary,
                modifier = androidx.compose.ui.Modifier
                    .size(size)
            )
        }
    }
}