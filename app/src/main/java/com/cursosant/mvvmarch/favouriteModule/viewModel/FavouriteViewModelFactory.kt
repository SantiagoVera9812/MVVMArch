package com.cursosant.mvvmarch.favouriteModule.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.cursosant.mvvmarch.accountModule.model.AccountRepository
import com.cursosant.mvvmarch.favouriteModule.model.FavouriteRepository


class FavouriteViewModelFactory(private val repository: FavouriteRepository): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FavouriteViewModel::class.java)){
            @Suppress("UNCHECKED_CAST")
            return FavouriteViewModel(repository) as T
        }
        throw IllegalArgumentException("Clase de view model desconocida")
    }
}