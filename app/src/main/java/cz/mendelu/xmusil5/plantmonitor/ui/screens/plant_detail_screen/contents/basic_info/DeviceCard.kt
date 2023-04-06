package cz.mendelu.xmusil5.plantmonitor.ui.components.complex_reusables

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
import androidx.compose.ui.platform.LocalContext
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

@Composable
fun DeviceCard(
    device: GetDevice,
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier
){
    Row(
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
            .clickable{
                onClick()
            }
    ){
        Row(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary)
            ) {
                Image(
                    imageVector = ImageVector.vectorResource(id = R.drawable.ic_measuring_device_filled),
                    contentDescription = stringResource(id = R.string.deviceImage),
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(11.dp)
                )
            }
            Spacer(modifier = Modifier.width(20.dp))
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                
                Text(
                    text = stringResource(id = R.string.currentMeasuringDevice),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(5.dp))

                Text(
                    text = device.getDisplayName(context = LocalContext.current),
                    style = MaterialTheme.typography.bodySmall,
                    textAlign = TextAlign.Center,
                    maxLines = 2,
                    color = MaterialTheme.colorScheme.onSurface,
                )
            }
        }

        Box(
            modifier = Modifier
                .padding(16.dp)
                .size(16.dp)
                .clip(CircleShape)
                .background(
                    if (device.active) onlineColor else errorColor
                )
        )
    }
}