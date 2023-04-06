package cz.mendelu.xmusil5.plantmonitor.ui.screens.plant_detail_screen.contents.basic_info

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import cz.mendelu.xmusil5.plantmonitor.R
import cz.mendelu.xmusil5.plantmonitor.models.api.measurement.MeasurementValueLimit
import cz.mendelu.xmusil5.plantmonitor.ui.components.list_items.MeasurementAcceptableRangeListItem
import cz.mendelu.xmusil5.plantmonitor.ui.utils.UiConstants

@Composable
fun PlantMeasurementLimits(
    limits: List<MeasurementValueLimit>,
    modifier: Modifier = Modifier
){
  Column(
      horizontalAlignment = Alignment.CenterHorizontally,
      modifier = modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(UiConstants.EDGE_FADE_MEDIUM))
          .background(MaterialTheme.colorScheme.surface)
          .padding()
  ) {
      Spacer(modifier = Modifier.height(10.dp))

      Text(
          text = stringResource(id = R.string.measurementValueLimits),
          style = MaterialTheme.typography.titleMedium,
          fontWeight = FontWeight.Bold,
          maxLines = 1,
          overflow = TextOverflow.Ellipsis
      )

      Spacer(modifier = Modifier.height(10.dp))

      limits.forEach {
          MeasurementAcceptableRangeListItem(
              measurementLimit = it
          )
      }

      Spacer(modifier = Modifier.height(10.dp))
  }
}