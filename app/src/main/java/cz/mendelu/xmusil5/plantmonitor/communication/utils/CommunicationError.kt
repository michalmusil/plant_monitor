package cz.mendelu.xmusil5.plantmonitor.communication.utils

data class CommunicationError(
    val code: Int,
    var message: String?,
    var secondaryMessage: String? = null)