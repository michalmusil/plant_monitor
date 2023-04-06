package cz.mendelu.xmusil5.plantmonitor.utils.image

import android.content.Context
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.provider.OpenableColumns
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.net.toUri
import java.io.BufferedInputStream
import java.io.ByteArrayOutputStream
import java.io.InputStream


object ImageUtils {
    fun fromBitmapToByteArray(bitmap: Bitmap?): ByteArray?{
        if(bitmap != null) {
            val outputStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 25, outputStream)
            return outputStream.toByteArray()
        } else{
            return null
        }
    }

    fun fromByteArrayToBitmap(byteArray: ByteArray?, compressionQuality: Int? = null): Bitmap?{
        if(byteArray != null) {
            val bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
            compressionQuality?.let { quality ->
                val outputStream = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream)
                val array = outputStream.toByteArray()
                return BitmapFactory.decodeByteArray(array, 0, array.size)
            }
            return bitmap
        } else{
            return null
        }
    }

    fun getBitmapFromVectorDrawable(context: Context, drawableId: Int): Bitmap? {
        var drawable = ContextCompat.getDrawable(context, drawableId)
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            drawable = DrawableCompat.wrap(drawable!!).mutate()
        }
        val bitmap = Bitmap.createBitmap(
            drawable!!.intrinsicWidth,
            drawable.intrinsicHeight, Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight())
        drawable.draw(canvas)
        return bitmap
    }

    fun getBitmapFromUri(
        context: Context,
        uriString: String,
        quality: ImageQuality = ImageQuality.SMALL
    ): Bitmap?{
        val inputStream = context.contentResolver.openInputStream(uriString.toUri())
        inputStream?.let {
            val bitmap = getBitmapFromInputStream(
                inputStream = inputStream,
                quality = quality
            )
            return bitmap
        }
        return null
        /*
        try {
            val uri = Uri.parse(uriString)
            val bitmap = MediaStore.Images.Media.getBitmap(context.contentResolver, uri)

            if (uri.path != null) {
                var bitmapRotated: Bitmap? = null
                // Rotating the image to be upright
                // based on how it's stored in file system - orientation stored in exif tag
                val exif = ExifInterface(uri.path.toString())
                val photoOrientation: Int = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1)
                val rotationDegrees: Float = when(photoOrientation){
                    3 -> 180.0f
                    4 -> 180.0f
                    5 -> 90.0f
                    6 -> 90.0f
                    7 -> 270.0f
                    8 -> 270.0f
                    else -> 0.0f
                }
                val matrix = Matrix().apply {
                    postRotate(rotationDegrees)
                }
                bitmapRotated = Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)

                return bitmapRotated
            }
            return bitmap
        } catch (ex: java.lang.Exception){
            return null
        }
         */
    }

    fun getBitmapFromInputStream(
        inputStream: InputStream,
        quality: ImageQuality = ImageQuality.SMALL
    ): Bitmap?{
        try {
            val bufferedInputStream = BufferedInputStream(inputStream)

            val compressed = BitmapFactory.Options().run {
                bufferedInputStream.mark(bufferedInputStream.available())
                inJustDecodeBounds = true
                BitmapFactory.decodeStream(bufferedInputStream, null, this)

                inSampleSize = calculateSampleSize(
                    options = this,
                    quality = quality
                )

                bufferedInputStream.reset()
                inJustDecodeBounds = false
                BitmapFactory.decodeStream(bufferedInputStream, null, this)
            }
            return compressed
        } catch (ex: Exception){
            ex.printStackTrace()
            return null
        }
    }

    fun getFileName(
        context: Context,
        uri: Uri
    ): String {
        val contentResolver = context.contentResolver

        var result: String? = null
        if (uri.scheme == "content") {
            val cursor: Cursor? = contentResolver.query(uri, null, null, null, null)
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
                }
            } finally {
                cursor?.close()
            }
        }
        if (result == null) {
            result = uri.path
            val cut = result!!.lastIndexOf('/')
            if (cut != -1) {
                result = result.substring(cut + 1)
            }
        }
        return result
    }

    fun calculateSampleSize(
        options: BitmapFactory.Options,
        quality: ImageQuality
    ): Int {
        // Raw height and width of image
        val originalHeight = options.outHeight
        val originalWidth = options.outWidth
        var sampleSize = 1

        var compressedHeight = originalHeight
        var compressedWidth = originalWidth

        while ((compressedHeight * compressedWidth) > quality.maxPixelDensity){
            sampleSize += 1
            compressedHeight /= 2
            compressedWidth /= 2
        }

        return sampleSize
    }

}