package cz.mendelu.xmusil5.plantmonitor.communication.api.repositories.devices

import cz.mendelu.xmusil5.plantmonitor.communication.utils.CommunicationResult
import cz.mendelu.xmusil5.plantmonitor.models.api.device.Device
import cz.mendelu.xmusil5.plantmonitor.models.api.device.PutDeviceActivation
import cz.mendelu.xmusil5.plantmonitor.models.api.device.PutDevicePlantAssignment
import cz.mendelu.xmusil5.plantmonitor.models.api.device.PutDeviceRegister

interface IDevicesRepository {

    suspend fun getAllDevices(): CommunicationResult<List<Device>>

    suspend fun getDeviceById(deviceId: Long): CommunicationResult<Device>

    suspend fun registerDevice(deviceRegister: PutDeviceRegister): CommunicationResult<Device>

    suspend fun unregisterDevice(deviceId: Long): CommunicationResult<Unit>

    suspend fun deviceActivation(putDeviceActivation: PutDeviceActivation): CommunicationResult<Device>

    suspend fun assignDeviceToPlant(devicePlantAssignment: PutDevicePlantAssignment): CommunicationResult<Device>
}