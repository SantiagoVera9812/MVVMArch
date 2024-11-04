package com.cursosant.mvvmarch.promoModule.model

import com.cursosant.mvvmarch.R
import com.cursosant.mvvmarch.common.entities.MyException
import com.cursosant.mvvmarch.common.entities.Promo
import com.cursosant.mvvmarch.common.utils.Constants

class PromoRepository(private val db: Database) {

    fun getPromos():List<Promo> {

        val result = db.getPromos()
        return result.ifEmpty {
            throw MyException(Constants.EC_REQUEST, R.string.promo_request_fail)
        }

    }
}