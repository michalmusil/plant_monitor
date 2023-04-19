package cz.mendelu.xmusil5.plantmonitor.database.type_converters

import android.graphics.Bitmap
import androidx.room.TypeConverter
import cz.mendelu.xmusil5.plantmonitor.utils.DateUtils
import cz.mendelu.xmusil5.plantmonitor.utils.image.ImageUtils
import java.util.*

class BitmapTypeConverter {

    @TypeConverter
    fun saveBitmapToDb(bitmap: Bitmap?): ByteArray? {
        bitmap?.let {
            val byteArray = ImageUtils.fromBitmapToByteArray(it)
            return byteArray
        }
        return null
    }

    @TypeConverter
    fun getBitmapFromDb(bitmapInDb: ByteArray?): Bitmap? {
        bitmapInDb?.let {
            val bitmap = ImageUtils.fromByteArrayToBitmap(bitmapInDb)
            return bitmap
        }
        return null
    }
}