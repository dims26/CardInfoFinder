package com.dims.cardinfofinder

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.dims.cardinfofinder.network.CardService
import com.dims.cardinfofinder.network.ServiceBuilder
import com.dims.cardinfofinder.screens.input.InputViewModel
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExpectedException
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ServiceBuilderTest {

    @get:Rule
    val exceptionRule: ExpectedException = ExpectedException.none()


    fun test_buildService_interfaceInput(){
        val cls = CardService::class.java

        val actual = ServiceBuilder.buildService(cls)

        assertNotNull(actual)
        assertEquals(cls, actual?.javaClass)

    }

    @Test
    fun test_buildService_nonInterfaceInput(){
        val cls = InputViewModel::class.java

        val actual = ServiceBuilder.buildService(cls)

        assertNull(actual)
    }
}