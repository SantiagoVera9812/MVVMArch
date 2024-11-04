package com.cursosant.mvvmarch.loginModule.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cursosant.mvvmarch.common.entities.FirebaseUser
import com.cursosant.mvvmarch.common.entities.MyException
import com.cursosant.mvvmarch.common.utils.Constants
import com.cursosant.mvvmarch.loginModule.model.LoginRepository
import kotlinx.coroutines.launch
import kotlin.math.log

class LoginViewModel(private val repository: LoginRepository): ViewModel() {


    private val _inProgress = MutableLiveData<Boolean>()
    val inProgress: LiveData<Boolean> = _inProgress

    private val _snackbarMsg = MutableLiveData<Int>()
    val snackbarMsg: LiveData<Int> = _snackbarMsg
//
//    private val _user = MutableLiveData<FirebaseUser?>()
//    val user: LiveData<FirebaseUser?> = _user

    private val _isAuthValid = MutableLiveData<Boolean>()
    val isAuthValid: LiveData<Boolean> = _isAuthValid

    init {
        checkAuth()
    }

    private fun checkAuth() {
        viewModelScope.launch {
            _inProgress.value = Constants.SHOW

            try {
                _isAuthValid.value = repository.checkAuth()
            } finally {

                _inProgress.value = Constants.HIDE
            }
        }
    }

    fun login(username: String, pin: String) {
        viewModelScope.launch {
            _inProgress.value = Constants.SHOW

            try {
                _isAuthValid.value = repository.login(username, pin)
                Log.i("auth value vm", _isAuthValid.value.toString())
            } catch (e: MyException){
                _snackbarMsg.value = e.msgRes
            } finally {

                _inProgress.value = Constants.HIDE
            }
        }
    }


}