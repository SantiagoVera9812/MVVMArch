package com.cursosant.mvvmarch.updateModule.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cursosant.mvvmarch.R
import com.cursosant.mvvmarch.common.entities.MyException
import com.cursosant.mvvmarch.common.entities.Wine
import com.cursosant.mvvmarch.common.utils.Constants
import com.cursosant.mvvmarch.updateModule.model.UpdateRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class UpdateViewModel(private val repository: UpdateRepository): ViewModel() {


    private val _inProgress = MutableLiveData<Boolean>()
    val inProgress: LiveData<Boolean> = _inProgress

    private val _snackbarMsg = MutableLiveData<Int>()
    val snackbarMsg: LiveData<Int> = _snackbarMsg

    private val _wine = MutableLiveData<Wine>()
    val wine: LiveData<Wine> = _wine

    fun requestWine(id: Double){
        viewModelScope.launch {
            _inProgress.value = Constants.SHOW

            try {
                withContext(Dispatchers.IO){
                    repository.requestWine(id) {
                        wine ->
                        _wine.postValue(wine)
                    }
                }
            } catch (e: MyException) {
                _snackbarMsg.postValue(e.msgRes)
            } finally {
                _inProgress.value = Constants.HIDE
            }
        }
    }

    fun updateWine(newRating: String){
        viewModelScope.launch {
            _inProgress.value = Constants.SHOW

            try {
                withContext(Dispatchers.IO){
                    _wine.value?.let {
                        it.rating.average = newRating
                        repository.updateWine(it) {
                            _snackbarMsg.postValue(R.string.room_save_success)
                        }
                    }
                }
            } catch (e: MyException) {
                _snackbarMsg.postValue(e.msgRes)
            } finally {
                _inProgress.value = Constants.HIDE
            }
        }
    }
}