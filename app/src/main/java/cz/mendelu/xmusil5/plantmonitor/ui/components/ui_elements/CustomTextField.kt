package cz.mendelu.xmusil5.plantmonitor.ui.components.ui_elements

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import cz.mendelu.xmusil5.plantmonitor.R
import cz.mendelu.xmusil5.plantmonitor.ui.theme.shadowColor
import cz.mendelu.xmusil5.plantmonitor.utils.customShadow

@Composable
fun CustomTextField(
    labelTitle: String,
    value: MutableState<String>,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    isError: Boolean = false,
    errorMessage: String = "",
    onTextChanged: (String) -> Unit = {},
    singleLine: Boolean = true,
    maxChars: Int = 10000,
    modifierTextField: Modifier = Modifier
){
    val localFocusManager = LocalFocusManager.current
    val cornerRadius = 30.dp

    Column(
        modifier = Modifier
            .padding(vertical = 10.dp)
            .fillMaxWidth()
            .customShadow(
                color = shadowColor,
                borderRadius = cornerRadius,
                spread = 0.dp,
                blurRadius = 5.dp,
                offsetY = 2.dp
            )
            .clip(RoundedCornerShape(cornerRadius))
            .background(MaterialTheme.colorScheme.surface)
    ) {
        Row {
            TextField(
                value = value.value,
                onValueChange = {
                    if (it.length <= maxChars) {
                        value.value = it
                        onTextChanged(it)
                    }
                },
                label = {
                    Text(text = labelTitle)
                },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Color.Transparent,
                    focusedLabelColor = MaterialTheme.colorScheme.secondary,
                    unfocusedBorderColor = Color.Transparent,
                    unfocusedLabelColor = MaterialTheme.colorScheme.primary,
                    disabledBorderColor = Color.Transparent,
                    disabledLabelColor = MaterialTheme.colorScheme.primary,
                    disabledTextColor = MaterialTheme.colorScheme.primary,
                    cursorColor = MaterialTheme.colorScheme.secondary,
                    errorBorderColor = Color.Transparent,
                    errorLabelColor = MaterialTheme.colorScheme.error,
                    textColor = MaterialTheme.colorScheme.onSurface
                ),
                visualTransformation = visualTransformation,
                keyboardActions = KeyboardActions(
                    onDone = {
                        localFocusManager.clearFocus()
                    },
                    onNext = {
                        localFocusManager.moveFocus(FocusDirection.Down)
                    }
                ),
                isError = isError,
                singleLine = singleLine,
                trailingIcon = {
                    if (isError) {
                        Icon(
                            imageVector = Icons.Filled.Clear,
                            contentDescription = stringResource(id = R.string.errorIcon),
                            tint = MaterialTheme.colorScheme.error,
                        )
                    }
                },
                modifier = modifierTextField
                    .weight(1f)
                    .padding(horizontal = 16.dp)
            )
        }
        if (isError){
            Box(
                contentAlignment = Alignment.CenterEnd,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 30.dp)
                    .padding(bottom = 5.dp)
            ){
                Text(
                    text = errorMessage,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.labelSmall,
                )
            }
        }
    }
}