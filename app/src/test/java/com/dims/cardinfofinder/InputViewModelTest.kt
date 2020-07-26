package com.dims.cardinfofinder

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.dims.cardinfofinder.helpers.ErrorState
import com.dims.cardinfofinder.screens.input.InputViewModel
import com.jraska.livedata.test
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExpectedException
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class InputViewModelTest {

    @get:Rule
    val instantExecutionRule = InstantTaskExecutorRule()
    @get:Rule
    val exceptionRule: ExpectedException = ExpectedException.none()

    private lateinit var viewModel: InputViewModel

    @Before
    fun setup(){
        viewModel = InputViewModel()
    }

    @Test
    fun test_check_noAction(){
        val expected = ErrorState.IDLE

        val actual = viewModel.error.test().value()

        assertEquals(expected, actual)
        exceptionRule.expect(IllegalStateException::class.java)
        viewModel.value
    }

    @Test
    fun test_check_correctInput(){
        val textLiveData = viewModel.textLiveData
        val stringValue = "53998345"
        val expectedState = ErrorState.INVISIBLE
        val expectedValue = stringValue.toInt()

        textLiveData.postValue(stringValue)
        val actualState = viewModel.error.test().value()
        val actualValue = viewModel.value

        assertEquals(expectedValue, actualValue)
        assertEquals(expectedState, actualState)
    }

    @Test
    fun test_check_nonNumberInput(){
        val textLiveData = viewModel.textLiveData
        val stringValue = "visacard"
        val expectedState = ErrorState.VISIBLE


        textLiveData.postValue(stringValue)
        val actualState = viewModel.error.test().value()

        assertEquals(expectedState, actualState)
        exceptionRule.expect(IllegalStateException::class.java)
        viewModel.value
    }

    @Test
    fun test_check_incorrectNumberOfDigitsInput(){
        val textLiveData = viewModel.textLiveData
        val stringValue = "539983"
        val expectedState = ErrorState.VISIBLE


        textLiveData.postValue(stringValue)
        val actualState = viewModel.error.test().value()

        assertEquals(expectedState, actualState)
        exceptionRule.expect(IllegalStateException::class.java)
        viewModel.value
    }
}