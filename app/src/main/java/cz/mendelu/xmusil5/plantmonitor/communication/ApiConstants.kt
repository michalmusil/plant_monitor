package cz.mendelu.xmusil5.plantmonitor.communication

object ApiConstants {

    const val HOUSE_PLANT_MEASUREMENTS_API_BASE_URL = "http://10.10.1.191:3817/api/v1/"
    const val HOUSE_PLANT_MEASUREMENTS_API_UNAUTHORIZED_CODE = 401
    const val HOUSE_PLANT_MEASUREMENTS_API_IMAGE_UPLOAD_FORM_PART_NAME = "image"

    const val CONNECTION_TIMEOUT: Long = 5
    const val READ_TIMEOUT: Long = 5
}