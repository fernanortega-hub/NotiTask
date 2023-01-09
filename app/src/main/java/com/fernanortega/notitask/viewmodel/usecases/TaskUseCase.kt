package com.fernanortega.notitask.viewmodel.usecases

import com.fernanortega.notitask.model.domain.UserModel
import com.fernanortega.notitask.model.repositories.TaskRepository
import com.fernanortega.notitask.model.repositories.UserRepository
import com.fernanortega.notitask.model.response.LocalResponse
import okhttp3.internal.concurrent.Task
import javax.inject.Inject

class TaskUseCase @Inject constructor(private val userRepository: UserRepository, private val taskRepo: TaskRepository){
    suspend fun invokeGetUserInfo() : LocalResponse<UserModel> {
        return userRepository.getUserInfo()
    }
}