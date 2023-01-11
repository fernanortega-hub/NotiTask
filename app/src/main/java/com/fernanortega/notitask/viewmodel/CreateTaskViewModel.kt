package com.fernanortega.notitask.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fernanortega.notitask.model.domain.TaskModel
import com.fernanortega.notitask.viewmodel.usecases.CreateTaskUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateTaskViewModel @Inject constructor(private val useCase: CreateTaskUseCase) :
    ViewModel() {
    private val _title = MutableLiveData<String>()
    val title: LiveData<String> = _title

    private val _body = MutableLiveData<String>()
    val body: LiveData<String> = _body

    private val _checked = MutableLiveData<Boolean>()
    val checked: LiveData<Boolean> = _checked

    private val _isButtonEnabled = MutableLiveData<Boolean>()
    val isButtonEnabled: LiveData<Boolean> = _isButtonEnabled

    fun createTask() {
        viewModelScope.launch {
            useCase.invokeCreateTasks(
                TaskModel(
                    id = System.currentTimeMillis(),
                    taskTitle = _title.value!!,
                    taskBody = _body.value!!,
                    isDone = false
                )
            )
        }
    }

    private fun enableLogin(title: String) = title.isNotBlank()

    fun onTextChange(title: String, body: String) {
        _title.value = title
        _body.value = body
        _isButtonEnabled.value = enableLogin(title)
    }
}