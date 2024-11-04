package com.cursosant.mvvmarch.favouriteModule.model

import com.cursosant.mvvmarch.common.entities.Wine

class FavouriteRepository(private val db: RoomDatabase) {

    fun getAllWines(): List<Wine>? {

        return try {
            return db.getAllWines()
        } catch (e: Exception) {
            null
        }


    }

    fun addWine(wine: Wine): Long {

        return db.addWine(wine)

    }

    fun deleteWine(wine: Wine): Int {

        return db.deleteWine(wine)

    }
}