package com.example.androidexample;

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


import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.Matchers.not;
import static org.junit.Assert.*;


import static java.lang.Thread.sleep;

import com.example.androidexample.features.items.BarcodeScanner;
import com.example.androidexample.features.user.ui.LoginActivity;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.UUID;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ItemTests {
    /*
    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("com.example.androidexample", appContext.getPackageName());
    }
    */
    String randomName;
    String randomId;

    @Rule
    public ActivityScenarioRule<LoginActivity> activityRule =
            new ActivityScenarioRule<>(LoginActivity.class);
    @Before
    public void login(){
        // Click the spinner to open it
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

        randomName = "Item_" + UUID.randomUUID().toString().substring(0, 8);
        randomId = String.valueOf((int)(Math.random() * 100000));
    }
    @Test
    public void addItem(){
        try {
            sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        onView(withId(R.id.inventory)).perform(click());
        onView(withId(R.id.addItemButton)).perform(click());


        onView(withId(R.id.itemNameBox)).perform(typeText(randomName), closeSoftKeyboard());
        onView(withId(R.id.itemIDBox)).perform(typeText(randomId), closeSoftKeyboard());
        onView(withId(R.id.itemNumberBox)).perform(typeText("100"), closeSoftKeyboard());
        onView(withId(R.id.itemDescriptionBox)).perform(typeText("desc"), closeSoftKeyboard());

        onView(withId(R.id.addItemButton)).perform(click());
        onView(withId(R.id.errorMessageView)).check(matches(withText("true")));


    }

    @Test
    public void searchItem(){

        try {
            sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        onView(withId(R.id.inventory)).perform(click());
        onView(withId(R.id.searchButton)).perform(click());
        onView(withId(R.id.itemNameBox)).perform(typeText(randomName), closeSoftKeyboard());
        onView(withId(R.id.itemIDBox)).perform(typeText(randomId), closeSoftKeyboard());

        onView(withId(R.id.searchItemButton)).perform(click());
        try {
            sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }



    }


    @Test
    public void deleteItem(){
        try {
            sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        onView(withId(R.id.inventory)).perform(click());
        onView(withId(R.id.editOrDeleteItemButton)).perform(click());

        onView(withId(R.id.itemIDBox)).perform(typeText(randomId), closeSoftKeyboard());
        onView(withId(R.id.deleteItemButton)).perform(click());

        try {
            sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        onView(withId(R.id.errorMessageView)).check(matches(withText("true")));

    }


    @Test
    public void displayChangeHist(){
        try {
            sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withId(R.id.inventory)).perform(click());
        onView(withId(R.id.changeHistoryButton)).perform(click());
        try {
            sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        onView(withId(R.id.btnJsonArr)).perform(click());

    }

    @Test
    public void sendPostToZXingTest() throws Exception {

        InputStream is = InstrumentationRegistry.getInstrumentation()
                .getContext().getAssets().open("img.png");
        byte[] imageBytes = is.readAllBytes();
        is.close();


        ActivityScenario<BarcodeScanner> scenario =
                ActivityScenario.launch(BarcodeScanner.class);

        scenario.onActivity(activity -> {
            activity.sendPostToZXing(imageBytes);
        });


        try { sleep(5000); } catch (InterruptedException e) { e.printStackTrace(); }


        onView(withId(R.id.hintView))
                .check(matches(not(withText("sending"))));

        scenario.close();
    }




}