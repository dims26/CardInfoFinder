package com.dims.cardinfofinder

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Network(private val cardService: CardService) {
    fun getCard(
        number: Int,
        indicator: MutableLiveData<NetworkState>,
        card: MutableLiveData<Card>
    ){
        val call = cardService.getCard(number)
        call.enqueue(getCardCallback(indicator, card))
    }

    private fun getCardCallback(
        indicator: MutableLiveData<NetworkState>,
        cardLiveData: MutableLiveData<Card>
    ): Callback<Card>{
        return object : Callback<Card> {
            override fun onFailure(call: Call<Card>, t: Throwable) {
                indicator.postValue(NetworkState.ERROR)
            }

            override fun onResponse(call: Call<Card>, response: Response<Card>) {
                if (response.isSuccessful){
                    indicator.postValue(NetworkState.LOADED)
                    cardLiveData.postValue(response.body())
                }else indicator.postValue(NetworkState.ERROR)
            }
        }
    }
}