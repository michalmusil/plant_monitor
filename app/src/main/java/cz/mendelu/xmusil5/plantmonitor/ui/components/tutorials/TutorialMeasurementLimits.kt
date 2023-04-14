package cz.mendelu.xmusil5.plantmonitor.ui.components.tutorials

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import cz.mendelu.xmusil5.plantmonitor.R
import cz.mendelu.xmusil5.plantmonitor.ui.tutorials.BaseTutorialWindow
import cz.mendelu.xmusil5.plantmonitor.ui.utils.UiConstants

@Composable
fun TutorialMeasurementLimits(
    showTutorial: MutableState<Boolean>
) {
    val notePointGap = 14.dp
    val imageCornerRadius = UiConstants.RADIUS_SMALL
    val imageContentScale = ContentScale.Fit

    BaseTutorialWindow(
        showTutorial = showTutorial,
        backgroundColor = MaterialTheme.colorScheme.background,
        title = stringResource(id = R.string.measurementLimitsTutorial)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background)
        ) {
            Text(
                text = stringResource(id = R.string.measurementLimitsTutorial_whatAreTheyFor),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(top = notePointGap)
            )
            Image(
                painter = painterResource(id = R.drawable.tut_limits_1),
                contentDescription = null,
                contentScale = imageContentScale,
                modifier = Modifier
                    .padding(horizontal = 15.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(imageCornerRadius))
            )
            Text(
                text = stringResource(id = R.string.measurementLimitsTutorial_whatHappensWhenExceed),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(top = notePointGap)
            )
            Image(
                painter = painterResource(id = R.drawable.tut_limits_2),
                contentDescription = null,
                contentScale = imageContentScale,
                modifier = Modifier
                    .padding(horizontal = 15.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(imageCornerRadius))
            )
        }
    }
}