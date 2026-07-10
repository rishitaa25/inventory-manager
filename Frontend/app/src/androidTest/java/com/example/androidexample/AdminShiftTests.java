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
public class AdminShiftTests {

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
        onView(withId(R.id.shifts)).perform(click());
        try {
            sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withId(R.id.shiftManagementButton)).perform(click());
        try {
            sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void createShift() {


        // Grab the live ManagerShiftView instance and call its public methods on the UI thread
        ActivityScenario<ManagerShiftView> scenario =
                ActivityScenario.launch(ManagerShiftView.class);

        scenario.onActivity(activity -> {
            try {
                JSONObject shiftJson = activity.createShiftJson(
                        Integer.parseInt("37"),
                        "09:00:00",
                        "17:00:00",
                        "2025-06-15"
                );
                activity.sendPostRequestToDB(shiftJson);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        // Give Volley time to fire the request and update the error message box
        try { sleep(2000); } catch (InterruptedException e) { e.printStackTrace(); }

        onView(withId(R.id.errorMessageView2)).check(matches(isDisplayed()));

        scenario.close();
    }
    @Test
    public void deleteShift() {


        // Grab the live ManagerShiftView instance and call its public methods on the UI thread
        ActivityScenario<ManagerShiftView> scenario =
                ActivityScenario.launch(ManagerShiftView.class);

        scenario.onActivity(activity -> {
            try {

                activity.sendDeleteRequestToDB("37","2025-06-15");
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        // Give Volley time to fire the request and update the error message box
        try { sleep(2000); } catch (InterruptedException e) { e.printStackTrace(); }

        onView(withId(R.id.errorMessageView2)).check(matches(isDisplayed()));

        scenario.close();
    }

    @Test
    public void putShift() {


        // Grab the live ManagerShiftView instance and call its public methods on the UI thread
        ActivityScenario<ManagerShiftView> scenario =
                ActivityScenario.launch(ManagerShiftView.class);

        scenario.onActivity(activity -> {
            try {
                JSONObject shiftJson = activity.createShiftJson(
                        Integer.parseInt("37"),
                        "09:00:00",
                        "17:00:00",
                        "2025-06-15"
                );
                activity.sendPostRequestToDB(shiftJson);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        // Give Volley time to fire the request and update the error message box
        try { sleep(2000); } catch (InterruptedException e) { e.printStackTrace(); }

        onView(withId(R.id.errorMessageView2)).check(matches(isDisplayed()));

        scenario.close();
    }
    @Test
    public void selectStartAndEndTimeButton() {
       selectPopup(R.id.selectStartTimeButton);
       selectPopup(R.id.selectEndtimeButton);

    }

    private void selectPopup(int id){
        onView(withId(id)).perform(click());
        try {
            sleep(800);
        } catch (InterruptedException e) { e.printStackTrace();
        }

        onView(withText("Pick a time"))
                .inRoot(isDialog())
                .check(matches(isDisplayed()));

        onView(withText("Cancel"))
                .inRoot(isDialog())
                .perform(click());
        try { sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        onView(withId(id)).check(matches(isDisplayed()));
    }
}