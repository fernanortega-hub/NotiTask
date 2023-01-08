package com.fernanortega.notitask.di

import android.content.Context
import androidx.room.Room
import com.fernanortega.notitask.model.local.NotiTaskDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {

    @Singleton
    @Provides
    fun provideRoom(@ApplicationContext context: Context) =
        Room.databaseBuilder(context, NotiTaskDatabase::class.java, "notitask_database").build()

    @Singleton
    @Provides
    fun provideUserDao(database: NotiTaskDatabase) = database.getUserDao()

    @Singleton
    @Provides
    fun provideTaskDao(database: NotiTaskDatabase) = database.getTaskDao()
}