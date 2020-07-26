package com.dims.cardinfofinder

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.dims.cardinfofinder.screens.scan.ScanViewModel
import com.jraska.livedata.test
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExpectedException
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ScanViewModelTest {

    @get:Rule
    val instantExecutionRule = InstantTaskExecutorRule()
    @get:Rule
    val exceptionRule: ExpectedException = ExpectedException.none()

    private lateinit var viewModel: ScanViewModel

    @Before
    fun setup(){
        viewModel = ScanViewModel()
    }

    @Test
    fun test_check_noAction(){
        val isVisible = viewModel.isVisible.test().value()
        val actualValue = viewModel.value



        assertFalse(isVisible)
        assertNull(actualValue)
    }

    @Test
    fun test_check_correctInput(){
        val stringValue = "53998345"
        val expectedValue = stringValue.toInt()

        viewModel.check(stringValue)

        val isVisible = viewModel.isVisible.test().value()
        val actualValue = viewModel.value
        assertEquals(expectedValue, actualValue)
        assertTrue(isVisible)
    }

    @Test
    fun test_check_nonNumberInput() {
        val stringValue = "visacard"


        viewModel.check(stringValue)

        val isVisible = viewModel.isVisible.test().value()
        val actualValue = viewModel.value
        assertFalse(isVisible)
        assertNull(actualValue)
    }

    @Test
    fun test_check_incorrectNumberOfDigitsInput(){
        val stringValue = "539983"


        viewModel.check(stringValue)

        val isVisible = viewModel.isVisible.test().value()
        val actualValue = viewModel.value
        assertFalse(isVisible)
        assertNull(actualValue)
    }
}