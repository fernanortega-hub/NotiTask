package com.fernanortega.notitask.model.repositories

import com.fernanortega.notitask.model.domain.UserModel
import com.fernanortega.notitask.model.domain.toDomain
import com.fernanortega.notitask.model.local.dao.UserDao
import com.fernanortega.notitask.model.local.entities.UserEntity
import com.fernanortega.notitask.model.response.LocalResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject


class UserRepository @Inject constructor(private val userDao: UserDao) {
    suspend fun insertUserInfo(name: String) {
        return withContext(Dispatchers.IO) {
            userDao.setUserInfo(UserEntity(1, name))
        }
    }

    suspend fun getUserInfo(): LocalResponse<UserModel> {
        return withContext(Dispatchers.IO) {
            try {
                val response = userDao.getUserInfo()
                LocalResponse.Success(data = response.toDomain())
            } catch (err: NullPointerException) {
                LocalResponse.NullResponse(err)
            } catch (err: Exception) {
                LocalResponse.Error(err)
            }
        }
    }
}