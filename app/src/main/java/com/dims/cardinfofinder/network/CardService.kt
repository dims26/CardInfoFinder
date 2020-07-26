package com.dims.cardinfofinder.network

import com.dims.cardinfofinder.model.Card
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface CardService {
    @GET("{number}")
    fun getCard(@Path("number")number: Int) : Call<Card>
}