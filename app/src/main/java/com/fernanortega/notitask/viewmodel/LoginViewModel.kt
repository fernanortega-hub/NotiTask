package com.fernanortega.notitask.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fernanortega.notitask.viewmodel.usecases.LoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val loginUseCase: LoginUseCase) : ViewModel() {
    private val _name = MutableLiveData<String>()
    val name: LiveData<String> = _name

    private val _isButtonEnabled = MutableLiveData<Boolean>()
    val isButtonEnabled: LiveData<Boolean> = _isButtonEnabled

    private fun enableLogin(name: String) = name.isNotBlank()

    fun onNameChange(name: String) {
        _name.value = name
        _isButtonEnabled.value = enableLogin(name)
    }


    fun sendName() {
        viewModelScope.launch {
            loginUseCase.invokeAddUserInfo(name.value!!)
        }
    }
}