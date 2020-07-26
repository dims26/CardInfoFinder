package com.dims.cardinfofinder

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.dims.cardinfofinder.screens.MainActivity
import com.dims.cardinfofinder.screens.landing.LandingFragment
import com.dims.cardinfofinder.screens.landing.LandingFragmentDirections
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify


@RunWith(AndroidJUnit4::class)
@LargeTest
class LandingFragmentTest {
    private lateinit var activityScenario : ActivityScenario<MainActivity>

    @Before
    fun launchActivity() {
        activityScenario = ActivityScenario.launch(MainActivity::class.java)
    }

    @Test
    fun test_headlineTextView_shouldBeDisplayed() {
        launchFragmentInContainer<LandingFragment>(themeResId = R.style.AppTheme)
        onView(withId(R.id.headlineTextView)).check(matches(isDisplayed()))
    }

    @Test
    fun test_descriptionTextView_shouldBeDisplayed() {
        launchFragmentInContainer<LandingFragment>(themeResId = R.style.AppTheme)
        onView(withId(R.id.descriptionTextView)).check(matches(isDisplayed()))
    }

    @Test
    fun test_scanButton_shouldBeDisplayed() {
        launchFragmentInContainer<LandingFragment>(themeResId = R.style.AppTheme)
        onView(withId(R.id.scanButton)).check(matches(isDisplayed()))
    }

    @Test
    fun test_inputButton_shouldBeDisplayed() {
        launchFragmentInContainer<LandingFragment>(themeResId = R.style.AppTheme)
        onView(withId(R.id.inputButton)).check(matches(isDisplayed()))
    }

    @Test
    fun test_scanButton_shouldNavigateToScanFragment() {
        // Create a mock NavController
        val mockNavController = mock(NavController::class.java)
        // Create a graphical FragmentScenario for the LandingFragment
        val scanScenario = launchFragmentInContainer<LandingFragment>(themeResId = R.style.AppTheme)
        // Set the NavController property on the fragment
        scanScenario.onFragment { fragment ->
            Navigation.setViewNavController(fragment.requireView(), mockNavController)
        }

        // Verify that performing a click prompts the correct Navigation action
        onView(withId(R.id.scanButton)).perform(click())

        val action =
            LandingFragmentDirections.actionLandingFragmentToScanFragment()
        verify(mockNavController).navigate(action)
    }

    @Test
    fun test_scanButton_shouldNavigateToInputFragment() {
        // Create a mock NavController
        val mockNavController = mock(NavController::class.java)
        // Create a graphical FragmentScenario for the LandingFragment
        val scanScenario = launchFragmentInContainer<LandingFragment>(themeResId = R.style.AppTheme)
        // Set the NavController property on the fragment
        scanScenario.onFragment { fragment ->
            Navigation.setViewNavController(fragment.requireView(), mockNavController)
        }

        // Verify that performing a click prompts the correct Navigation action
        onView(withId(R.id.inputButton)).perform(click())

        val action =
            LandingFragmentDirections.actionLandingFragmentToInputFragment()
        verify(mockNavController).navigate(action)
    }

}