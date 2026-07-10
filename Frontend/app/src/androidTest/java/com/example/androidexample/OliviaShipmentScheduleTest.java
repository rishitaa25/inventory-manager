package com.example.androidexample;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isChecked;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.containsString;

import android.content.Intent;
import android.util.Log;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.androidexample.features.shipments.model.ShipmentObject;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import com.example.androidexample.features.shipments.ui.ShipmentViewActivity;
import com.example.androidexample.features.shipments.ui.shipmentEditActivity;
import com.example.androidexample.features.shipments.ui.shipmentScheduleActivity;
import com.example.androidexample.features.user.data.SessionManager;
import com.example.androidexample.features.user.enums.AccessLevel;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class OliviaShipmentScheduleTest {

    private static final int SIMULATED_DELAY_MS = 4000;

    @Rule
    public ActivityScenarioRule<shipmentScheduleActivity> activityRule =
            new ActivityScenarioRule<>(shipmentScheduleActivity.class);

    @Before
    public void login() throws JSONException, InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);

        JSONObject jsonBody = new JSONObject();
        jsonBody.put("userName", "Admin1");
        jsonBody.put("password", "Password123");

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                "http://coms-3090-013.class.las.iastate.edu:8080/admin/login",
                jsonBody,
                response -> {
                    try {
                        SessionManager.saveSession(
                                response.getString("token"),
                                response.getInt("employeeId"),
                                AccessLevel.valueOf(response.getString("accessLevel")),
                                "Admin1");
                    } catch (JSONException e) {
                        Log.e("LOGIN_PARSE_ERROR", "failed", e);
                    }
                    latch.countDown();
                },
                error -> {
                    Log.e("LOGIN_ERROR", error.toString());
                    latch.countDown();
                }
        );

        Volley.newRequestQueue(ApplicationProvider.getApplicationContext()).add(request);
        latch.await(10, TimeUnit.SECONDS);
    }

    /**
     * Clicks Add Item without filling any fields.
     * Expects a validation error and the schedule button to still be visible.
     */
    @Test
    public void testAddItem_emptyFields_showsValidationError() {
        onView(withId(R.id.addItemButton)).perform(click());

        onView(withId(R.id.shipment_status_msg))
                .check(matches(withText("Complete all fields")));
        onView(withId(R.id.schedule_button))
                .check(matches(isDisplayed()));
    }

    /**
     * Fills all item fields with a valid storage type (DRY).
     * Expects the item to be added and the name field to be cleared afterwards.
     */
    @Test
    public void testAddItem_validItem_clearsFieldAndShowsSuccess() {
        onView(withId(R.id.itemNameInput))
                .perform(typeText("Steel"), closeSoftKeyboard());
        onView(withId(R.id.storageLocationInput))
                .perform(typeText("A"), closeSoftKeyboard());
        onView(withId(R.id.quantityInput))
                .perform(typeText("10"), closeSoftKeyboard());
        onView(withId(R.id.weightInput))
                .perform(typeText("200"), closeSoftKeyboard());
        onView(withId(R.id.storageType))
                .perform(typeText("DRY"), closeSoftKeyboard());

        onView(withId(R.id.addItemButton)).perform(click());

        onView(withId(R.id.shipment_status_msg))
                .check(matches(withText("Item Added")));
        onView(withId(R.id.itemNameInput))
                .check(matches(withText("")));
    }

    /**
     * Enters an invalid storage type (WARM is not accepted).
     * Expects an error message containing "Invalid storage type".
     */
    @Test
    public void testAddItem_invalidStorageType_showsError() {
        onView(withId(R.id.itemNameInput))
                .perform(typeText("TEST"), closeSoftKeyboard());
        onView(withId(R.id.storageLocationInput))
                .perform(typeText("Aisle 3"), closeSoftKeyboard());
        onView(withId(R.id.quantityInput))
                .perform(typeText("5"), closeSoftKeyboard());
        onView(withId(R.id.weightInput))
                .perform(typeText("10"), closeSoftKeyboard());
        onView(withId(R.id.storageType))
                .perform(typeText("WARM"), closeSoftKeyboard());

        onView(withId(R.id.addItemButton)).perform(click());

        onView(withId(R.id.shipment_status_msg))
                .check(matches(withText(containsString("Invalid storage type"))));
    }

    /**
     * Adds a valid item then clicks Schedule once.
     * Expects a confirmation prompt before the shipment is actually submitted.
     * Clicks Schedule a second time to confirm and waits for backend response.
     * Expects a success message containing "created successfully".
     */
    @Test
    public void testAddShipment_path_showsSuccess() throws InterruptedException {
        onView(withId(R.id.itemNameInput))
                .perform(typeText("Test"), closeSoftKeyboard());
        onView(withId(R.id.storageLocationInput))
                .perform(typeText("B"), closeSoftKeyboard());
        onView(withId(R.id.quantityInput))
                .perform(typeText("3"), closeSoftKeyboard());
        onView(withId(R.id.weightInput))
                .perform(typeText("50"), closeSoftKeyboard());
        onView(withId(R.id.storageType))
                .perform(typeText("DRY"), closeSoftKeyboard());

        onView(withId(R.id.addItemButton)).perform(click());

        onView(withId(R.id.shipment_status_msg))
                .check(matches(withText("Item Added")));

        onView(withId(R.id.shipment_admin))
                .perform(typeText("2"), closeSoftKeyboard());
        onView(withId(R.id.shipment_shipmentDate))
                .perform(typeText("12-31-2025"), closeSoftKeyboard());

        onView(withId(R.id.schedule_button)).perform(click());

        onView(withId(R.id.shipment_status_msg))
                .check(matches(withText("Are all items added? Click schedule again to confirm.")));

        onView(withId(R.id.schedule_button)).perform(click());

        Thread.sleep(SIMULATED_DELAY_MS);

        onView(withId(R.id.schedule_title))
                .check(matches(withText("View Shipments")));
    }
}