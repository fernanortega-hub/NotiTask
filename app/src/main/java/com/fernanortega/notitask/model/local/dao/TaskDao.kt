package com.fernanortega.notitask.model.local.dao

import androidx.room.Dao
import androidx.room.Query
import com.fernanortega.notitask.model.local.entities.TaskEntity

@Dao
interface TaskDao {
    @Query("SELECT * FROM task_table")
    suspend fun getAllTasks() : List<TaskEntity>
}
