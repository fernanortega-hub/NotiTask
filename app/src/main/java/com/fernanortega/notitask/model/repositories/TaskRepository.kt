package com.fernanortega.notitask.model.repositories

import com.fernanortega.notitask.model.domain.TaskModel
import com.fernanortega.notitask.model.domain.toDomain
import com.fernanortega.notitask.model.local.dao.TaskDao
import com.fernanortega.notitask.model.local.entities.TaskEntity
import com.fernanortega.notitask.model.response.LocalResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class TaskRepository @Inject constructor(private val taskDao: TaskDao) {
    suspend fun getTasks(): LocalResponse<List<TaskModel>> {
        return withContext(Dispatchers.IO) {
            try {
                val response = taskDao.getAllTasks().map {
                    it.toDomain()
                }

                LocalResponse.Success(response)
            } catch (err: NullPointerException) {
                LocalResponse.NullResponse(err)
            } catch (err: Exception) {
                LocalResponse.Error(err)
            }
        }
    }

    suspend fun createTask(task: TaskModel) {
        return withContext(Dispatchers.IO) {
            taskDao.createTask(TaskEntity(task.id, task.taskTitle, task.taskBody, task.isDone))
        }
    }
}
