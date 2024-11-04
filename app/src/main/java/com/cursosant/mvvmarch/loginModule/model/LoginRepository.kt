package com.cursosant.mvvmarch.loginModule.model

import android.util.Log
import com.cursosant.mvvmarch.R
import com.cursosant.mvvmarch.common.entities.MyException
import com.cursosant.mvvmarch.common.local.FakeFirebaseAuth
import com.cursosant.mvvmarch.common.utils.Constants

class LoginRepository(private val auth: FakeFirebaseAuth) {

    suspend fun checkAuth(): Boolean    {
        Log.i("auth", auth.isValidAuth().toString())
        return auth.isValidAuth()
    }

    suspend fun login(username: String, pin: String): Boolean {
        val result = auth.login(username, pin)
        if (!result) throw MyException(Constants.EC_LOGIN, R.string.login_login_fail)
        return true
    }
}