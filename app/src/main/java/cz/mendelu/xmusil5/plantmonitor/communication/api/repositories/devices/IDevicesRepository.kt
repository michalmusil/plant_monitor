package cz.mendelu.xmusil5.plantmonitor.communication.api.repositories.devices

import cz.mendelu.xmusil5.plantmonitor.communication.utils.CommunicationResult
import cz.mendelu.xmusil5.plantmonitor.models.api.device.GetDevice
import cz.mendelu.xmusil5.plantmonitor.models.api.device.PutDeviceActivation
import cz.mendelu.xmusil5.plantmonitor.models.api.device.PutDevicePlantAssignment
import cz.mendelu.xmusil5.plantmonitor.models.api.device.PutDeviceRegister

interface IDevicesRepository {

    suspend fun getAllDevices(): CommunicationResult<List<GetDevice>>

    suspend fun getDeviceById(deviceId: Long): CommunicationResult<GetDevice>

    suspend fun registerDevice(deviceRegister: PutDeviceRegister): CommunicationResult<GetDevice>

    suspend fun deviceActivation(putDeviceActivation: PutDeviceActivation): CommunicationResult<GetDevice>

    suspend fun assignDeviceToPlant(devicePlantAssignment: PutDevicePlantAssignment): CommunicationResult<GetDevice>
}