package com.fernanortega.notitask.model.domain

import com.fernanortega.notitask.model.local.entities.UserEntity

data class UserModel(val id: Int, val name: String)

fun UserEntity.toDomain() = UserModel(id, name)
