package cz.mendelu.xmusil5.plantmonitor.ui.components.ui_elements

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import cz.mendelu.xmusil5.plantmonitor.R
import cz.mendelu.xmusil5.plantmonitor.ui.theme.shadowColor

@Composable
fun ExpandableCard(
    headlineContent: @Composable () -> Unit,
    expandedContent: @Composable () -> Unit,
    animationDuration: Int = 200,
    modifier: Modifier = Modifier
){
    val expanded = remember{
        mutableStateOf(false)
    }

    val rotation by animateFloatAsState(
        targetValue = if (expanded.value) { 180.0f } else { 0.0f },
        animationSpec = tween(
            durationMillis = animationDuration
        )
    )

    Column(
        modifier = modifier
            .animateContentSize(
                animationSpec = tween(
                    durationMillis = animationDuration
                )
            )
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    expanded.value = !expanded.value
                }
        ) {
            Spacer(modifier = Modifier.height(10.dp))

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                headlineContent()
                Icon(
                    imageVector = Icons.Filled.KeyboardArrowDown,
                    contentDescription = stringResource(id = R.string.expand),
                    tint = shadowColor,
                    modifier = Modifier
                        .height(30.dp)
                        .aspectRatio(1f)
                        .rotate(rotation)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))
        }

        if (expanded.value) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ){
                expandedContent()
            }

            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}