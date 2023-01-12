package com.fernanortega.notitask.model.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.fernanortega.notitask.model.local.entities.TaskEntity

@Dao
interface TaskDao {
    @Query("SELECT * FROM task_table")
    suspend fun getAllTasks() : List<TaskEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun createTask(taskEntity: TaskEntity)

    @Query("DELETE FROM task_table WHERE id LIKE :id")
    suspend fun deleteTask(id: Long)
}
