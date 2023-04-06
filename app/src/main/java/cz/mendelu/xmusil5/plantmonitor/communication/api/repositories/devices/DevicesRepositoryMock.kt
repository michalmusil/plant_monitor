package cz.mendelu.xmusil5.plantmonitor.communication.api.repositories.devices

import cz.mendelu.xmusil5.plantmonitor.authentication.AuthenticationManagerMock
import cz.mendelu.xmusil5.plantmonitor.communication.utils.CommunicationError
import cz.mendelu.xmusil5.plantmonitor.communication.utils.CommunicationResult
import cz.mendelu.xmusil5.plantmonitor.models.api.device.GetDevice
import cz.mendelu.xmusil5.plantmonitor.models.api.device.PutDeviceActivation
import cz.mendelu.xmusil5.plantmonitor.models.api.device.PutDevicePlantAssignment
import cz.mendelu.xmusil5.plantmonitor.models.api.device.PutDeviceRegister

class DevicesRepositoryMock: IDevicesRepository {

    companion object{
        val DEVICES = mutableListOf(
            GetDevice(
                id = 1,
                active = true,
                userId = AuthenticationManagerMock.MOCKED_USER_ID,
                plantId = 1
            ),
            GetDevice(
                id = 2,
                active = false,
                userId = AuthenticationManagerMock.MOCKED_USER_ID,
                plantId = null
            ),
            GetDevice(
                id = 3,
                active = true,
                userId = AuthenticationManagerMock.MOCKED_USER_ID,
                plantId = 2
            ),
            GetDevice(
                id = 4,
                active = false,
                userId = AuthenticationManagerMock.MOCKED_USER_ID,
                plantId = 3
            ),

        )
    }

    override suspend fun getAllDevices(): CommunicationResult<List<GetDevice>> {
        return CommunicationResult.Success(data = DEVICES)
    }

    override suspend fun getDeviceById(deviceId: Long): CommunicationResult<GetDevice> {
        val foundDevice = DEVICES.firstOrNull{ it.id == deviceId }
        foundDevice?.let {
            return CommunicationResult.Success(data = it)
        }
        return CommunicationResult.Error(
            error = CommunicationError(
                code = 404,
                message = "Device not found"
            )
        )
    }

    override suspend fun registerDevice(deviceRegister: PutDeviceRegister): CommunicationResult<GetDevice> {
        val maxId = DEVICES.maxOf { it.id }
        val newDevice = GetDevice(
            id = maxId,
            active = true,
            userId = AuthenticationManagerMock.MOCKED_USER_ID,
            plantId = null
        )
        return CommunicationResult.Success(data = newDevice)
    }

    override suspend fun unregisterDevice(deviceId: Long): CommunicationResult<Unit> {
        DEVICES.removeIf {
            it.id == deviceId
        }
        return CommunicationResult.Success(Unit)
    }

    override suspend fun deviceActivation(putDeviceActivation: PutDeviceActivation): CommunicationResult<GetDevice> {
        val foundDevice = DEVICES.firstOrNull{ it.id == putDeviceActivation.deviceId }
        foundDevice?.let {
            val activatedDevice = GetDevice(
                id = it.id,
                active = putDeviceActivation.isActive,
                userId = it.userId,
                plantId = it.plantId
            )
            return CommunicationResult.Success(data = activatedDevice)
        }
        return CommunicationResult.Error(
            error = CommunicationError(
                code = 404,
                message = "Device not found"
            )
        )
    }

    override suspend fun assignDeviceToPlant(devicePlantAssignment: PutDevicePlantAssignment): CommunicationResult<GetDevice> {
        val foundDevice = DEVICES.firstOrNull{ it.id == devicePlantAssignment.deviceId }
        foundDevice?.let {
            val assignedDevice = GetDevice(
                id = it.id,
                active = it.active,
                userId = it.userId,
                plantId = devicePlantAssignment.plantId
            )
            return CommunicationResult.Success(data = assignedDevice)
        }
        return CommunicationResult.Error(
            error = CommunicationError(
                code = 404,
                message = "Device not found"
            )
        )
    }
}