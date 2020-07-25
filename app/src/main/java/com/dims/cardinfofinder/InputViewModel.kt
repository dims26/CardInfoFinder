package com.dims.cardinfofinder

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import java.lang.NumberFormatException
import kotlin.properties.Delegates

class InputViewModel(application: Application) : AndroidViewModel(application) {
    var textLiveData = MutableLiveData<String>()
    private val valueDelegate = Delegates.notNull<Int>()
    var value by valueDelegate
    private val _error = MutableLiveData(ErrorState.IDLE)
    val error: LiveData<ErrorState> get() = _error
    private val _textObserver = Observer<String> {
        check(it)
    }

    init {
        textLiveData.observeForever(_textObserver)
    }

    private fun check(text: String){
        _error.value =
            if (text.length == 8) ErrorState.INVISIBLE else ErrorState.VISIBLE
        try {
            value = Integer.valueOf(text)
        }catch (e: NumberFormatException){
            _error.value = ErrorState.VISIBLE
        }
    }
}