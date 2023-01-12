package com.fernanortega.notitask.viewmodel

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

    private val _showDialog = MutableLiveData<Boolean>()
    val showDialog: LiveData<Boolean> = _showDialog

    private val _showButtons = MutableLiveData<Boolean>()
    val showButtons: LiveData<Boolean> = _showButtons

    private val _taskId = MutableLiveData(Long.MIN_VALUE)
    val taskId: LiveData<Long> = _taskId

    private val _taskTitle = MutableLiveData<String>()
    val taskTitle: LiveData<String> = _taskTitle

    private val _taskBody = MutableLiveData<String>()
    val taskBody: LiveData<String> = _taskBody

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
                        (taskUseCase.invokeGetTasks() as LocalResponse.NullResponse).exception.message
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

    fun deleteTask(id: Long) {
        viewModelScope.launch {
            when (taskUseCase.invokeDeleteTask(id)) {
                is LocalResponse.Error -> {
                    _error.value =
                        (taskUseCase.invokeDeleteTask(id) as LocalResponse.Error).error.message
                    _isUILoading.value = false
                }
                is LocalResponse.NullResponse -> {
                    _error.value =
                        (taskUseCase.invokeDeleteTask(id) as LocalResponse.NullResponse).exception.message
                    _isUILoading.value = false
                }
                is LocalResponse.Success -> {
                    hideButtons()
                    getTasks()
                }
            }
        }
    }

    fun closeDialog() {
        _showDialog.value = false
    }

    fun showDialog() {
        _showDialog.value = true
    }

    fun hideButtons() {
        _showButtons.value = false
    }

    fun showButtons() {
        _showButtons.value = true
    }

    fun onInfoChange(id: Long, title: String, body: String) {
        _taskId.value = id
        _taskTitle.value = title
        _taskBody.value = body
    }
}