package cz.mendelu.xmusil5.plantmonitor.models.support

import android.graphics.Bitmap
import android.net.Uri

data class BitmapWithUri(
    val uri: Uri?,
    val bitmap: Bitmap
)
