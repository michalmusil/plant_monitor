package cz.mendelu.xmusil5.plantmonitor.communication.api.repositories.devices

import cz.mendelu.xmusil5.plantmonitor.communication.utils.DataResult
import cz.mendelu.xmusil5.plantmonitor.models.api.device.Device
import cz.mendelu.xmusil5.plantmonitor.models.api.device.PutDeviceActivation
import cz.mendelu.xmusil5.plantmonitor.models.api.device.PutDevicePlantAssignment
import cz.mendelu.xmusil5.plantmonitor.models.api.device.PutDeviceRegister

interface IDevicesRepository {

    suspend fun getAllDevices(): DataResult<List<Device>>

    suspend fun getDeviceById(deviceId: Long): DataResult<Device>

    suspend fun registerDevice(deviceRegister: PutDeviceRegister): DataResult<Device>

    suspend fun unregisterDevice(deviceId: Long): DataResult<Unit>

    suspend fun deviceActivation(putDeviceActivation: PutDeviceActivation): DataResult<Device>

    suspend fun assignDeviceToPlant(devicePlantAssignment: PutDevicePlantAssignment): DataResult<Device>
}