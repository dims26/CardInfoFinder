package com.dims.cardinfofinder

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ViewModelFactory(private val application: Application, private val cardNumber: Int? = null) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(InputViewModel::class.java) ->
                InputViewModel(application) as T
            modelClass.isAssignableFrom(ResultViewModel::class.java) && cardNumber != null ->
                ResultViewModel(application, cardNumber) as T
            else -> throw IllegalArgumentException("Unknown ViewModel class")
        }
    }

}