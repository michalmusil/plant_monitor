package cz.mendelu.xmusil5.plantmonitor.database.repositories.devices

import cz.mendelu.xmusil5.plantmonitor.communication.utils.CommunicationResult
import cz.mendelu.xmusil5.plantmonitor.database.daos.DevicesDao
import cz.mendelu.xmusil5.plantmonitor.database.repositories.BaseDbRepository
import cz.mendelu.xmusil5.plantmonitor.models.database.entities.DbDevice
import kotlinx.coroutines.flow.Flow

class DbDevicesRepositoryImpl(
    private val devicesDao: DevicesDao
): BaseDbRepository(), IDbDevicesRepository {
    override suspend fun getAllDevices(): CommunicationResult<List<DbDevice>> {
        return processDbCall {
            devicesDao.getAll()
        }
    }

    override suspend fun getById(deviceId: Long): CommunicationResult<DbDevice> {
        val result = processDbCall {
            devicesDao.getById(deviceId)
        }
        return when (result){
            is CommunicationResult.Success -> {
                CommunicationResult.Success(result.data)
            }
            is CommunicationResult.Error -> {
                CommunicationResult.Error(error = result.error)
            }
            is CommunicationResult.Exception -> {
                CommunicationResult.Exception(exception = result.exception)
            }
        }
    }

    override suspend fun addDevice(device: DbDevice): CommunicationResult<DbDevice> {
        val result = processDbCall {
            devicesDao.insert(device)
        }
        return when (result){
            is CommunicationResult.Success -> {
                CommunicationResult.Success(device)
            }
            is CommunicationResult.Error -> {
                CommunicationResult.Error(error = result.error)
            }
            is CommunicationResult.Exception -> {
                CommunicationResult.Exception(exception = result.exception)
            }
        }
    }

    override suspend fun updateDevice(device: DbDevice): CommunicationResult<DbDevice> {
        val result = processDbCall {
            devicesDao.update(device)
        }
        return when (result){
            is CommunicationResult.Success -> {
                CommunicationResult.Success(device)
            }
            is CommunicationResult.Error -> {
                CommunicationResult.Error(error = result.error)
            }
            is CommunicationResult.Exception -> {
                CommunicationResult.Exception(exception = result.exception)
            }
        }
    }

    override suspend fun deleteDevice(device: DbDevice): CommunicationResult<Unit> {
        return processDbCall {
            devicesDao.delete(device)
        }
    }
}