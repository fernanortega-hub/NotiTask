package com.fernanortega.notitask.model.repositories

import com.fernanortega.notitask.model.local.dao.TaskDao
import javax.inject.Inject

class TaskRepository @Inject constructor(private val taskDao: TaskDao) {

}
