package com.fernanortega.notitask.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fernanortega.notitask.viewmodel.usecases.TaskUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TasksViewModel @Inject constructor(private val taskUseCase: TaskUseCase): ViewModel() {
    private val _username = MutableLiveData<String>()
    val username: LiveData<String> = _username

    fun getUserInfo() {
        viewModelScope.launch {
            _username.value = taskUseCase.invokeGetUserInfo()
        }
    }
}