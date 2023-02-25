package cz.mendelu.xmusil5.plantmonitor.ui.components.list_items

import android.graphics.Bitmap
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import cz.mendelu.xmusil5.plantmonitor.R
import cz.mendelu.xmusil5.plantmonitor.models.api.measurement.MeasurementValue
import cz.mendelu.xmusil5.plantmonitor.models.api.plant.GetPlant
import cz.mendelu.xmusil5.plantmonitor.ui.theme.shadowColor
import cz.mendelu.xmusil5.plantmonitor.utils.ImageUtils
import cz.mendelu.xmusil5.plantmonitor.utils.customShadow
import kotlin.math.roundToInt


@Composable
fun PlantListItemExpandable(
    plant: GetPlant,
    plantImage: MutableState<Bitmap?>,
    measurementValues: MutableState<List<MeasurementValue>?>,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    onExpanded: () -> Unit
){
    val animationDuration = 150

    val cornerRadius = 30.dp

    val expanded = remember{
        mutableStateOf(false)
    }

    val rotation by animateFloatAsState(
        targetValue = if (expanded.value) { 180f } else { 0f }
    )

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 7.dp)
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
            .animateContentSize(
                animationSpec = tween(
                    durationMillis = animationDuration
                )
            )
    ){
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp, horizontal = 10.dp)
        ) {

            Image(
                bitmap = plantImage.value?.asImageBitmap()
                    ?: ImageUtils.getBitmapFromVectorDrawable(
                        LocalContext.current, R.drawable.ic_plant_root
                    )!!.asImageBitmap(),
                contentDescription = stringResource(id = R.string.plantImage),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .height(90.dp)
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(cornerRadius))
            )

            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start,
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(start = 16.dp)
                    .weight(1f)
            ) {
                Text(
                    text = plant.name,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = plant.species,
                    style = MaterialTheme.typography.labelMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            Icon(
                imageVector = Icons.Filled.KeyboardArrowDown,
                contentDescription = stringResource(id = R.string.expand),
                tint = shadowColor,
                modifier = Modifier
                    .height(40.dp)
                    .aspectRatio(1f)
                    .clickable {
                        expanded.value = !expanded.value
                        if (expanded.value) {
                            onExpanded()
                        }
                    }
                    .rotate(rotation)
            )

        }

        if (expanded.value){
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(bottom = 16.dp, top = 5.dp)
                    .fillMaxWidth()
            ){
                if (measurementValues.value != null){
                    if (measurementValues.value!!.size > 0) {
                        PlantListItemValues(
                            measurementValues = measurementValues.value!!
                        )
                    } else {
                        Text(
                            text = stringResource(id = R.string.noMeasurementsYet),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
                else{
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .size(30.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun PlantListItemValues(
    measurementValues: List<MeasurementValue>
){
    LazyRow(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ){
        items(measurementValues){ measurementValue ->

            val roundedValue = remember{
                mutableStateOf((measurementValue.value * 10.0).roundToInt() / 10.0)
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(horizontal = 10.dp)
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = measurementValue.measurementType.iconId),
                    contentDescription = stringResource(id = R.string.expand),
                    tint = measurementValue.measurementType.color,
                    modifier = Modifier
                        .height(40.dp)
                        .aspectRatio(1f)
                )

                Spacer(modifier = Modifier.width(10.dp))

                Text(
                    text = roundedValue.value.toString() + " ${measurementValue.measurementType.unit}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}