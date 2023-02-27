package cz.mendelu.xmusil5.plantmonitor.communication.repositories.devices

import cz.mendelu.xmusil5.plantmonitor.authentication.AuthenticationManagerMock
import cz.mendelu.xmusil5.plantmonitor.communication.repositories.user_auth.UserAuthRepositoryMock
import cz.mendelu.xmusil5.plantmonitor.communication.utils.CommunicationError
import cz.mendelu.xmusil5.plantmonitor.communication.utils.CommunicationResult
import cz.mendelu.xmusil5.plantmonitor.models.api.device.GetDevice
import cz.mendelu.xmusil5.plantmonitor.models.api.device.PostDeviceActivation
import cz.mendelu.xmusil5.plantmonitor.models.api.device.PostDevicePlantAssignment
import cz.mendelu.xmusil5.plantmonitor.models.api.device.PostDeviceRegister

class DevicesRepositoryMock: IDevicesRepository {

    companion object{
        val DEVICES = listOf(
            GetDevice(
                id = 1,
                uuid = "292fh92ufh923hfde",
                active = true,
                userId = AuthenticationManagerMock.MOCKED_USER_ID,
                plantId = 1
            ),
            GetDevice(
                id = 2,
                uuid = "hdh827y387123h8jnvaz",
                active = false,
                userId = AuthenticationManagerMock.MOCKED_USER_ID,
                plantId = null
            ),
            GetDevice(
                id = 3,
                uuid = "hdh827adfgiuh34921",
                active = true,
                userId = AuthenticationManagerMock.MOCKED_USER_ID,
                plantId = 2
            ),
            GetDevice(
                id = 4,
                uuid = "1wcbg5vjopfv34921",
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

    override suspend fun registerDevice(deviceRegister: PostDeviceRegister): CommunicationResult<GetDevice> {
        val maxId = DEVICES.maxOf { it.id }
        val newDevice = GetDevice(
            id = maxId,
            uuid = deviceRegister.uuid,
            active = true,
            userId = AuthenticationManagerMock.MOCKED_USER_ID,
            plantId = null
        )
        return CommunicationResult.Success(data = newDevice)
    }

    override suspend fun deviceActivation(postDeviceActivation: PostDeviceActivation): CommunicationResult<GetDevice> {
        val foundDevice = DEVICES.firstOrNull{ it.id == postDeviceActivation.deviceId }
        foundDevice?.let {
            val activatedDevice = GetDevice(
                id = it.id,
                uuid = it.uuid,
                active = postDeviceActivation.isActive,
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

    override suspend fun assignDeviceToPlant(devicePlantAssignment: PostDevicePlantAssignment): CommunicationResult<GetDevice> {
        val foundDevice = DEVICES.firstOrNull{ it.id == devicePlantAssignment.deviceId }
        foundDevice?.let {
            val assignedDevice = GetDevice(
                id = it.id,
                uuid = it.uuid,
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