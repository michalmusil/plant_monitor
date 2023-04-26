package cz.mendelu.xmusil5.plantmonitor.communication.api.repositories.devices

import cz.mendelu.xmusil5.plantmonitor.user_session.UserSessionManagerMock
import cz.mendelu.xmusil5.plantmonitor.communication.utils.DataError
import cz.mendelu.xmusil5.plantmonitor.communication.utils.DataResult
import cz.mendelu.xmusil5.plantmonitor.models.api.device.Device
import cz.mendelu.xmusil5.plantmonitor.models.api.device.PutDeviceActivation
import cz.mendelu.xmusil5.plantmonitor.models.api.device.PutDevicePlantAssignment
import cz.mendelu.xmusil5.plantmonitor.models.api.device.PutDeviceRegister

class DevicesRepositoryMock: IDevicesRepository {

    companion object{
        val DEVICES = mutableListOf(
            Device(
                id = 1,
                active = true,
                userId = UserSessionManagerMock.MOCKED_USER_ID,
                plantId = 1
            ),
            Device(
                id = 2,
                active = false,
                userId = UserSessionManagerMock.MOCKED_USER_ID,
                plantId = null
            ),
            Device(
                id = 3,
                active = true,
                userId = UserSessionManagerMock.MOCKED_USER_ID,
                plantId = 2
            ),
            Device(
                id = 4,
                active = false,
                userId = UserSessionManagerMock.MOCKED_USER_ID,
                plantId = 3
            ),

        )
    }

    override suspend fun getAllDevices(): DataResult<List<Device>> {
        return DataResult.Success(data = DEVICES)
    }

    override suspend fun getDeviceById(deviceId: Long): DataResult<Device> {
        val foundDevice = DEVICES.firstOrNull{ it.id == deviceId }
        foundDevice?.let {
            return DataResult.Success(data = it)
        }
        return DataResult.Error(
            error = DataError(
                code = 404,
                message = "Device not found"
            )
        )
    }

    override suspend fun registerDevice(deviceRegister: PutDeviceRegister): DataResult<Device> {
        val maxId = DEVICES.maxOf { it.id }
        val newDevice = Device(
            id = maxId,
            active = true,
            userId = UserSessionManagerMock.MOCKED_USER_ID,
            plantId = null
        )
        return DataResult.Success(data = newDevice)
    }

    override suspend fun unregisterDevice(deviceId: Long): DataResult<Unit> {
        DEVICES.removeIf {
            it.id == deviceId
        }
        return DataResult.Success(Unit)
    }

    override suspend fun deviceActivation(putDeviceActivation: PutDeviceActivation): DataResult<Device> {
        val foundDevice = DEVICES.firstOrNull{ it.id == putDeviceActivation.deviceId }
        foundDevice?.let {
            val activatedDevice = Device(
                id = it.id,
                active = putDeviceActivation.isActive,
                userId = it.userId,
                plantId = it.plantId
            )
            return DataResult.Success(data = activatedDevice)
        }
        return DataResult.Error(
            error = DataError(
                code = 404,
                message = "Device not found"
            )
        )
    }

    override suspend fun assignDeviceToPlant(devicePlantAssignment: PutDevicePlantAssignment): DataResult<Device> {
        val foundDevice = DEVICES.firstOrNull{ it.id == devicePlantAssignment.deviceId }
        foundDevice?.let {
            val assignedDevice = Device(
                id = it.id,
                active = it.active,
                userId = it.userId,
                plantId = devicePlantAssignment.plantId
            )
            return DataResult.Success(data = assignedDevice)
        }
        return DataResult.Error(
            error = DataError(
                code = 404,
                message = "Device not found"
            )
        )
    }
}