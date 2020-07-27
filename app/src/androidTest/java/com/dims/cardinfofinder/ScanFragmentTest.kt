package com.dims.cardinfofinder

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.rule.GrantPermissionRule
import com.dims.cardinfofinder.screens.input.InputFragment
import com.dims.cardinfofinder.screens.scan.ScanFragment
import com.dims.cardinfofinder.screens.scan.ScanFragmentDirections
import org.hamcrest.Matchers.not
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito

@RunWith(AndroidJUnit4::class)
@LargeTest
class ScanFragmentTest {

    private val CORRECT_TEXT = "53998345"
    private val INCORRECT_TEXT = "123456789"

    @get:Rule
    val mRuntimePermissionRule: GrantPermissionRule = GrantPermissionRule.grant(android.Manifest.permission.CAMERA)

    @Test
    fun test_surfaceView_shouldBeDisplayed(){
        launchFragmentInContainer<ScanFragment>(themeResId = R.style.AppTheme)
        onView(withId(R.id.surfaceView)).check(matches(isDisplayed()))
    }

    @Test
    fun test_directionsTextView_shouldBeDisplayed(){
        launchFragmentInContainer<ScanFragment>(themeResId = R.style.AppTheme)
        onView(withId(R.id.directionsTextView)).check(matches(isDisplayed()))
    }

    @Test
    fun test_detectedTextView_shouldBeDisplayed(){
        launchFragmentInContainer<ScanFragment>(themeResId = R.style.AppTheme)
        onView(withId(R.id.detectedTextView)).check(matches(isDisplayed()))
    }

    @Test
    fun test_proceedButton_shouldBeDisplayed(){
        launchFragmentInContainer<ScanFragment>(themeResId = R.style.AppTheme)
        onView(withId(R.id.proceedButton)).check(matches(isDisplayed()))
    }

    @Test
    fun test_proceedButton_isDisabled(){
        launchFragmentInContainer<InputFragment>(themeResId = R.style.AppTheme)
        onView(withId(R.id.submitButton)).check(matches(not(isEnabled())))
    }

    @Test
    fun test_directionsTextView_shouldContainText(){
        launchFragmentInContainer<ScanFragment>(themeResId = R.style.AppTheme)
        onView(withId(R.id.directionsTextView)).check(matches(withText(R.string.scan_directions_text)))
    }

    @Test
    fun test_proceedButton_isEnabledAfterCorrectInput() {
        // Create a mock NavController
        val mockNavController = Mockito.mock(NavController::class.java)
        // Create a graphical FragmentScenario for the LandingFragment
        val scanScenario = launchFragmentInContainer<ScanFragment>(themeResId = R.style.AppTheme)
        // Set the NavController property on the fragment
        scanScenario.onFragment { fragment ->
            Navigation.setViewNavController(fragment.requireView(), mockNavController)
            //send value to viewModel
            fragment.viewModel.check(CORRECT_TEXT)
        }

        // Verify that performing a click prompts the correct Navigation action
        onView(withId(R.id.proceedButton)).check(matches(isEnabled()))
    }

    @Test
    fun test_proceedButton_isDisabledAfterIncorrectInput() {
        // Create a mock NavController
        val mockNavController = Mockito.mock(NavController::class.java)
        // Create a graphical FragmentScenario for the LandingFragment
        val scanScenario = launchFragmentInContainer<ScanFragment>(themeResId = R.style.AppTheme)
        // Set the NavController property on the fragment
        scanScenario.onFragment { fragment ->
            Navigation.setViewNavController(fragment.requireView(), mockNavController)
            //send value to viewModel
            fragment.viewModel.check(INCORRECT_TEXT)
        }

        // Verify that performing a click prompts the correct Navigation action
        onView(withId(R.id.proceedButton)).check(matches(not(isEnabled())))
    }


    @Test
    fun test_proceedButton_shouldNavigateToResultFragment() {
        // Create a mock NavController
        val mockNavController = Mockito.mock(NavController::class.java)
        // Create a graphical FragmentScenario for the LandingFragment
        val scanScenario = launchFragmentInContainer<ScanFragment>(themeResId = R.style.AppTheme)
        // Set the NavController property on the fragment
        scanScenario.onFragment { fragment ->
            Navigation.setViewNavController(fragment.requireView(), mockNavController)
            //send value to viewModel
            fragment.viewModel.check(CORRECT_TEXT)
        }

        // Verify that performing a click prompts the correct Navigation action
        onView(withId(R.id.proceedButton)).perform(ViewActions.click())

        val action =
            ScanFragmentDirections.actionScanFragmentToResultFragment(CORRECT_TEXT.toInt())
        Mockito.verify(mockNavController).navigate(action)
    }
}