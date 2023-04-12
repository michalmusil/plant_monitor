package cz.mendelu.xmusil5.plantmonitor.ui.screens.plant_detail_screen.contents

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import cz.mendelu.xmusil5.plantmonitor.R
import cz.mendelu.xmusil5.plantmonitor.navigation.INavigationRouter
import cz.mendelu.xmusil5.plantmonitor.ui.components.complex_reusables.DeviceCard
import cz.mendelu.xmusil5.plantmonitor.ui.components.complex_reusables.MostRecentMeasurementValuesCard
import cz.mendelu.xmusil5.plantmonitor.ui.components.ui_elements.DetailCard
import cz.mendelu.xmusil5.plantmonitor.ui.screens.plant_detail_screen.PlantDetailViewModel
import cz.mendelu.xmusil5.plantmonitor.ui.screens.plant_detail_screen.contents.basic_info.PlantMeasurementLimits
import cz.mendelu.xmusil5.plantmonitor.ui.theme.shadowColor
import cz.mendelu.xmusil5.plantmonitor.ui.utils.UiConstants
import cz.mendelu.xmusil5.plantmonitor.utils.DateUtils
import cz.mendelu.xmusil5.plantmonitor.utils.customShadow
import java.util.*

@Composable
fun PlantDetailBasicInfo(
    navigation: INavigationRouter,
    viewModel: PlantDetailViewModel
){
    val cornerRadius = UiConstants.RADIUS_LARGE

    val plant = viewModel.plant.collectAsState()
    val mostRecentValues = viewModel.mostRecentMeasurementValues.collectAsState()

    plant.value?.let {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {

            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 15.dp)
            ) {
                DetailCard(
                    titleText = stringResource(id = R.string.plantSpecies),
                    contentText = it.species,
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.weight(0.05f))
                DetailCard(
                    titleText = stringResource(id = R.string.created),
                    contentText = DateUtils.getLocalizedDateString(it.created?.calendarInUTC0 ?: Calendar.getInstance()),
                    modifier = Modifier.weight(1f)
                )
            }

            if (it.description != null && it.description.length > 0) {
                DetailCard(
                    titleText = stringResource(id = R.string.plantDescription),
                    contentText = it.description,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 15.dp)
                )
            }

            mostRecentValues.value?.let { measurementValues ->
                if (measurementValues.isNotEmpty()) {
                    MostRecentMeasurementValuesCard(
                        plant = it,
                        mostRecentValues = measurementValues,
                        measurementsValidator = viewModel.measurementsValidator,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 15.dp)
                    )
                }
            }

            plant.value?.valueLimits?.let {
                if (it.isNotEmpty()) {
                    PlantMeasurementLimits(
                        limits = it,
                        modifier = Modifier
                            .padding(top = 15.dp)
                    )
                }
            }

            it.associatedDevice?.let {
                DeviceCard(
                    device = it,
                    onClick = {
                        navigation.toDeviceDetailAndControl(it.id)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 15.dp)
                        .padding(horizontal = 5.dp)
                        .customShadow(
                            color = shadowColor,
                            borderRadius = cornerRadius,
                            spread = 0.dp,
                            blurRadius = 5.dp,
                            offsetY = 2.dp
                        )
                        .clip(RoundedCornerShape(cornerRadius))
                        .background(MaterialTheme.colorScheme.surface)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}