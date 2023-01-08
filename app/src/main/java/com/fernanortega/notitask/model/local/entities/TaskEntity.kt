package com.fernanortega.notitask.model.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Calendar

@Entity(tableName = "task_table")
data class TaskEntity(
    @PrimaryKey @ColumnInfo(name = "id") val id: Long = Calendar.getInstance().timeInMillis,
    @ColumnInfo(name = "task_title") val taskTitle: String,
    @ColumnInfo(name = "task_body") val taskBody: String,
    @ColumnInfo(name = "is_done") val isDone: Boolean
)