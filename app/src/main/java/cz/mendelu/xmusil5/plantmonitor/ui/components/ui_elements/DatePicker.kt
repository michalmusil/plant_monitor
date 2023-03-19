package cz.mendelu.xmusil5.plantmonitor.ui.components.ui_elements

import android.app.DatePickerDialog
import android.widget.DatePicker
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import cz.mendelu.xmusil5.plantmonitor.R
import cz.mendelu.xmusil5.plantmonitor.ui.theme.shadowColor
import cz.mendelu.xmusil5.plantmonitor.ui.theme.surface
import cz.mendelu.xmusil5.plantmonitor.ui.utils.UiConstants
import cz.mendelu.xmusil5.plantmonitor.utils.DateUtils
import cz.mendelu.xmusil5.plantmonitor.utils.customShadowPercentage
import java.util.Calendar

@Composable
fun DatePicker(
    date: Calendar,
    onDatePicked: (Calendar) -> Unit,
    modifier: Modifier = Modifier
){
    val context = LocalContext.current
    val cornerRadius = UiConstants.RADIUS_CAPSULE_PERCENTAGE

    val year = date.get(Calendar.YEAR)
    val month = date.get(Calendar.MONTH)
    val day = date.get(Calendar.DAY_OF_MONTH)

    val displayString = remember{
        mutableStateOf(DateUtils.getDateString(date))
    }

    val datePickerDialog = DatePickerDialog(
        context,
        { _: DatePicker, mYear: Int, mMonth: Int, mDayOfMonth: Int ->
            val newDate = DateUtils.getCalendarFromDateComponents(
                year = mYear,
                month = mMonth,
                day = mDayOfMonth
            )
            onDatePicked(newDate)
            displayString.value = DateUtils.getDateString(newDate)
        }, year, month, day
    )

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .customShadowPercentage(
                color = shadowColor,
                borderRadiusPercentage = cornerRadius,
                spread = 0.dp,
                blurRadius = 5.dp,
                offsetY = 2.dp
            )
            .clip(RoundedCornerShape(cornerRadius))
            .background(MaterialTheme.colorScheme.primary)
            .clickable {
                datePickerDialog.show()
            }
    ){
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .padding(10.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(30.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.secondary)
            ){
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.ic_calendar),
                    contentDescription = stringResource(id = R.string.icon),
                    tint = MaterialTheme.colorScheme.onSecondary,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(5.dp)
                )
            }
                
            Spacer(modifier = Modifier.width(16.dp))
            
            Text(
                text = displayString.value,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}