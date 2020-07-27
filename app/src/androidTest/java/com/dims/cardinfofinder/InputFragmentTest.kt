package com.dims.cardinfofinder

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.assertion.ViewAssertions.selectedDescendantsMatch
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.dims.cardinfofinder.screens.input.InputFragment
import com.dims.cardinfofinder.screens.input.InputFragmentDirections
import com.google.android.material.textfield.TextInputEditText
import org.hamcrest.Matchers.not
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito

@RunWith(AndroidJUnit4::class)
@LargeTest
class InputFragmentTest {
    private val CORRECT_TEXT = "53998345"
    private val INCORRECT_TEXT = "123456789"

    @Test
    fun test_cardNumberTextField_shouldBeDisplayed(){
        launchFragmentInContainer<InputFragment>(themeResId = R.style.AppTheme)
        onView(withId(R.id.cardNumber_textField))
            .check(matches(isDisplayed()))
            .perform(closeSoftKeyboard())
    }

    @Test
    fun test_cardNumberEditText_shouldBeDisplayed(){
        launchFragmentInContainer<InputFragment>(themeResId = R.style.AppTheme)
        onView(withId(R.id.cardNumber_editText))
            .check(matches(isDisplayed()))
            .perform(closeSoftKeyboard())
    }

    @Test
    fun test_cardNumberTextField_containsEditText(){
        launchFragmentInContainer<InputFragment>(themeResId = R.style.AppTheme)
        onView(withId(R.id.cardNumber_editText))
            .check(selectedDescendantsMatch(isAssignableFrom(TextInputEditText::class.java), withId(R.id.cardNumber_editText)))
            .perform(closeSoftKeyboard())
    }

    @Test
    fun test_submitButton_shouldBeDisplayed(){
        launchFragmentInContainer<InputFragment>(themeResId = R.style.AppTheme)
        onView(withId(R.id.cardNumber_editText)).perform(closeSoftKeyboard())
        onView(withId(R.id.submitButton)).check(matches(isDisplayed()))
    }

    @Test
    fun test_submitButton_isDisabled(){
        launchFragmentInContainer<InputFragment>(themeResId = R.style.AppTheme)
        onView(withId(R.id.cardNumber_editText)).perform(closeSoftKeyboard())
        onView(withId(R.id.submitButton)).check(matches(not(isEnabled())))
    }

    @Test
    fun test_submitButton_isEnabledAfterCorrectTextInput(){
        launchFragmentInContainer<InputFragment>(themeResId = R.style.AppTheme)
        onView(withId(R.id.cardNumber_editText)).perform(typeText(CORRECT_TEXT), closeSoftKeyboard())
        onView(withId(R.id.submitButton)).check(matches(isEnabled()))
    }

    @Test
    fun test_submitButton_isDisabledAfterIncorrectTextInput(){
        launchFragmentInContainer<InputFragment>(themeResId = R.style.AppTheme)
        onView(withId(R.id.cardNumber_editText)).perform(typeText(INCORRECT_TEXT), closeSoftKeyboard())
        onView(withId(R.id.submitButton)).check(matches(not(isEnabled())))
    }

    @Test
    fun test_submitButton_shouldNavigateToResultFragment() {
        // Create a mock NavController
        val mockNavController = Mockito.mock(NavController::class.java)
        // Create a graphical FragmentScenario for the LandingFragment
        val inputScenario = launchFragmentInContainer<InputFragment>(themeResId = R.style.AppTheme)
        // Set the NavController property on the fragment
        inputScenario.onFragment { fragment ->
            Navigation.setViewNavController(fragment.requireView(), mockNavController)
        }
        onView(withId(R.id.cardNumber_editText)).perform(typeText(CORRECT_TEXT), closeSoftKeyboard())

        // Verify that performing a click prompts the correct Navigation action
        onView(withId(R.id.submitButton)).perform(ViewActions.click())

        val action =
            InputFragmentDirections.actionInputFragmentToResultFragment(CORRECT_TEXT.toInt())
        Mockito.verify(mockNavController).navigate(action)
    }
}