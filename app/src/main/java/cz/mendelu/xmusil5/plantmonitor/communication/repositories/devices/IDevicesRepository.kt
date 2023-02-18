package cz.mendelu.xmusil5.plantmonitor.communication.repositories.devices

import cz.mendelu.xmusil5.plantmonitor.communication.utils.CommunicationResult
import cz.mendelu.xmusil5.plantmonitor.models.api.device.GetDevice

interface IDevicesRepository {

    suspend fun getAllDevices(): CommunicationResult<List<GetDevice>>
}