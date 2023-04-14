package cz.mendelu.xmusil5.plantmonitor.ui.components.ui_elements

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import cz.mendelu.xmusil5.plantmonitor.R
import cz.mendelu.xmusil5.plantmonitor.ui.theme.shadowColor
import cz.mendelu.xmusil5.plantmonitor.ui.utils.UiConstants
import cz.mendelu.xmusil5.plantmonitor.utils.customShadowPercentage

@Composable
fun CircularIconButton(
    iconId: Int,
    onClick: () -> Unit,
    contentDescription: String = stringResource(id = R.string.icon),
    backgroundColor: Color = MaterialTheme.colorScheme.primary,
    foregroundColor: Color = MaterialTheme.colorScheme.onPrimary,
    size: Dp = 55.dp,
    modifier: Modifier = Modifier
){
    val iconSize = (size.value * 0.8).dp
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .customShadowPercentage(
                color = shadowColor,
                borderRadiusPercentage = 50,
                spread = 0.dp,
                blurRadius = 5.dp,
                offsetY = 2.dp
            )
            .size(size)
            .clip(CircleShape)
            .background(backgroundColor)
            .clickable {
                onClick()
            }
            .padding(15.dp)
    ){
        Icon(
            imageVector = ImageVector.vectorResource(id = iconId),
            contentDescription = contentDescription,
            tint = foregroundColor,
            modifier = Modifier
                .size(iconSize)
        )
    }
}