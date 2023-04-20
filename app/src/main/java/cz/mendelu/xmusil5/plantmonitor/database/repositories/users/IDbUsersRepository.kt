package cz.mendelu.xmusil5.plantmonitor.database.repositories.users

import cz.mendelu.xmusil5.plantmonitor.communication.utils.CommunicationResult
import cz.mendelu.xmusil5.plantmonitor.models.database.entities.DbPlant
import cz.mendelu.xmusil5.plantmonitor.models.database.entities.DbUser
import kotlinx.coroutines.flow.Flow

interface IDbUsersRepository {
    suspend fun getAllUsers(): CommunicationResult<List<DbUser>>

    suspend fun getById(userId: Long): CommunicationResult<DbUser>

    suspend fun addUser(user: DbUser): CommunicationResult<DbUser>

    suspend fun updateUser(user: DbUser): CommunicationResult<DbUser>

    suspend fun deleteUser(user: DbUser): CommunicationResult<Unit>
}