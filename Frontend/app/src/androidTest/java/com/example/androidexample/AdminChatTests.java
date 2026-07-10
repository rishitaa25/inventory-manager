package com.example.androidexample;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.matcher.RootMatchers.isDialog;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.json.JSONObject;
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

import com.example.androidexample.features.shifts.ManagerShiftView;
import com.example.androidexample.features.user.ui.LoginActivity;

@RunWith(AndroidJUnit4.class)
public class AdminChatTests {

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

        onData(allOf(is(instanceOf(String.class)), is("Admin")))
                .perform(click());

        onView(withId(R.id.login_username)).perform(typeText("Admin1"), closeSoftKeyboard());
        onView(withId(R.id.login_password)).perform(typeText("Password123"), closeSoftKeyboard());
        try {
            sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        onView(withId(R.id.login_button)).perform(click());
        try {
            sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withId(R.id.chat)).perform(click());
        try {
            sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withId(R.id.createChatButton)).perform(click());
        try {
            sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    @Test
    public void createRoom(){

        onView(withId(R.id.roomNameBox)).perform(typeText("Room1"), closeSoftKeyboard());
        try {
            sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withId(R.id.createRoomButton)).perform(click());

        onView(withId(R.id.errorMessageView)).check(matches(isDisplayed()));



    }
    @Test
    public void addUserTest(){

        onView(withId(R.id.groupIDBox)).perform(typeText("1"), closeSoftKeyboard());
        onView(withId(R.id.employeeIdBox)).perform(typeText("1"), closeSoftKeyboard());
        try {
            sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withId(R.id.addEmployeeButton)).perform(click());

        onView(withId(R.id.errorMessageView)).check(matches(isDisplayed()));



    }

}