package com.brunodles.nossoponto.withoutrobots

import android.support.test.InstrumentationRegistry
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions
import android.support.test.espresso.assertion.ViewAssertions
import android.support.test.espresso.matcher.ViewMatchers.*
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import com.brunodles.nossoponto.Application
import com.brunodles.nossoponto.HomeActivity
import com.brunodles.nossoponto.Preferences
import com.brunodles.nossoponto.R
import org.hamcrest.core.AllOf
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class HomeActivityTest_withRobots {

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

        homeResult {
            isUsername("Wow")
        }
    }

    @Test
    fun whenStart_withFinisher_shouldShowFinishButton() {
        preferences.setFinisher(true)
        activityTestRule.launchActivity(null)

        homeResult {
            isFinishVisible()
        }
    }

    @Test
    fun whenStart_withoutFinisher_shouldnotShowFinishButton() {
        activityTestRule.launchActivity(null)

        homeResult {
            isFinishHidden()
        }
    }

}

fun home(func: HomeRobot.() -> Unit) = HomeRobot().apply { func() }
fun homeResult(func: HomeResult.() -> Unit) = HomeResult().apply { func() }

class HomeRobot() {
    fun finish() {
        onView(withId(R.id.finish)).perform(ViewActions.click())
    }
}

class HomeResult {
    fun isUsername(username: String) {
        onView(withId(R.id.username)).check(ViewAssertions.matches(AllOf.allOf(isDisplayed(), withText(username))))
    }

    fun isFinishVisible() {
        onView(withId(R.id.finish)).check(ViewAssertions.matches(isCompletelyDisplayed()))
    }

    fun isFinishHidden() {
        onView(withId(R.id.finish)).check(ViewAssertions.matches(withEffectiveVisibility(Visibility.GONE)))
    }
}