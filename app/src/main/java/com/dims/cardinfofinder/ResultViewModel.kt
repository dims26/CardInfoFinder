package com.dims.cardinfofinder

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class ResultViewModel(application: Application, val cardNumber: Int) : AndroidViewModel(application) {
    private val _loading = MutableLiveData(NetworkState.LOADING)
    val isVisible: LiveData<NetworkState>
        get() = _loading

    private val _cardLiveData = MutableLiveData<Card>()
    val cardLiveData: LiveData<Card>
        get() = _cardLiveData


    fun loadCardDetails(cardService: CardService) {
        Network(cardService).getCard(cardNumber, _loading, _cardLiveData)
    }
}