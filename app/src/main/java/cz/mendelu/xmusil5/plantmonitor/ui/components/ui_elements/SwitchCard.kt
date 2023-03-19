package cz.mendelu.xmusil5.plantmonitor.ui.components.complex_reusables

import androidx.compose.foundation.layout.*
import androidx.compose.material.Switch
import androidx.compose.material.SwitchDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import cz.mendelu.xmusil5.plantmonitor.R
import cz.mendelu.xmusil5.plantmonitor.ui.theme.disabledColor
import cz.mendelu.xmusil5.plantmonitor.ui.theme.shadowColor

@Composable
fun SwitchCard(
    checked: MutableState<Boolean>,
    mainText: String,
    secondaryText: String? = null,
    additionalContent: @Composable () -> Unit = {},
    iconId: Int? = null,
    iconEnabledTint: Color = MaterialTheme.colorScheme.primary,
    iconDisabledTint: Color = disabledColor,
    onValueChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
){
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ){
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp, horizontal = 16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .weight(3f)
            ) {

                Spacer(modifier = Modifier.width(5.dp))

                iconId?.let {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = it),
                        contentDescription = stringResource(id = R.string.icon),
                        tint = if (checked.value) iconEnabledTint else iconDisabledTint,
                        modifier = Modifier
                            .height(50.dp)
                            .aspectRatio(1f)
                    )
                    Spacer(modifier = Modifier.width(15.dp))
                }

                Column(
                    horizontalAlignment = Alignment.Start
                ) {

                    Text(
                        text = mainText,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    secondaryText?.let {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = secondaryText,
                            style = MaterialTheme.typography.labelSmall,
                            maxLines = 3,
                            color = MaterialTheme.colorScheme.onSurface,
                        )
                    }

                    Box(contentAlignment = Alignment.Center){
                        additionalContent()
                    }
                }
            }

            Box(
                contentAlignment = Alignment.CenterEnd,
                modifier = Modifier
                    .weight(1f)
            ){
                Switch(
                    checked = checked.value,
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = MaterialTheme.colorScheme.secondary,
                        uncheckedThumbColor = disabledColor,
                        checkedTrackColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.6f),
                        uncheckedTrackColor = disabledColor.copy(0.8f)
                    ),
                    onCheckedChange = {
                        checked.value = it
                        onValueChange(it)
                    }
                )
            }
        }
    }
}