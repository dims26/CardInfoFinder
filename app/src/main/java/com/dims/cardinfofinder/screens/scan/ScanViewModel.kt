package com.dims.cardinfofinder.screens.scan

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ScanViewModel : ViewModel() {
    var value: Int? = null
        private set
    private val _isVisible = MutableLiveData(false)
    val isVisible: LiveData<Boolean> get() = _isVisible

    fun check(text: String){
        if (text.length == 8) {
            try {
                value = Integer.valueOf(text)
                _isVisible.postValue(true)
            } catch (e: NumberFormatException) {
                _isVisible.postValue((value != null))
            }
        }
    }
}

