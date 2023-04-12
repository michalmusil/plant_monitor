package cz.mendelu.xmusil5.plantmonitor.ui.components.templates

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
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
    onCancelOrDismiss: () -> Unit = {}){

    val cornerRadius = UiConstants.RADIUS_MEDIUM

    val buttonBottomPadding = 4.dp

    val animationDuration = 150

    AnimatedVisibility(
        visible = showDialog.value,
        enter = fadeIn(animationSpec = tween(animationDuration)),
        exit = fadeOut(animationSpec = tween(animationDuration))
    ) {
        AlertDialog(
            onDismissRequest = {
                showDialog.value = false
                onCancelOrDismiss()
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
                    },
                    modifier = Modifier
                        .padding(bottom = buttonBottomPadding)
                )
            },
            dismissButton = {
                CustomButton(
                    text = cancelButtonText,
                    backgroundColor = MaterialTheme.colorScheme.primary,
                    onClick = {
                        showDialog.value = false
                        onCancelOrDismiss()
                    },
                    modifier = Modifier
                        .padding(bottom = buttonBottomPadding)
                )
            },
            shape = RoundedCornerShape(cornerRadius),
            backgroundColor = MaterialTheme.colorScheme.surface,
        )
    }

}