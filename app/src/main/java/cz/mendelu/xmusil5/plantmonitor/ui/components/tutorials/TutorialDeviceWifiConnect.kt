package cz.mendelu.xmusil5.plantmonitor.ui.tutorials

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cz.mendelu.xmusil5.plantmonitor.R
import cz.mendelu.xmusil5.plantmonitor.ui.utils.UiConstants

@Composable
fun TutorialDeviceWifiConnect(
    showTutorial: MutableState<Boolean>
){
    val notePointGap = 14.dp
    val imageCornerRadius = UiConstants.RADIUS_SMALL
    val imageContentScale = ContentScale.Fit

    BaseTutorialWindow(
        showTutorial = showTutorial,
        backgroundColor = MaterialTheme.colorScheme.background,
        title = stringResource(id = R.string.deviceWifiConnectTutorial)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background)
        ) {
            Text(
                text = stringResource(id = R.string.deviceWifiConnect_intro),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(top = notePointGap)
            )
            Text(
                text = stringResource(id = R.string.deviceWifiConnect_timeWarning),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(top = notePointGap)
            )



            Row(
                verticalAlignment = Alignment.Top,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = notePointGap)
            ) {
                Text(
                    text = "1.",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.width(3.dp))
                Text(
                    text = stringResource(id = R.string.deviceWifiConnect_firstStep),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                )
            }
            Image(
                painter = painterResource(id = R.drawable.tut_device_1),
                contentDescription = null,
                contentScale = imageContentScale,
                modifier = Modifier
                    .padding(horizontal = 15.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(imageCornerRadius))
            )


            Row(
                verticalAlignment = Alignment.Top,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = notePointGap)
            ) {
                Text(
                    text = "2.",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.width(3.dp))
                Text(
                    text = stringResource(id = R.string.deviceWifiConnect_secondStep),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                )
            }



            Row(
                verticalAlignment = Alignment.Top,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = notePointGap)
            ) {
                Text(
                    text = "3.",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.width(3.dp))
                Text(
                    text = stringResource(id = R.string.deviceWifiConnect_thirdStep),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                )
            }
            Image(
                painter = painterResource(id = R.drawable.tut_device_3),
                contentDescription = null,
                contentScale = imageContentScale,
                modifier = Modifier
                    .padding(horizontal = 15.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(imageCornerRadius))
            )




            Row(
                verticalAlignment = Alignment.Top,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = notePointGap)
            ) {
                Text(
                    text = "4.",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.width(3.dp))
                Text(
                    text = stringResource(id = R.string.deviceWifiConnect_fourthStep),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                )
            }




            Row(
                verticalAlignment = Alignment.Top,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = notePointGap)
            ) {
                Text(
                    text = "5.",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.width(3.dp))
                Text(
                    text = stringResource(id = R.string.deviceWifiConnect_fifthStep),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                )
            }
            Image(
                painter = painterResource(id = R.drawable.tut_device_4),
                contentDescription = null,
                contentScale = imageContentScale,
                modifier = Modifier
                    .padding(horizontal = 15.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(imageCornerRadius))
            )



            Row(
                verticalAlignment = Alignment.Top,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = notePointGap)
            ) {
                Text(
                    text = "6.",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.width(3.dp))
                Text(
                    text = stringResource(id = R.string.deviceWifiConnect_sixthStep),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                )
            }
        }
    }
}