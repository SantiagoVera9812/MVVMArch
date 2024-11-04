package com.cursosant.mvvmarch.promoModule.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cursosant.mvvmarch.common.entities.MyException
import com.cursosant.mvvmarch.common.entities.Promo
import com.cursosant.mvvmarch.promoModule.model.PromoRepository
import kotlinx.coroutines.launch

class PromoViewModel(private val repository: PromoRepository): ViewModel() {

    private val _snackbarMsg = MutableLiveData<Int>()
    val snackbarMsg: LiveData<Int> = _snackbarMsg

    private val _promos = MutableLiveData<List<Promo>>()
    val promos: LiveData<List<Promo>> = _promos

    init {

        getPromos()

    }

    private fun getPromos() {
        viewModelScope.launch {
            try {
                val result = repository.getPromos()
                _promos.value = result
            } catch (e: MyException) {
                _snackbarMsg.value = e.msgRes
            }
        }
    }
}