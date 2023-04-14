package cz.mendelu.xmusil5.plantmonitor.ui.tutorials

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import cz.mendelu.xmusil5.plantmonitor.ui.utils.UiConstants

@Composable
fun BaseTutorialWindow(
    showTutorial: MutableState<Boolean>,
    title: String,
    backgroundColor: Color = MaterialTheme.colorScheme.background,
    content: @Composable () -> Unit
){
    val cornerRadius = UiConstants.RADIUS_MEDIUM
    val animationDuration = 150
    val height = 700.dp

    AnimatedVisibility(
        visible = showTutorial.value,
        enter = fadeIn(animationSpec = tween(animationDuration)),
        exit = fadeOut(animationSpec = tween(animationDuration))
    ) {

        Dialog(
            onDismissRequest = {
                showTutorial.value = false
            }
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(height)
                    .clip(RoundedCornerShape(cornerRadius))
                    .verticalScroll(rememberScrollState())
                    .background(backgroundColor)
                    .padding(16.dp)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(10.dp))

                content()
            }
        }
    }
}