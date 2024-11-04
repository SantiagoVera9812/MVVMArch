package com.cursosant.mvvmarch.loginModule.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.cursosant.mvvmarch.loginModule.model.LoginRepository

class LoginViewModelfactory(private val repository: LoginRepository): ViewModelProvider.Factory {


    override fun <T: ViewModel> create(modelClass: Class<T>): T {

        if(modelClass.isAssignableFrom(LoginViewModel::class.java)){

            @Suppress("UNCHECKED_CAST")
            return LoginViewModel(repository) as T
        } else {

            throw IllegalArgumentException("Clase de viewmodel desconocida")
        }
    }
}