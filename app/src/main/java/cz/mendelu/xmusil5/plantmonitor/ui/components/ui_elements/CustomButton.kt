package cz.mendelu.xmusil5.plantmonitor.ui.components.ui_elements

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cz.mendelu.xmusil5.plantmonitor.R
import cz.mendelu.xmusil5.plantmonitor.ui.theme.disabledColor

@Composable
fun CustomButton(
    text: String,
    iconId: Int? = null,
    tintIcon: Boolean = true,
    backgroundColor: Color = MaterialTheme.colorScheme.primary,
    textColor: Color = MaterialTheme.colorScheme.onPrimary,
    textSize: TextUnit = 17.sp,
    onClick: () -> Unit,
    enabled: Boolean = true,
    modifier: Modifier = Modifier
){
    Button(
        enabled = enabled,
        shape = RoundedCornerShape(50),
        colors = ButtonDefaults.buttonColors(
            containerColor = backgroundColor,
            contentColor = textColor,
            disabledContainerColor = disabledColor
        ),
        onClick = {
            if (enabled) {
                onClick()
            }
        },
        modifier = modifier
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier
                .padding(horizontal = 8.dp, vertical = 5.dp)
        ) {
            iconId?.let {
                if (tintIcon) {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = it),
                        contentDescription = stringResource(id = R.string.icon),
                        tint = textColor,
                        modifier = Modifier
                            .size((textSize.value * 1.5).dp)
                            .padding(end = 5.dp)
                    )
                }
                else {
                    Image(
                        imageVector = ImageVector.vectorResource(id = it),
                        contentDescription = stringResource(id = R.string.icon),
                        modifier = Modifier
                            .size((textSize.value * 1.5).dp)
                            .padding(end = 5.dp)
                    )
                }
            }
            Text(
                text = text,
                color = textColor,
                fontWeight = FontWeight.Bold,
                maxLines = 2,
                lineHeight = 12.sp,
                overflow = TextOverflow.Ellipsis,
                fontSize = textSize,
            )
        }
    }
}