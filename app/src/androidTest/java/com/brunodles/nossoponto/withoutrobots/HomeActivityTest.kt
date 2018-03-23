package com.brunodles.nossoponto.withoutrobots

import android.support.test.InstrumentationRegistry
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.assertion.ViewAssertions
import android.support.test.espresso.matcher.ViewMatchers
import android.support.test.espresso.matcher.ViewMatchers.*
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import com.brunodles.nossoponto.Application
import com.brunodles.nossoponto.HomeActivity
import com.brunodles.nossoponto.Preferences
import com.brunodles.nossoponto.R
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class HomeActivityTest {

    @JvmField
    @Rule
    val activityTestRule = ActivityTestRule<HomeActivity>(HomeActivity::class.java, true, false)

    val preferences by lazy {
        Preferences(InstrumentationRegistry.getTargetContext())
    }

    @Before
    fun clean() {
        preferences.clear()
    }

    @Test
    fun whenStart_withUser_shouldShowUserName() {
        (InstrumentationRegistry.getTargetContext().applicationContext as Application).currentUsername = "Wow"
        activityTestRule.launchActivity(null)

        onView(ViewMatchers.withText("Wow")).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun whenStart_withFinisher_shouldShowFinishButton() {
        preferences.setFinisher(true)

        activityTestRule.launchActivity(null)

        onView(withId(R.id.finish)).check(ViewAssertions.matches(isCompletelyDisplayed()))
    }

    @Test
    fun whenStart_withoutFinisher_shouldnotShowFinishButton() {
        activityTestRule.launchActivity(null)

        onView(withId(R.id.finish)).check(ViewAssertions.matches(withEffectiveVisibility(Visibility.GONE)))
    }

}