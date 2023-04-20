package cz.mendelu.xmusil5.plantmonitor.database.repositories.users

import cz.mendelu.xmusil5.plantmonitor.communication.utils.CommunicationResult
import cz.mendelu.xmusil5.plantmonitor.database.daos.UsersDao
import cz.mendelu.xmusil5.plantmonitor.database.repositories.BaseDbRepository
import cz.mendelu.xmusil5.plantmonitor.models.database.entities.DbUser
import kotlinx.coroutines.flow.Flow

class DbUsersRepositoryImpl(
    private val usersDao: UsersDao
): BaseDbRepository(), IDbUsersRepository {
    override suspend fun getAllUsers(): CommunicationResult<List<DbUser>> {
        return processDbCall {
            usersDao.getAll()
        }
    }

    override suspend fun getById(userId: Long): CommunicationResult<DbUser> {
        return processDbCall {
            usersDao.getById(userId)
        }
    }

    override suspend fun addUser(user: DbUser): CommunicationResult<DbUser> {
        val result = processDbCall {
            usersDao.insert(user)
        }
        return when (result){
            is CommunicationResult.Success -> {
                CommunicationResult.Success(user)
            }
            is CommunicationResult.Error -> {
                CommunicationResult.Error(error = result.error)
            }
            is CommunicationResult.Exception -> {
                CommunicationResult.Exception(exception = result.exception)
            }
        }
    }

    override suspend fun updateUser(user: DbUser): CommunicationResult<DbUser> {
        val result = processDbCall {
            usersDao.update(user)
        }
        return when (result){
            is CommunicationResult.Success -> {
                CommunicationResult.Success(user)
            }
            is CommunicationResult.Error -> {
                CommunicationResult.Error(error = result.error)
            }
            is CommunicationResult.Exception -> {
                CommunicationResult.Exception(exception = result.exception)
            }
        }
    }

    override suspend fun deleteUser(user: DbUser): CommunicationResult<Unit> {
        return processDbCall {
            usersDao.delete(user)
        }
    }
}