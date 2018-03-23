package com.brunodles.nossoponto.withoutrobots

import android.app.Activity
import android.app.Instrumentation
import android.support.annotation.IdRes
import android.support.test.InstrumentationRegistry
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions
import android.support.test.espresso.assertion.ViewAssertions
import android.support.test.espresso.intent.Intents
import android.support.test.espresso.intent.matcher.IntentMatchers
import android.support.test.espresso.intent.rule.IntentsTestRule
import android.support.test.espresso.matcher.ViewMatchers.*
import android.support.test.runner.AndroidJUnit4
import android.widget.EditText
import com.brunodles.nossoponto.HomeActivity
import com.brunodles.nossoponto.LoginActivity
import com.brunodles.nossoponto.Preferences
import com.brunodles.nossoponto.R
import org.hamcrest.core.AllOf.allOf
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LoginActivityTest {

    @JvmField
    @Rule
    val activitTestRule = IntentsTestRule<LoginActivity>(LoginActivity::class.java, true, false)

    val preferences by lazy { Preferences(InstrumentationRegistry.getTargetContext()) }

    @Before
    fun setup() {
        preferences.clear()
    }

    @Test
    fun atFirstStart_shouldBeClean() {
        activitTestRule.launchActivity(null)
        onEditTextInsideTextInputLayout(R.id.username).check(ViewAssertions.matches(withText("")))
        onEditTextInsideTextInputLayout(R.id.password).check(ViewAssertions.matches(withText("")))
    }

    @Test
    fun whenInputInvalidUser_shouldShowInvalidUserMessage() {
        activitTestRule.launchActivity(null)
        onView(withText("SignIn")).perform(ViewActions.click())
        onView(withText("Invalid username")).check(ViewAssertions.matches(isDisplayed()))
    }

    @Test
    fun whenLogin_usingButton_shouldSendToHomeActivity() {
        // Act
        activitTestRule.launchActivity(null)
        Intents.intending(IntentMatchers.hasComponent(HomeActivity::class.java.name))
                .respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, null))

        // Act
        onEditTextInsideTextInputLayout(R.id.username).perform(
                ViewActions.typeText("brunodles"),
                ViewActions.pressImeActionButton())
        onEditTextInsideTextInputLayout(R.id.password).perform(
                ViewActions.typeText("mysecret"),
                ViewActions.closeSoftKeyboard())
        onView(withId(R.id.login)).perform(ViewActions.click())

        // Assert
        Intents.intended(IntentMatchers.hasComponent(HomeActivity::class.java.name))
    }

    @Test
    fun whenLogin_usingKeyboard_shouldSendToHomeActivity() {
        activitTestRule.launchActivity(null)
        Intents.intending(IntentMatchers.hasComponent(HomeActivity::class.java.name))
                .respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, null))

        onEditTextInsideTextInputLayout(R.id.username).perform(
                ViewActions.typeText("brunodles"),
                ViewActions.pressImeActionButton()
        )
        onEditTextInsideTextInputLayout(R.id.password).perform(
                ViewActions.typeText("mysecret"),
                ViewActions.pressImeActionButton()
        )

        Intents.intended(IntentMatchers.hasComponent(HomeActivity::class.java.name))
    }

    @Test
    fun whenStartAgain_shouldShowThePreviousUsername() {
        preferences.setPreviousUsername("dlimaun")

        activitTestRule.launchActivity(null)

        onEditTextInsideTextInputLayout(R.id.username).check(ViewAssertions.matches(withText("dlimaun")))
        onEditTextInsideTextInputLayout(R.id.password).check(ViewAssertions.matches(withText("")))
    }

    private fun onEditTextInsideTextInputLayout(@IdRes textInputLayoutId: Int) =
            onView(allOf(isDescendantOfA(withId(textInputLayoutId)), isAssignableFrom(EditText::class.java)))

}

