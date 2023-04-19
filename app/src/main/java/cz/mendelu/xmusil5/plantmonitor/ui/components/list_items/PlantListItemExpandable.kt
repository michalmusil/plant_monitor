package cz.mendelu.xmusil5.plantmonitor.ui.components.list_items

import android.graphics.Bitmap
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import cz.mendelu.xmusil5.plantmonitor.R
import cz.mendelu.xmusil5.plantmonitor.models.api.measurement.LatestMeasurementValueOfPlant
import cz.mendelu.xmusil5.plantmonitor.models.enums.MeasurementLimitValidation
import cz.mendelu.xmusil5.plantmonitor.models.enums.MeasurementType
import cz.mendelu.xmusil5.plantmonitor.models.api.measurement.MeasurementValue
import cz.mendelu.xmusil5.plantmonitor.models.api.plant.Plant
import cz.mendelu.xmusil5.plantmonitor.ui.theme.shadowColor
import cz.mendelu.xmusil5.plantmonitor.ui.utils.Edges
import cz.mendelu.xmusil5.plantmonitor.ui.utils.UiConstants
import cz.mendelu.xmusil5.plantmonitor.utils.image.ImageUtils
import cz.mendelu.xmusil5.plantmonitor.utils.customShadow
import cz.mendelu.xmusil5.plantmonitor.utils.fadeEdges
import cz.mendelu.xmusil5.plantmonitor.utils.validation.measurements.IMeasurementsValidator


@Composable
fun PlantListItemExpandable(
    plant: Plant,
    plantImage: MutableState<Bitmap?>,
    latestValues: MutableState<List<LatestMeasurementValueOfPlant>?>,
    expanded: MutableState<Boolean>,
    measurementValidator: IMeasurementsValidator? = null,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
){
    val expandAnimationDuration = 150
    val stripColorAnimationDuration = 300
    val imageSize = 90.dp

    val cornerRadius = UiConstants.RADIUS_LARGE

    val overallValidation = remember{
        mutableStateOf<MeasurementLimitValidation?>(null)
    }

    val stripColor by animateColorAsState(
        targetValue = if (overallValidation.value != null) overallValidation.value!!.color else Color.Transparent,
        animationSpec = tween(stripColorAnimationDuration)
    )

    val rotation by animateFloatAsState(
        targetValue = if (expanded.value) { 180f } else { 0f }
    )

    // Validates the measurement values and determines the color of the plant strip
    LaunchedEffect(latestValues.value){
        if (latestValues.value != null && measurementValidator != null) {
            var worstValidation: MeasurementLimitValidation? = null
            for (value in latestValues.value!!) {
                val validation = measurementValidator.validateMeasurementValue(
                    value = value.value,
                    type = value.measurementType,
                    plant = plant
                )
                when(validation){
                    MeasurementLimitValidation.VALID -> {
                        if(worstValidation == null){
                            worstValidation = validation
                        }
                    }
                    MeasurementLimitValidation.MINOR_INVALID -> {
                        if (worstValidation == null || worstValidation == MeasurementLimitValidation.VALID){
                            worstValidation = validation
                        }
                    }
                    MeasurementLimitValidation.INVALID -> {
                        worstValidation = validation
                        break
                    }
                }
            }
            overallValidation.value = worstValidation
        }
    }

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
                    durationMillis = expandAnimationDuration
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
                    .height(imageSize)
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(cornerRadius))
            )

            Box(
                modifier = Modifier
                    .padding(start = 8.dp)
                    .clip(CircleShape)
                    .width(8.dp)
                    .height(imageSize)
                    .background(stripColor)
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
                if (latestValues.value != null){
                    if (latestValues.value!!.size > 0) {
                        ExpandableLastPlantValues(
                            plant = plant,
                            measurementValues = latestValues.value!!,
                            measurementValidator = measurementValidator
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
fun ExpandableLastPlantValues(
    plant: Plant,
    measurementValues: List<LatestMeasurementValueOfPlant>,
    measurementValidator: IMeasurementsValidator? = null,
){
    val validatedTypeLimits = remember{
        mutableStateListOf<Pair<MeasurementType, MeasurementLimitValidation>>()
    }

    LaunchedEffect(measurementValidator){
        measurementValidator?.let { validator ->
            validatedTypeLimits.clear()
            measurementValues.forEach {
                val validation = validator.validateMeasurementValue(
                    value = it.value,
                    type = it.measurementType,
                    plant = plant
                )
                val validatedType = Pair(it.measurementType, validation)
                validatedTypeLimits.add(validatedType)
            }
        }
    }

    LazyRow(
        verticalAlignment = Alignment.CenterVertically,
        contentPadding = PaddingValues(horizontal = 12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .fadeEdges(
                edges = Edges.HORIZONTAL,
                backgroundColor = MaterialTheme.colorScheme.secondary,
                fadeWidth = UiConstants.EDGE_FADE_SMALL
            )
    ){
        items(
            count = measurementValues.count(),
            key = {
                measurementValues[it].measurementType
            },
            itemContent = { index ->
                val latestPlantValue = measurementValues.getOrNull(index)
                latestPlantValue?.let {
                    MeasurementValueListItem(
                        measurementValue = MeasurementValue(
                            it.measurementType,
                            value = it.value
                        ),
                        validation = validatedTypeLimits.firstOrNull {
                            it.first == latestPlantValue.measurementType
                        }?.second
                    )
                }
            }
        )
    }
}
