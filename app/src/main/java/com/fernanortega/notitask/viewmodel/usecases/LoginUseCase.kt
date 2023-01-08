package com.fernanortega.notitask.viewmodel.usecases

import com.fernanortega.notitask.model.repositories.UserRepository
import javax.inject.Inject

class LoginUseCase @Inject constructor(private val repository: UserRepository) {
    suspend fun invokeAddUserInfo(name: String) {
        return repository.insertUserInfo(name)
    }
}