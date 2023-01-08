package com.fernanortega.notitask.model.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.fernanortega.notitask.model.local.dao.TaskDao
import com.fernanortega.notitask.model.local.dao.UserDao
import com.fernanortega.notitask.model.local.entities.TaskEntity
import com.fernanortega.notitask.model.local.entities.UserEntity

@Database(entities = [UserEntity::class, TaskEntity::class], version = 1, exportSchema = false)
abstract class NotiTaskDatabase : RoomDatabase() {
    abstract fun getUserDao(): UserDao
    abstract fun getTaskDao(): TaskDao
}