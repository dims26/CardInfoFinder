package com.dims.cardinfofinder

import android.os.Bundle
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.dims.cardinfofinder.helpers.NetworkState
import com.dims.cardinfofinder.model.Bank
import com.dims.cardinfofinder.model.Card
import com.dims.cardinfofinder.model.Country
import com.dims.cardinfofinder.screens.result.ResultFragment
import org.hamcrest.Matchers.not
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class ResultFragmentTest {
    private val card = Card("mastercard", "debit", Bank("GTBank"), Country("Nigeria"))
    private val snackbarMessage = "Loading failed, check Internet access and Card number."
    private val cardNumber = 53998345
    private val args by lazy{
        Bundle().apply {
            putInt("cardNumber", cardNumber)
        }
    }

    @Test
    fun test_cardView_shouldBeDisplayed(){
        launchFragmentInContainer<ResultFragment>(themeResId = R.style.AppTheme, fragmentArgs = args)
        onView(withId(R.id.card_cardView)).check(matches(isDisplayed()))
    }

    @Test
    fun test_chipImageView_shouldBeDisplayed(){
        launchFragmentInContainer<ResultFragment>(themeResId = R.style.AppTheme, fragmentArgs = args)
        onView(withId(R.id.chip_ImageView)).check(matches(isDisplayed()))
    }

    @Test
    fun test_cardNumberTextView_shouldBeDisplayed(){
        launchFragmentInContainer<ResultFragment>(themeResId = R.style.AppTheme, fragmentArgs = args)
        onView(withId(R.id.cardNumber_TextView)).check(matches(isDisplayed()))
    }

    @Test
    fun test_progressBar_shouldBeDisplayed(){
        launchFragmentInContainer<ResultFragment>(themeResId = R.style.AppTheme, fragmentArgs = args)
        onView(withId(R.id.detail_progressBar)).check(matches(isDisplayed()))
    }

    @Test
    fun test_detailBlock_shouldNotBeDisplayed(){
        launchFragmentInContainer<ResultFragment>(themeResId = R.style.AppTheme, fragmentArgs = args)
        onView(withId(R.id.detail_block)).check(matches(not(isDisplayed())))
    }

    @Test
    fun test_progressBar_shouldNotBeDisplayedAfterDataLoad(){
        val resultScenario =
            launchFragmentInContainer<ResultFragment>(themeResId = R.style.AppTheme, fragmentArgs = args)


        resultScenario.onFragment { fragment ->
            fragment.viewModel.loadMockData(NetworkState.LOADED, card)
        }


        onView(withId(R.id.detail_progressBar)).check(matches(not(isDisplayed())))
    }

    @Test
    fun test_detailBlock_shouldBeDisplayedAfterDataLoad(){
        val resultScenario =
            launchFragmentInContainer<ResultFragment>(themeResId = R.style.AppTheme, fragmentArgs = args)


        resultScenario.onFragment { fragment ->
            fragment.viewModel.loadMockData(NetworkState.LOADED, card)
        }


        onView(withId(R.id.detail_block)).check(matches(isDisplayed()))
    }

    @Test
    fun test_progressBar_shouldNotBeDisplayedOnUnsuccessfulLoad(){
        val resultScenario =
            launchFragmentInContainer<ResultFragment>(themeResId = R.style.AppTheme, fragmentArgs = args)


        resultScenario.onFragment { fragment ->
            fragment.viewModel.loadMockData(NetworkState.ERROR, null)
        }


        onView(withId(R.id.detail_progressBar)).check(matches(not(isDisplayed())))
    }

    @Test
    fun test_detailBlock_shouldNotBeDisplayedOnUnsuccessfulLoad(){
        val resultScenario =
            launchFragmentInContainer<ResultFragment>(themeResId = R.style.AppTheme, fragmentArgs = args)


        resultScenario.onFragment { fragment ->
            fragment.viewModel.loadMockData(NetworkState.ERROR, null)
        }

        onView(withId(R.id.detail_block)).check(matches(not(isDisplayed())))
    }

    @Test
    fun test_snackBar_shouldBeDisplayedOnUnsuccessfulLoad(){
        val resultScenario =
            launchFragmentInContainer<ResultFragment>(themeResId = R.style.AppTheme, fragmentArgs = args)

        resultScenario.onFragment { fragment ->
            fragment.viewModel.loadMockData(NetworkState.ERROR, null)
        }

        onView(withText(snackbarMessage)).check(matches(isDisplayed()))
    }

    @Test
    fun test_cardNumberTextView_shouldContainInitialText(){
        launchFragmentInContainer<ResultFragment>(themeResId = R.style.AppTheme, fragmentArgs = args)
        onView(withId(R.id.cardNumber_TextView)).check(matches(withText(R.string.cardNumber_placeholder_textView)))
    }

    @Test
    fun test_cardNumberTextView_shouldContainCardNumberAfterLoad(){
        val resultScenario =
            launchFragmentInContainer<ResultFragment>(themeResId = R.style.AppTheme, fragmentArgs = args)


        resultScenario.onFragment { fragment ->
            fragment.viewModel.loadMockData(NetworkState.LOADED, card)
        }


        onView(withId(R.id.cardNumber_TextView)).check(matches(withText(cardNumber.toString())))
    }

    @Test
    fun test_textViews_shouldContainTextAfterLoad(){
        val resultScenario =
            launchFragmentInContainer<ResultFragment>(themeResId = R.style.AppTheme, fragmentArgs = args)


        resultScenario.onFragment { fragment ->
            fragment.viewModel.loadMockData(NetworkState.ERROR, card)
        }


        onView(withId(R.id.cardBrand_textView)).check(matches(withText(card.scheme.toUpperCase())))
        onView(withId(R.id.cardType_textView)).check(matches(withText(card.type.toUpperCase())))
        onView(withId(R.id.bankName_textView)).check(matches(withText(card.bank.name.toUpperCase())))
        onView(withId(R.id.country_textView)).check(matches(withText(card.country.name.toUpperCase())))
    }

}