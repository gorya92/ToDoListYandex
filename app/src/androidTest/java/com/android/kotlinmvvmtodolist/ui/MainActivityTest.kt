package com.android.kotlinmvvmtodolist.ui


import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.filters.LargeTest
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.android.kotlinmvvmtodolist.R
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher
import org.hamcrest.core.AllOf.allOf
import org.hamcrest.core.Is.`is`
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4ClassRunner::class)
class MainActivityTest {

    @Rule
    @JvmField
    var mActivityScenarioRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun CheckVisibility() {
        onView(withId(R.id.coordinatorTask)).check(matches(isDisplayed()))
    }

    @Test
    fun checkingTextVisibility() {
        onView(withId(R.id.mydeal))
            .check(matches(isDisplayed()))

        onView(withId(R.id.fab))
            .check(matches(isDisplayed()))
    }

    @Test
    fun checkTextIsMainActivity() {
        onView(withId(R.id.mydeal))
            .check(
                matches(
                    withText(
                        R.string.my_deal
                    )
                )
            )
    }

    @Test
    fun navigateToAddTaskFragment() {
        onView(withId(R.id.fab)).perform(
            click()
        )

        onView(withId(R.id.coordinatorTodo))
            .check(matches(isDisplayed()))
        onView(withId(R.id.dealtext)).perform(
            replaceText("aboba")
        )
        onView(withId(R.id.save))
            .perform(click())

        val recyclerView = onView(
            allOf(
                withId(R.id.recycler_view),
                childAtPosition(
                    withClassName(`is`("androidx.cardview.widget.CardView")),
                    0
                )
            )
        )
        recyclerView.perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                0,
                click()
            )
        )
        onView(withId(R.id.deletetext))
            .perform(click())
    }

    private fun childAtPosition(
        parentMatcher: Matcher<View>, position: Int
    ): Matcher<View> {

        return object : TypeSafeMatcher<View>() {
            override fun describeTo(description: Description) {
                description.appendText("Child at position $position in parent ")
                parentMatcher.describeTo(description)
            }

            public override fun matchesSafely(view: View): Boolean {
                val parent = view.parent
                return parent is ViewGroup && parentMatcher.matches(parent)
                        && view == parent.getChildAt(position)
            }
        }
    }

}
