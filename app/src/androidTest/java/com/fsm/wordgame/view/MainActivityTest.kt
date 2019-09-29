package com.fsm.wordgame.view


import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import com.fsm.wordgame.R
import org.hamcrest.Matchers.not
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(MainActivity::class.java)

    @Test
    fun mainActivityTest_CreatedState() {
        onView(withId(R.id.btn_correct))
            .check(matches(not(isDisplayed())))

        onView(withId(R.id.btn_correct))
            .check(matches(not(isDisplayed())))

        onView(withId(R.id.btn_start))
            .check(matches(isDisplayed()))

        onView(withId(R.id.txt_score_wrong))
            .check(matches(isDisplayed()))
            .check(matches(withText("0")))

        onView(withId(R.id.txt_score_correct))
            .check(matches(isDisplayed()))
            .check(matches(withText("0")))

        onView(withId(R.id.txt_score_missed))
            .check(matches(isDisplayed()))
            .check(matches(withText("0")))
    }

    @Test
    fun mainActivityTest_StartingGame() {
        onView(withId(R.id.btn_start))
            .perform(click())
            .check(matches(not(isDisplayed())))

        onView(withId(R.id.btn_correct))
            .check(matches(isDisplayed()))

        onView(withId(R.id.btn_correct))
            .check(matches(isDisplayed()))

        onView(withId(R.id.txt_score_wrong))
            .check(matches(isDisplayed()))
            .check(matches(withText("0")))

        onView(withId(R.id.txt_score_correct))
            .check(matches(isDisplayed()))
            .check(matches(withText("0")))

        onView(withId(R.id.txt_score_missed))
            .check(matches(isDisplayed()))
            .check(matches(withText("0")))
    }

}
