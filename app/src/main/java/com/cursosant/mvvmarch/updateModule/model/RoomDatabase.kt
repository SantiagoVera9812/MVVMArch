package com.cursosant.mvvmarch.updateModule.model

import com.cursosant.mvvmarch.WineApplication
import com.cursosant.mvvmarch.common.entities.Wine
import com.cursosant.mvvmarch.common.room.WineDao

class RoomDatabase {

    private val dao: WineDao by lazy { WineApplication.database.wineDao() }

    fun getWineById(id: Double) = dao.getWineById(id)

    fun updateWine(wine: Wine) = dao.updateWine(wine)
}