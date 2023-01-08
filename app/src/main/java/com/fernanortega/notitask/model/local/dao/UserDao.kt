package com.fernanortega.notitask.model.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.fernanortega.notitask.model.local.entities.UserEntity

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun setUserInfo(user: UserEntity)

    @Query("SELECT name FROM user_table")
    suspend fun getUserInfo() : String
}