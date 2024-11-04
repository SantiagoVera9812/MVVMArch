package com.cursosant.mvvmarch.favouriteModule.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cursosant.mvvmarch.R
import com.cursosant.mvvmarch.common.entities.Wine
import com.cursosant.mvvmarch.common.utils.Constants
import com.cursosant.mvvmarch.common.viewModel.BaseWineViewModel
import com.cursosant.mvvmarch.favouriteModule.model.FavouriteRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FavouriteViewModel(private val repository: FavouriteRepository): BaseWineViewModel() {


    init {
        getAllWines()
    }

    override fun getAllWines(){

        viewModelScope.launch {
            setInProgress(Constants.SHOW)

            try {

                withContext(Dispatchers.IO) {

                    val result = repository.getAllWines()

                    result?.let { setWines(it) }

                    if (result == null) {

                        setSnackbarMsg(R.string.room_request_fail)
                    }
                }

            } catch (e: Exception) {

                setSnackbarMsg(R.string.room_request_fail)

            } finally {

                setInProgress(Constants.HIDE)

            }
        }


    }

    override fun addWine(wine: Wine){

        viewModelScope.launch {
            setInProgress(Constants.SHOW)

            try {

                withContext(Dispatchers.IO) {

                    val result = repository.addWine(wine)

                    if (result != -1L) {

                        setSnackbarMsg(R.string.room_save_success)

                    } else {

                        setSnackbarMsg(R.string.room_save_fail)
                    }
                }

            } finally {

                setInProgress(Constants.HIDE)

            }
        }

    }

    fun deleteWine(wine: Wine){

        viewModelScope.launch {
            setInProgress(Constants.SHOW)

            try {

                withContext(Dispatchers.IO) {

                val result = repository.deleteWine(wine)

                if(result == 0) {

                    setSnackbarMsg(R.string.room_save_fail)

                } else {

                    setSnackbarMsg(R.string.room_save_success)
                }
                    }

            } finally {

                setInProgress(Constants.HIDE)

            }
        }

    }

}