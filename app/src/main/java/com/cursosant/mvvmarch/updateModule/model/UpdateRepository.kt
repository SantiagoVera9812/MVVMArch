package com.cursosant.mvvmarch.updateModule.model

import com.cursosant.mvvmarch.R
import com.cursosant.mvvmarch.common.entities.MyException
import com.cursosant.mvvmarch.common.entities.Wine
import com.cursosant.mvvmarch.common.utils.Constants

class UpdateRepository(private val db: RoomDatabase) {

    fun requestWine(id: Double, callback: (Wine) -> Unit){

        try {
            val result = db.getWineById(id)
            callback(result)
        } catch (e: MyException){
            throw MyException(Constants.EC_GET_WINE, R.string.room_request_fail)
        }

    }

    fun updateWine(wine: Wine, callback: () -> Unit){

        val result = db.updateWine(wine)

        if (result == 0){
            throw MyException(Constants.EC_UPDATE_WINE, R.string.room_update_fail)
        } else {
            callback()
        }
    }
}