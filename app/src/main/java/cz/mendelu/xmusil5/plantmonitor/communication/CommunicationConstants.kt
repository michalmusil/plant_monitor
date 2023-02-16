package cz.mendelu.xmusil5.plantmonitor.communication

object CommunicationConstants {

    const val HOUSE_PLANT_MEASUREMENTS_API_BASE_URL = "http://localhost:3817/api/v1/"
    const val HOUSE_PLANT_MEASUREMENTS_API_UNAUTHORIZED_CODE = 401

    const val CONNECTION_TIMEOUT: Long = 5
    const val READ_TIMEOUT: Long = 5
}