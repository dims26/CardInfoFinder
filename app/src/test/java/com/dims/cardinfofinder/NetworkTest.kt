package com.dims.cardinfofinder

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.dims.cardinfofinder.helpers.NetworkState
import com.dims.cardinfofinder.model.Bank
import com.dims.cardinfofinder.model.Card
import com.dims.cardinfofinder.model.Country
import com.dims.cardinfofinder.network.CardService
import com.dims.cardinfofinder.network.Network
import com.jraska.livedata.test
import com.nhaarman.mockitokotlin2.*
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExpectedException
import org.junit.runner.RunWith
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.HttpURLConnection

@RunWith(AndroidJUnit4::class)
class NetworkTest {

    @get:Rule
    //rule to run all async operations immediately
    val testRule = InstantTaskExecutorRule()
    @get:Rule
    //allows to verify that a certain rule was thrown
    val exceptionRule: ExpectedException = ExpectedException.none()


    private val json = "{\"number\":{},\"scheme\":\"mastercard\",\"type\":\"debit\",\"brand\":\"Debit\",\"country\":{\"numeric\":\"566\",\"alpha2\":\"NG\",\"name\":\"Nigeria\",\"emoji\":\"\uD83C\uDDF3\uD83C\uDDEC\",\"currency\":\"NGN\",\"latitude\":10,\"longitude\":8},\"bank\":{\"name\":\"GTBANK\",\"phone\":\"2348039003900\"}}"

    private val cardNumber = 53998345
    private val card = Card(
        "mastercard", "debit",
        Bank("GTBank"), Country("Nigeria")
    )
    private lateinit var cardService: CardService
    private lateinit var indicatorLiveData: MutableLiveData<NetworkState>
    private lateinit var cardLiveData: MutableLiveData<Card>
    private lateinit var mockWebServer: MockWebServer


    @Before
    fun setup(){
        mockWebServer = MockWebServer()
        mockWebServer.start()
        val logger = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        val client = OkHttpClient.Builder().addInterceptor(logger).build()
        val retrofit = Retrofit.Builder().baseUrl(mockWebServer.url("/"))
//            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        cardService = spy(retrofit.create(CardService::class.java))

        indicatorLiveData = spy(MutableLiveData())
        cardLiveData = spy(MutableLiveData())
    }

    @After
    fun tearDown(){
        mockWebServer.shutdown()
    }

    @Test
    fun test_getCardCall() = runBlocking{
        val network = spy(Network(cardService))
        mockWebServer.enqueue(MockResponse()
            .setResponseCode(HttpURLConnection.HTTP_OK)
            .setBody(json))


        network.getCard(cardNumber, indicatorLiveData, cardLiveData)


        verify(cardService).getCard(cardNumber)
        verify(network).getCardCallback(indicatorLiveData, cardLiveData)
        return@runBlocking
    }

    @Test
    //runBlocking blocks the thread till coroutine completion
    fun test_Callback_onResponse_successful() = runBlocking{
        val network = Network(cardService)
        val response = Response.success(200, card)
        val callback = network.getCardCallback(indicatorLiveData, cardLiveData)
        val call = mock<Call<Card>>{call ->
            on(call.enqueue(eq(callback))).doAnswer {
                callback.onResponse(call, response)
            }
        }
        val expected = card

        call.enqueue(callback)

        verify(indicatorLiveData).postValue(NetworkState.LOADED)
        verify(cardLiveData).postValue(expected)
        return@runBlocking
    }

    @Test
    //runBlocking blocks the thread till coroutine completion
    fun test_Callback_onResponse_notFound() = runBlocking{
        val network = Network(cardService)
        val response = Response.error<Card>(400, mock())
        val callback = network.getCardCallback(indicatorLiveData, cardLiveData)
        val call = mock<Call<Card>>{call ->
            on(call.enqueue(eq(callback))).doAnswer {
                callback.onResponse(call, response)
            }
        }

        call.enqueue(callback)

        verify(indicatorLiveData).postValue(NetworkState.ERROR)
        verify(cardLiveData, never()).postValue(any())
        return@runBlocking
    }

    @Test
    //runBlocking blocks the thread till coroutine completion
    fun test_Callback_onFailure() = runBlocking{
        val network = Network(cardService)
        val callback = network.getCardCallback(indicatorLiveData, cardLiveData)
        val call = mock<Call<Card>>{call ->
            on(call.enqueue(eq(callback))).doAnswer {
                callback.onFailure(call, mock())
            }
        }

        call.enqueue(callback)

        verify(indicatorLiveData).postValue(NetworkState.ERROR)
        verify(cardLiveData, never()).postValue(any())
        return@runBlocking
    }
}