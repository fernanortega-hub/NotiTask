package com.fernanortega.notitask.viewmodel.usecases

import com.fernanortega.notitask.model.domain.TaskModel
import com.fernanortega.notitask.model.repositories.TaskRepository
import javax.inject.Inject

class CreateTaskUseCase @Inject constructor(private val repo: TaskRepository) {
    suspend fun invokeCreateTasks(task: TaskModel) {
        return repo.insertTask(task)
    }
}