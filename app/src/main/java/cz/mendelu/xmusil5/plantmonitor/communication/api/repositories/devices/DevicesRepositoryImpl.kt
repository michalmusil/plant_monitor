package cz.mendelu.xmusil5.plantmonitor.communication.api.repositories.devices

import cz.mendelu.xmusil5.plantmonitor.user_session.IUserSessionManager
import cz.mendelu.xmusil5.plantmonitor.communication.api.HousePlantMeasurementsApi
import cz.mendelu.xmusil5.plantmonitor.communication.utils.BaseApiRepository
import cz.mendelu.xmusil5.plantmonitor.communication.utils.DataResult
import cz.mendelu.xmusil5.plantmonitor.models.api.device.Device
import cz.mendelu.xmusil5.plantmonitor.models.api.device.PutDeviceActivation
import cz.mendelu.xmusil5.plantmonitor.models.api.device.PutDevicePlantAssignment
import cz.mendelu.xmusil5.plantmonitor.models.api.device.PutDeviceRegister
import javax.inject.Inject

class DevicesRepositoryImpl @Inject constructor(
    userSessionManager: IUserSessionManager,
    private val api: HousePlantMeasurementsApi
): BaseApiRepository(userSessionManager), IDevicesRepository {

    override suspend fun getAllDevices(): DataResult<List<Device>> {
        return processRequest{
            api.getAllDevices(
                userId = userSessionManager.getUserId(),
                bearerToken = userSessionManager.getToken()
            )
        }
    }

    override suspend fun getDeviceById(deviceId: Long): DataResult<Device> {
        return processRequest {
            api.getDeviceById(
                id = deviceId,
                bearerToken = userSessionManager.getToken()
            )
        }
    }

    override suspend fun registerDevice(deviceRegister: PutDeviceRegister): DataResult<Device> {
        return processRequest {
            api.registerDevice(
                putDeviceRegister = deviceRegister,
                bearerToken = userSessionManager.getToken()
            )
        }
    }

    override suspend fun unregisterDevice(deviceId: Long): DataResult<Unit> {
        return processRequest {
            api.unregisterDevice(
                deviceId = deviceId,
                bearerToken = userSessionManager.getToken()
            )
        }
    }

    override suspend fun deviceActivation(putDeviceActivation: PutDeviceActivation): DataResult<Device> {
        return processRequest {
            api.deviceActivation(
                putDeviceActivation = putDeviceActivation,
                bearerToken = userSessionManager.getToken()
            )
        }
    }

    override suspend fun assignDeviceToPlant(devicePlantAssignment: PutDevicePlantAssignment): DataResult<Device> {
        return processRequest {
            api.devicePlantAssign(
                putDevicePlantAssignment = devicePlantAssignment,
                bearerToken = userSessionManager.getToken()
            )
        }
    }
}