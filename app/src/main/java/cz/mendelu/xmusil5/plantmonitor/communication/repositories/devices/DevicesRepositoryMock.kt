package cz.mendelu.xmusil5.plantmonitor.communication.repositories.devices

import cz.mendelu.xmusil5.plantmonitor.authentication.AuthenticationManagerMock
import cz.mendelu.xmusil5.plantmonitor.communication.utils.CommunicationError
import cz.mendelu.xmusil5.plantmonitor.communication.utils.CommunicationResult
import cz.mendelu.xmusil5.plantmonitor.models.api.device.GetDevice
import cz.mendelu.xmusil5.plantmonitor.models.api.device.PutDeviceActivation
import cz.mendelu.xmusil5.plantmonitor.models.api.device.PutDevicePlantAssignment
import cz.mendelu.xmusil5.plantmonitor.models.api.device.PutDeviceRegister

class DevicesRepositoryMock: IDevicesRepository {

    companion object{
        val DEVICES = listOf(
            GetDevice(
                id = 1,
                communicationId = "292fh92ufh923hfde",
                active = true,
                userId = AuthenticationManagerMock.MOCKED_USER_ID,
                plantId = 1
            ),
            GetDevice(
                id = 2,
                communicationId = "hdh827y387123h8jnvaz",
                active = false,
                userId = AuthenticationManagerMock.MOCKED_USER_ID,
                plantId = null
            ),
            GetDevice(
                id = 3,
                communicationId = "hdh827adfgiuh34921",
                active = true,
                userId = AuthenticationManagerMock.MOCKED_USER_ID,
                plantId = 2
            ),
            GetDevice(
                id = 4,
                communicationId = "1wcbg5vjopfv34921",
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
            communicationId = deviceRegister.communicationId,
            active = true,
            userId = AuthenticationManagerMock.MOCKED_USER_ID,
            plantId = null
        )
        return CommunicationResult.Success(data = newDevice)
    }

    override suspend fun deviceActivation(putDeviceActivation: PutDeviceActivation): CommunicationResult<GetDevice> {
        val foundDevice = DEVICES.firstOrNull{ it.id == putDeviceActivation.deviceId }
        foundDevice?.let {
            val activatedDevice = GetDevice(
                id = it.id,
                communicationId = it.communicationId,
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
                communicationId = it.communicationId,
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