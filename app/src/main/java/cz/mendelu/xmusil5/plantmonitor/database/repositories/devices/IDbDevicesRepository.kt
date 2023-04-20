package cz.mendelu.xmusil5.plantmonitor.database.repositories.devices

import cz.mendelu.xmusil5.plantmonitor.communication.utils.CommunicationResult
import cz.mendelu.xmusil5.plantmonitor.models.database.entities.DbDevice
import kotlinx.coroutines.flow.Flow

interface IDbDevicesRepository {
    suspend fun getAllDevices(): CommunicationResult<List<DbDevice>>

    suspend fun getById(deviceId: Long): CommunicationResult<DbDevice>

    suspend fun addDevice(device: DbDevice): CommunicationResult<DbDevice>

    suspend fun updateDevice(device: DbDevice): CommunicationResult<DbDevice>

    suspend fun deleteDevice(device: DbDevice): CommunicationResult<Unit>
}