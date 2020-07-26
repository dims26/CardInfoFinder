package com.dims.cardinfofinder.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.IllegalArgumentException


object ServiceBuilder {
    private const val URL = "https://lookup.binlist.net"
    private val logger = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor {
            val request  = it.request()
                .newBuilder()
                .addHeader("Accept-Version", "3")
                .build()
            it.proceed(request)
        }
        .addInterceptor(logger)

    private val builder = Retrofit.Builder().baseUrl(URL)
        .client(okHttpClient.build())
        .addConverterFactory(GsonConverterFactory.create())

    private val retrofit = builder.build()

    fun <S> buildService(serviceType: Class<S>): S? {
        return try {
            retrofit.create(serviceType)
        }catch (e: IllegalArgumentException){
            null
        }
    }
}