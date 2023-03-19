package cz.mendelu.xmusil5.plantmonitor.ui.components.list_items

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import cz.mendelu.xmusil5.plantmonitor.R
import cz.mendelu.xmusil5.plantmonitor.models.api.device.GetDevice
import cz.mendelu.xmusil5.plantmonitor.ui.theme.errorColor
import cz.mendelu.xmusil5.plantmonitor.ui.theme.onlineColor
import cz.mendelu.xmusil5.plantmonitor.ui.theme.shadowColor
import cz.mendelu.xmusil5.plantmonitor.ui.utils.UiConstants
import cz.mendelu.xmusil5.plantmonitor.utils.customShadow

@Composable
fun DeviceListItem(
    device: GetDevice,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val cornerRadius = UiConstants.RADIUS_SMALL

    Box(
        contentAlignment = Alignment.TopEnd,
        modifier = modifier
            .padding(15.dp)
            .width(170.dp)
            .height(200.dp)
            .customShadow(
                color = shadowColor,
                borderRadius = cornerRadius,
                spread = 0.dp,
                blurRadius = 5.dp,
                offsetY = 2.dp
            )
            .clip(RoundedCornerShape(cornerRadius))
            .background(color = MaterialTheme.colorScheme.surface)
            .clickable {
                onClick()
            }
    ) {
        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp)
        ) {

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(70.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary)
            ){
                Image(
                    imageVector = ImageVector.vectorResource(id = R.drawable.ic_measuring_device_filled),
                    contentDescription = stringResource(id = R.string.deviceImage),
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .padding(11.dp)
                        .fillMaxSize()
                )
            }

            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(top = 8.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    device.plant?.let {
                        Image(
                            imageVector = ImageVector.vectorResource(id = R.drawable.ic_house_plant),
                            contentDescription = stringResource(id = R.string.icon),
                            modifier = Modifier
                                .size(22.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(2.dp))

                    Text(
                        text = device.plant?.name ?: stringResource(id = R.string.noPlantAssigned),
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        color = MaterialTheme.colorScheme.onSurface
                    )   
                }
                
                Spacer(modifier = Modifier.height(5.dp))
                
                Text(
                    text = device.communicationId,
                    style = MaterialTheme.typography.labelSmall,
                    textAlign = TextAlign.Center,
                    maxLines = 2,
                    color = MaterialTheme.colorScheme.onSurface,
                )
            }
        }
        Box(
            modifier = Modifier
                .padding(10.dp)
                .size(16.dp)
                .clip(CircleShape)
                .background(
                    if (device.active) onlineColor else errorColor
                )
        )
    }
}