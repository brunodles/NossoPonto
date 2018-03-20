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
class LoginActivityTest_withRobots {

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
        loginResult {
            isUsernameEmpty()
            isPasswordEmpty()
        }
    }

    @Test
    fun whenInputInvalidUser_shouldShowInvalidUserMessage() {
        activitTestRule.launchActivity(null)
        login {
        } signIn {
            isInvalidUserMessageVisible()
        }
    }

    @Test
    fun whenLogin_usingButton_shouldSendToHomeActivity() {
        activitTestRule.launchActivity(null)
        Intents.intending(IntentMatchers.hasComponent(HomeActivity::class.java.name))
                .respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, null))

        login {
            username("brunodles")
            keyboardAction()
            password("mysecret")
            closeSoftKeyboard()
        } signIn {
            homeActivityWasCalled()
        }
    }

    @Test
    fun whenLogin_usingKeyboard_shouldSendToHomeActivity() {
        activitTestRule.launchActivity(null)
        Intents.intending(IntentMatchers.hasComponent(HomeActivity::class.java.name))
                .respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, null))

        login {
            username("brunodles")
            keyboardAction()
            password("senha")
            keyboardAction()
        }
        loginResult {
            homeActivityWasCalled()
        }
    }

    @Test
    fun whenStartAgain_shouldShowThePreviousUsername() {
        preferences.setPreviousUsername("dlimaun")
        activitTestRule.launchActivity(null)

        loginResult {
            isUsername("dlimaun")
            isPasswordEmpty()
        }
    }

}

fun login(func: LoginRobot.() -> Unit) = LoginRobot().apply { func() }
fun loginResult(func: LoginResult.() -> Unit) = LoginResult().apply { func() }

class LoginRobot {

    var lastEditText: Int? = null

    fun username(username: String) {
        inputTextInto(username, R.id.username)
    }

    fun password(password: String) {
        inputTextInto(password, R.id.password)
    }

    private fun inputTextInto(text: String, @IdRes id: Int) {
        lastEditText = id
        onEditTextInsideTextInputLayout(id).perform(
                ViewActions.typeText(text))
    }

    fun keyboardAction() {
        lastEditText?.let {
            onEditTextInsideTextInputLayout(it)
                    .perform(ViewActions.pressImeActionButton())
        }
    }

    fun closeSoftKeyboard() {
        lastEditText?.let {
            onEditTextInsideTextInputLayout(it)
                    .perform(ViewActions.closeSoftKeyboard())
        }
    }

    infix fun signIn(func: LoginResult.() -> Unit): LoginResult {
        onView(withText("SignIn")).perform(ViewActions.click())
        return loginResult(func)
    }
}

class LoginResult {

    fun isUsername(username: String) {
        onEditTextInsideTextInputLayout(R.id.username).check(ViewAssertions.matches(withText(username)))
    }

    fun isUsernameEmpty() {
        onEditTextInsideTextInputLayout(R.id.username).check(ViewAssertions.matches(withText("")))
    }

    fun isPasswordEmpty() {
        onEditTextInsideTextInputLayout(R.id.password).check(ViewAssertions.matches(withText("")))
    }

    fun isInvalidUserMessageVisible() {
        onView(withText("Invalid username")).check(ViewAssertions.matches(isDisplayed()))
    }

    fun homeActivityWasCalled() {
        Intents.intended(IntentMatchers.hasComponent(HomeActivity::class.java.name))
    }
}

private fun onEditTextInsideTextInputLayout(@IdRes textInputLayoutId: Int) =
        onView(allOf(isDescendantOfA(withId(textInputLayoutId)), isAssignableFrom(EditText::class.java)))