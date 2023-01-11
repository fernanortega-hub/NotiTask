package com.fernanortega.notitask.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fernanortega.notitask.model.domain.TaskModel
import com.fernanortega.notitask.model.domain.UserModel
import com.fernanortega.notitask.model.response.LocalResponse
import com.fernanortega.notitask.viewmodel.usecases.TaskUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.internal.concurrent.Task
import javax.inject.Inject

@HiltViewModel
class TasksViewModel @Inject constructor(private val taskUseCase: TaskUseCase) : ViewModel() {
    private val _username = MutableLiveData<String>()
    val username: LiveData<String> = _username

    private val _userExists = MutableLiveData<Boolean>()
    val userExists: LiveData<Boolean> = _userExists

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    private val _tasks = MutableLiveData<List<TaskModel>>()
    val tasks: LiveData<List<TaskModel>> = _tasks

    private val _isUILoading = MutableLiveData<Boolean>()
    val isUILoading: LiveData<Boolean> = _isUILoading

    fun getUserInfo() {
        viewModelScope.launch {
            when (taskUseCase.invokeGetUserInfo()) {
                is LocalResponse.Error -> _userExists.value = false
                is LocalResponse.NullResponse -> _userExists.value = false
                is LocalResponse.Success -> {
                    _userExists.value = true
                    _username.value =
                        (taskUseCase.invokeGetUserInfo() as LocalResponse.Success<UserModel>).data.name
                }
            }
        }
    }

    fun getTasks() {
        viewModelScope.launch {
            _isUILoading.value = true
            when (taskUseCase.invokeGetTasks()) {
                is LocalResponse.Error -> {
                    _error.value =
                        (taskUseCase.invokeGetTasks() as LocalResponse.Error).error.message
                    _isUILoading.value = false
                }
                is LocalResponse.NullResponse -> {
                    _error.value =
                        (taskUseCase.invokeGetTasks() as LocalResponse.Error).error.message
                    _isUILoading.value = false
                }
                is LocalResponse.Success -> {
                    _isUILoading.value = true
                    _tasks.value = (taskUseCase.invokeGetTasks() as LocalResponse.Success).data
                    _isUILoading.value = false
                }
            }
        }
    }
}