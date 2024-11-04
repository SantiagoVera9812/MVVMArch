package com.cursosant.mvvmarch.homeModule.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cursosant.mvvmarch.R
import com.cursosant.mvvmarch.common.entities.MyException
import com.cursosant.mvvmarch.common.entities.Wine
import com.cursosant.mvvmarch.common.utils.Constants
import com.cursosant.mvvmarch.common.viewModel.BaseWineViewModel
import com.cursosant.mvvmarch.homeModule.model.HomeRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeViewModel(private val repository: HomeRepository) : BaseWineViewModel(){


    init {
        getAllWines()
    }

    override fun getAllWines() {


        viewModelScope.launch {

            setInProgress(Constants.SHOW)

            try {

                withContext(Dispatchers.IO) {

                    repository.getAllWines { wines ->

                        setWines(wines)

                    }

                } } catch (e: MyException) {

                setSnackbarMsg(e.msgRes)

            }

            finally {

                setInProgress(Constants.HIDE)

            }
        }

    }

    override fun addWine(wine: Wine){

        viewModelScope.launch {

            setInProgress(Constants.SHOW)

            try {

                withContext(Dispatchers.IO) {

                    repository.addWine(wine) {

                       setSnackbarMsg(R.string.room_save_success)
                    }

                } } catch (e: MyException) {

                setSnackbarMsg(e.msgRes)

                }

            finally {

                setInProgress(Constants.HIDE)

            }
        }

    }
}