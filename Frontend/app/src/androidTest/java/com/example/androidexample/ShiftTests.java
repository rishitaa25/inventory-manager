package com.example.androidexample;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Before;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;

import android.content.Context;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;


import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;


import static java.lang.Thread.sleep;

import com.example.androidexample.features.user.ui.LoginActivity;

@RunWith(AndroidJUnit4.class)
public class ShiftTests {

    @Rule
    public ActivityScenarioRule<LoginActivity> activityRule =
            new ActivityScenarioRule<>(LoginActivity.class);

    @Before
    public void login() {
        onView(withId(R.id.login_account_type_spinner)).perform(click());
        try {
            sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        onData(allOf(is(instanceOf(String.class)), is("Employee")))
                .perform(click());

        onView(withId(R.id.login_username)).perform(typeText("n"), closeSoftKeyboard());
        onView(withId(R.id.login_password)).perform(typeText("n"), closeSoftKeyboard());
        try {
            sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        onView(withId(R.id.login_button)).perform(click());
        try {
            sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void shiftNavViewButtonNavigatesToViewShiftSchedule() {
        try {
            sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        onView(withId(R.id.shifts)).perform(click());
        try {
            sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        onView(withId(R.id.shiftViewButton)).perform(click());
        try {
            sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withId(R.id.forwardWeekButton)).perform(click());
        onView(withId(R.id.backwardWeekButton)).perform(click());

        onView(withId(R.id.forwardWeekButton)).check(matches(isDisplayed()));
    }
}
