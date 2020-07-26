package com.dims.cardinfofinder.screens.input

import android.app.Application
import androidx.lifecycle.*
import com.dims.cardinfofinder.helpers.ErrorState
import java.lang.NumberFormatException
import kotlin.properties.Delegates

class InputViewModel : ViewModel() {
    var textLiveData = MutableLiveData<String>()
    private val valueDelegate = Delegates.notNull<Int>()
    var value by valueDelegate
        private set
    private val _error = MutableLiveData(ErrorState.IDLE)
    val error: LiveData<ErrorState> get() = _error
    private val _textObserver = Observer<String> {
        check(it)
    }

    init {
        textLiveData.observeForever(_textObserver)
    }

    private fun check(text: String){
        if (text.length == 8) {
            try {
                value = Integer.valueOf(text)
                _error.postValue(ErrorState.INVISIBLE)
            } catch (e: NumberFormatException) {
                _error.postValue(ErrorState.VISIBLE)
            }
        }else _error.postValue(ErrorState.VISIBLE)
    }
}