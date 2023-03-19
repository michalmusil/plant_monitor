package cz.mendelu.xmusil5.plantmonitor.ui.screens.plant_detail_screen.contents

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import cz.mendelu.xmusil5.plantmonitor.R
import cz.mendelu.xmusil5.plantmonitor.models.api.measurement.LatestMeasurementValueOfPlant
import cz.mendelu.xmusil5.plantmonitor.models.api.plant.GetPlant
import cz.mendelu.xmusil5.plantmonitor.navigation.INavigationRouter
import cz.mendelu.xmusil5.plantmonitor.ui.components.complex_reusables.DeviceCard
import cz.mendelu.xmusil5.plantmonitor.ui.components.complex_reusables.MostRecentMeasurementValuesCard
import cz.mendelu.xmusil5.plantmonitor.ui.components.ui_elements.DetailCard
import cz.mendelu.xmusil5.plantmonitor.ui.components.ui_elements.ExpandableCard
import cz.mendelu.xmusil5.plantmonitor.ui.screens.plant_detail_screen.PlantDetailViewModel
import cz.mendelu.xmusil5.plantmonitor.ui.theme.shadowColor
import cz.mendelu.xmusil5.plantmonitor.utils.DateUtils
import cz.mendelu.xmusil5.plantmonitor.utils.customShadow
import java.util.*

@Composable
fun PlantDetailBasicInfo(
    plant: GetPlant,
    mostRecentValues: List<LatestMeasurementValueOfPlant>,
    navigation: INavigationRouter,
    viewModel: PlantDetailViewModel
){
    val cornerRadius = 30.dp

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
                contentText = plant.species,
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.weight(0.05f))
            DetailCard(
                titleText = stringResource(id = R.string.created),
                contentText = DateUtils.getLocalizedDateString(plant.created?.calendarInUTC0 ?: Calendar.getInstance()),
                modifier = Modifier.weight(1f)
            )
        }

        if (mostRecentValues.isNotEmpty()){
            MostRecentMeasurementValuesCard(
                plant = plant,
                mostRecentValues = mostRecentValues,
                measurementsValidator = viewModel.measurementsValidator,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 15.dp)
            )
        }

        plant.associatedDevice?.let {
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
        if (plant.description != null && plant.description.length > 0) {
            DetailCard(
                titleText = stringResource(id = R.string.plantDescription),
                contentText = plant.description,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 15.dp)
            )
        }
    }
}