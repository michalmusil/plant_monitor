package cz.mendelu.xmusil5.plantmonitor.ui.components.ui_elements

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp

@Composable
fun CustomTextField(
    labelTitle: String,
    value: MutableState<String>,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    isError: Boolean = false,
    errorMessage: String = "",
    onTextChanged: (String) -> Unit = {},
    enabled: Boolean = true,
    singleLine: Boolean = true,
    maxChars: Int = 10000,
    modifierTextField: Modifier = Modifier
){
    val localFocusManager = LocalFocusManager.current
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            OutlinedTextField(
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
                    focusedBorderColor = MaterialTheme.colorScheme.secondary,
                    focusedLabelColor = MaterialTheme.colorScheme.secondary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedLabelColor = MaterialTheme.colorScheme.primary,
                    disabledBorderColor = MaterialTheme.colorScheme.primary,
                    disabledLabelColor = MaterialTheme.colorScheme.primary,
                    disabledTextColor = MaterialTheme.colorScheme.primary,
                    cursorColor = MaterialTheme.colorScheme.secondary,
                    errorBorderColor = MaterialTheme.colorScheme.error,
                    errorLabelColor = MaterialTheme.colorScheme.error,
                    textColor = MaterialTheme.colorScheme.onBackground
                ),
                visualTransformation = visualTransformation,
                keyboardActions = KeyboardActions(
                    onDone = {
                        localFocusManager.clearFocus()
                    },
                ),
                shape = RoundedCornerShape(12.dp),
                isError = isError,
                enabled = enabled,
                singleLine = singleLine,
                trailingIcon = {
                    if (isError){
                        Icon(Icons.Filled.Clear,"error", tint = MaterialTheme.colorScheme.error)
                    }
                },
                modifier = modifierTextField
                    .weight(1f)
            )
        }
        if (isError){
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier.padding(start = 3.dp)
            )
        }
    }
}