package com.dims.cardinfofinder.helpers

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dims.cardinfofinder.screens.input.InputViewModel
import com.dims.cardinfofinder.screens.result.ResultViewModel
import com.dims.cardinfofinder.screens.scan.ScanViewModel

class ViewModelFactory(private val cardNumber: Int? = null) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(InputViewModel::class.java) ->
                InputViewModel() as T
            modelClass.isAssignableFrom(ResultViewModel::class.java) && cardNumber != null ->
                ResultViewModel(cardNumber) as T
            modelClass.isAssignableFrom(ScanViewModel::class.java) ->
                ScanViewModel() as T
            else -> throw IllegalArgumentException("Unknown ViewModel class")
        }
    }

}