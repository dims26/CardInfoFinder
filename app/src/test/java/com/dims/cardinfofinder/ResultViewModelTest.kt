package com.dims.cardinfofinder

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.dims.cardinfofinder.helpers.NetworkState
import com.dims.cardinfofinder.model.Bank
import com.dims.cardinfofinder.model.Card
import com.dims.cardinfofinder.model.Country
import com.dims.cardinfofinder.network.Network
import com.dims.cardinfofinder.screens.result.ResultViewModel
import com.jraska.livedata.test
import com.nhaarman.mockitokotlin2.*
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExpectedException
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ResultViewModelTest {

    @get:Rule
    val instantExecutionRule = InstantTaskExecutorRule()
    @get:Rule
    val exceptionRule: ExpectedException = ExpectedException.none()

    private lateinit var viewModel: ResultViewModel
    private val cardNumber = 53998345
    private val card = Card(
        "mastercard", "debit",
        Bank("GTBank"), Country("Nigeria")
    )

    @Before
    fun setup(){
        viewModel = ResultViewModel(cardNumber)
    }

    @Test
    fun test_loadCardDetails(){
        val network = mock<Network>()

        viewModel.loadCardDetails(network)

        verify(network).getCard(any(), any(), any())
    }

    @Test
    fun test_liveData_successfulLoad(){
        val network = mock<Network>{network ->
            on(network.getCard(eq(cardNumber), any(), any())).doAnswer {
                (it.getArgument<MutableLiveData<NetworkState>>(1)).postValue(NetworkState.LOADED)
                (it.getArgument<MutableLiveData<Card>>(2)).postValue(card)
            }
        }
        val expectedState = NetworkState.LOADED
        val expectedCard = card

        viewModel.loadCardDetails(network)
        val actualState = viewModel.loading.test().value()
        val actualCard = viewModel.cardLiveData.test().value()

        assertEquals(expectedState, actualState)
        assertEquals(expectedCard, actualCard)
        verify(network).getCard(eq(cardNumber), any(), any())
    }

    @Test
    fun test_liveData_failedLoad(){
        val network = mock<Network>{network ->
            on(network.getCard(eq(cardNumber), any(), any())).doAnswer {
                (it.getArgument<MutableLiveData<NetworkState>>(1)).postValue(NetworkState.ERROR)
            }
        }
        val expectedState = NetworkState.ERROR

        viewModel.loadCardDetails(network)
        val actualState = viewModel.loading.test().value()
        val actualCard = viewModel.cardLiveData.value

        assertEquals(expectedState, actualState)
        assertNull(actualCard)
        verify(network).getCard(eq(cardNumber), any(), any())
    }

    @Test
    fun test_liveData_loading(){
        val network = mock<Network>{network ->
            on(network.getCard(eq(cardNumber), any(), any())).doAnswer {
                (it.getArgument<MutableLiveData<NetworkState>>(1)).postValue(NetworkState.LOADING)
            }
        }
        val expectedState = NetworkState.LOADING

        viewModel.loadCardDetails(network)
        val actualState = viewModel.loading.test().value()
        val actualCard = viewModel.cardLiveData.value

        assertEquals(expectedState, actualState)
        assertNull(actualCard)
        verify(network).getCard(eq(cardNumber), any(), any())
    }
}