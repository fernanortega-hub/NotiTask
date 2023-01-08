package com.fernanortega.notitask.model.repositories

import com.fernanortega.notitask.model.domain.UserModel
import com.fernanortega.notitask.model.local.dao.UserDao
import com.fernanortega.notitask.model.local.entities.UserEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject


class UserRepository @Inject constructor(private val userDao: UserDao) {
    suspend fun insertUserInfo(name: String) {
        return withContext(Dispatchers.IO) {
            userDao.setUserInfo(UserEntity(name))
        }
    }

    suspend fun getUserInfo() : String {
        return withContext(Dispatchers.IO) {
            userDao.getUserInfo()
        }
    }
}