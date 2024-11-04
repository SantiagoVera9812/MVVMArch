package com.cursosant.mvvmarch.favouriteModule.model

import com.cursosant.mvvmarch.WineApplication
import com.cursosant.mvvmarch.common.entities.Wine
import com.cursosant.mvvmarch.common.room.WineDao

class RoomDatabase {

    private val dao: WineDao by lazy { WineApplication.database.wineDao()}

    fun getAllWines() = dao.getAllWines()

    fun addWine(wine: Wine) = dao.addWine(wine)

    fun deleteWine(wine: Wine) = dao.deleteWine(wine)
}