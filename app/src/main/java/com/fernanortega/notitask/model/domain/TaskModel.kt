package com.fernanortega.notitask.model.domain

import com.fernanortega.notitask.model.local.entities.TaskEntity

data class TaskModel(
    val id: Long = System.currentTimeMillis(),
    val taskTitle: String,
    val taskBody: String,
    val isDone: Boolean
)

fun TaskEntity.toDomain() = TaskModel(id, taskTitle, taskBody, isDone)


