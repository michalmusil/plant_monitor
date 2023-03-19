package cz.mendelu.xmusil5.plantmonitor.ui.components.templates

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import cz.mendelu.xmusil5.plantmonitor.ui.components.ui_elements.CustomButton
import cz.mendelu.xmusil5.plantmonitor.ui.utils.UiConstants

@Composable
fun PopupDialog(
    showDialog: MutableState<Boolean>,
    title: String,
    text: String,
    confirmButtonText: String,
    cancelButtonText: String,
    onConfirm: () -> Unit,
    onCancel: () -> Unit = {}){

    val cornerRadius = UiConstants.RADIUS_MEDIUM

    AlertDialog(
        onDismissRequest = {
            showDialog.value = false
        },
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium
            )
        },
        text = {
            Text(
                text = text,
                style = MaterialTheme.typography.bodyMedium
            )
        },
        confirmButton = {
            CustomButton(
                text = confirmButtonText,
                backgroundColor = MaterialTheme.colorScheme.secondary,
                onClick = {
                    showDialog.value = false
                    onConfirm()
                }
            )
        },
        dismissButton = {
            CustomButton(
                text = cancelButtonText,
                backgroundColor = MaterialTheme.colorScheme.primary,
                onClick = {
                    showDialog.value = false
                    onCancel()
                }
            )
        },
        shape = RoundedCornerShape(cornerRadius),
        backgroundColor = MaterialTheme.colorScheme.surface,
    )

}