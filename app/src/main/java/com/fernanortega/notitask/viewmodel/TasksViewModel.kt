package com.fernanortega.notitask.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fernanortega.notitask.model.domain.UserModel
import com.fernanortega.notitask.model.response.LocalResponse
import com.fernanortega.notitask.viewmodel.usecases.TaskUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TasksViewModel @Inject constructor(private val taskUseCase: TaskUseCase) : ViewModel() {
    private val _username = MutableLiveData<String>()
    val username: LiveData<String> = _username

    private val _userExists = MutableLiveData<Boolean>()
    val userExists: LiveData<Boolean> = _userExists

    fun getUserInfo() {
        viewModelScope.launch {
            when(taskUseCase.invokeGetUserInfo()) {
                is LocalResponse.Error -> _userExists.value = false
                is LocalResponse.NullResponse -> _userExists.value = false
                is LocalResponse.Success -> {
                    _userExists.value = true
                    _username.value = (taskUseCase.invokeGetUserInfo() as LocalResponse.Success<UserModel>).data.name
                }
            }
        }
    }
}