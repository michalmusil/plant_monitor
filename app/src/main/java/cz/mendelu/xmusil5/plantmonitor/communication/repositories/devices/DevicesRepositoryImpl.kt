package cz.mendelu.xmusil5.plantmonitor.communication.repositories.devices

import cz.mendelu.xmusil5.plantmonitor.authentication.IAuthenticationManager
import cz.mendelu.xmusil5.plantmonitor.communication.api.HousePlantMeasurementsApi
import cz.mendelu.xmusil5.plantmonitor.communication.utils.BaseApiRepository
import cz.mendelu.xmusil5.plantmonitor.communication.utils.CommunicationResult
import cz.mendelu.xmusil5.plantmonitor.models.api.device.GetDevice
import cz.mendelu.xmusil5.plantmonitor.models.api.device.PutDeviceActivation
import cz.mendelu.xmusil5.plantmonitor.models.api.device.PutDevicePlantAssignment
import cz.mendelu.xmusil5.plantmonitor.models.api.device.PutDeviceRegister
import javax.inject.Inject

class DevicesRepositoryImpl @Inject constructor(
    authenticationManager: IAuthenticationManager,
    private val api: HousePlantMeasurementsApi
): BaseApiRepository(authenticationManager), IDevicesRepository {

    override suspend fun getAllDevices(): CommunicationResult<List<GetDevice>> {
        return processRequest{
            api.getAllDevices(
                userId = authenticationManager.getUserId(),
                bearerToken = authenticationManager.getToken()
            )
        }
    }

    override suspend fun getDeviceById(deviceId: Long): CommunicationResult<GetDevice> {
        return processRequest {
            api.getDeviceById(
                id = deviceId,
                bearerToken = authenticationManager.getToken()
            )
        }
    }

    override suspend fun registerDevice(deviceRegister: PutDeviceRegister): CommunicationResult<GetDevice> {
        return processRequest {
            api.registerDevice(
                putDeviceRegister = deviceRegister,
                bearerToken = authenticationManager.getToken()
            )
        }
    }

    override suspend fun deviceActivation(putDeviceActivation: PutDeviceActivation): CommunicationResult<GetDevice> {
        return processRequest {
            api.deviceActivation(
                putDeviceActivation = putDeviceActivation,
                bearerToken = authenticationManager.getToken()
            )
        }
    }

    override suspend fun assignDeviceToPlant(devicePlantAssignment: PutDevicePlantAssignment): CommunicationResult<GetDevice> {
        return processRequest {
            api.devicePlantAssign(
                putDevicePlantAssignment = devicePlantAssignment,
                bearerToken = authenticationManager.getToken()
            )
        }
    }
}