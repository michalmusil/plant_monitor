package cz.mendelu.xmusil5.plantmonitor.ui.components.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import cz.mendelu.xmusil5.plantmonitor.R

@Composable
fun ErrorScreen(
    text: String?,
    imageResourceId: Int = R.drawable.ic_launcher_foreground,
    paintIcon: Boolean = false
){
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 30.dp, vertical = 30.dp)
    ){
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center)
        ) {
            if (paintIcon){
                Icon(
                    imageVector = ImageVector.vectorResource(id = imageResourceId),
                    contentDescription = stringResource(id = R.string.errorIcon),
                    tint = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier
                        .padding(bottom = 20.dp)
                        .width(150.dp)
                        .aspectRatio(1f)
                )
            } else {
                Image(
                    imageVector = ImageVector.vectorResource(id = imageResourceId),
                    contentDescription = stringResource(id = R.string.errorIcon),
                    modifier = Modifier
                        .padding(bottom = 20.dp)
                        .width(150.dp)
                        .aspectRatio(1f)
                )
            }
            Text(
                text = text ?: stringResource(id = R.string.somethingWentWrong),
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center
            )
        }
    }
}