package cz.mendelu.xmusil5.plantmonitor.ui.components.ui_elements

import android.graphics.Bitmap
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import cz.mendelu.xmusil5.plantmonitor.utils.image.ImageQuality
import cz.mendelu.xmusil5.plantmonitor.utils.image.ImageUtils

@Composable
fun GalleryLauncherButton(
    text: String,
    iconId: Int? = null,
    tintIcon: Boolean = true,
    modifier: Modifier = Modifier,
    onImagePicked: (Uri, Bitmap?) -> Unit
){
    val context = LocalContext.current

    val galleryLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()) { imageUri ->
            imageUri?.let {
                val bitmap = ImageUtils.getBitmapFromUri(
                    context = context,
                    uri = it
                )
                onImagePicked(it, bitmap)
            }
        }
    CustomButton(
        text = text,
        iconId = iconId,
        tintIcon = tintIcon,
        modifier = modifier,
        onClick = {
            galleryLauncher.launch("image/*")
        }
    )
}