package cz.mendelu.xmusil5.plantmonitor.communication.repositories.devices

import cz.mendelu.xmusil5.plantmonitor.communication.utils.CommunicationResult
import cz.mendelu.xmusil5.plantmonitor.models.api.device.GetDevice
import cz.mendelu.xmusil5.plantmonitor.models.api.device.PostDeviceActivation
import cz.mendelu.xmusil5.plantmonitor.models.api.device.PostDevicePlantAssignment
import cz.mendelu.xmusil5.plantmonitor.models.api.device.PostDeviceRegister

interface IDevicesRepository {

    suspend fun getAllDevices(): CommunicationResult<List<GetDevice>>

    suspend fun getDeviceById(deviceId: Long): CommunicationResult<GetDevice>

    suspend fun registerDevice(deviceRegister: PostDeviceRegister): CommunicationResult<GetDevice>

    suspend fun deviceActivation(postDeviceActivation: PostDeviceActivation): CommunicationResult<GetDevice>

    suspend fun assignDeviceToPlant(devicePlantAssignment: PostDevicePlantAssignment): CommunicationResult<GetDevice>
}