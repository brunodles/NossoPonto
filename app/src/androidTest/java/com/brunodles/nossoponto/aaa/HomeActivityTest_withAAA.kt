package com.brunodles.nossoponto.aaa

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

    @Test
    fun whenStart_withUser_shouldShowUserName() {
        homeArrange {
            cleanPreferences()
            signInWithUser("Wow")
            activityTestRule.launchActivity(null)
        }
        homeAssert {
            isUsername("Wow")
        }
    }

    @Test
    fun whenStart_withFinisher_shouldShowFinishButton() {
        homeArrange {
            cleanPreferences()
            setFinisher(true)
            activityTestRule.launchActivity(null)
        }
        homeAssert {
            isFinishVisible()
        }
    }

    @Test
    fun whenStart_withoutFinisher_shouldnotShowFinishButton() {
        activityTestRule.launchActivity(null)

        homeAssert {
            isFinishHidden()
        }
    }

}

fun homeArrange(func: HomeArrange.() -> Unit) = HomeArrange().apply(func)
fun homeAct(func: HomeAct.() -> Unit) = HomeAct().apply(func)
fun homeAssert(func: HomeAssert.() -> Unit) = HomeAssert().apply(func)

class HomeArrange() {

    private val preferences by lazy {
        Preferences(InstrumentationRegistry.getTargetContext())
    }

    fun cleanPreferences() {
        preferences.clear()
    }

    fun signInWithUser(username: String) {
        (InstrumentationRegistry.getTargetContext().applicationContext as Application).currentUsername = username
    }

    fun setFinisher(finisher: Boolean) {
        preferences.setFinisher(finisher)
    }
}

class HomeAct() {
    fun finish() {
        onView(withId(R.id.finish)).perform(ViewActions.click())
    }
}

class HomeAssert {
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