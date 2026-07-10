package com.example.androidexample;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.not;

import android.util.Log;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.androidexample.features.notifications.ui.NotificationActivity;
import com.example.androidexample.features.user.data.SessionManager;
import com.example.androidexample.features.user.enums.AccessLevel;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class OliviaNotificationTests {

    private static final int SIMULATED_DELAY_MS = 4000;

    @Rule
    public ActivityScenarioRule<NotificationActivity> activityRule =
            new ActivityScenarioRule<>(NotificationActivity.class);

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
     * Verifies the notification screen loads and the list view is visible.
     */
    @Test
    public void testNotificationScreen_loads() throws InterruptedException {
        Thread.sleep(SIMULATED_DELAY_MS);

        onView(withId(R.id.shipment_item_list_view))
                .check(matches(isDisplayed()));
    }

    /**
     * Clicks the All filter and verifies the list view is still visible,
     * confirming the all notifications endpoint loads without crashing.
     */
    @Test
    public void testFilter_all_loadsNotifications() throws InterruptedException {
        Thread.sleep(SIMULATED_DELAY_MS);

        onView(withId(R.id.filter_all_icon)).perform(click());

        Thread.sleep(SIMULATED_DELAY_MS);

        onView(withId(R.id.shipment_item_list_view))
                .check(matches(isDisplayed()));
    }

    /**
     * Clicks the Pending filter and verifies the list view is still visible,
     * confirming the pending notifications endpoint loads without crashing.
     */
    @Test
    public void testFilter_pending_loadsNotifications() throws InterruptedException {
        Thread.sleep(SIMULATED_DELAY_MS);

        onView(withId(R.id.filter_pending_icon)).perform(click());

        Thread.sleep(SIMULATED_DELAY_MS);

        onView(withId(R.id.shipment_item_list_view))
                .check(matches(isDisplayed()));
    }

    /**
     * Clicks the Personal filter and verifies the list view is still visible,
     * confirming the personal notifications endpoint loads without crashing.
     */
    @Test
    public void testFilter_personal_loadsNotifications() throws InterruptedException {
        Thread.sleep(SIMULATED_DELAY_MS);

        onView(withId(R.id.filter_personal_icon)).perform(click());

        Thread.sleep(SIMULATED_DELAY_MS);

        onView(withId(R.id.shipment_item_list_view))
                .check(matches(isDisplayed()));
    }

    /**
     * Clicks through all three filters in sequence.
     * Verifies the list remains visible after each switch,
     * confirming no crash occurs when switching between filters.
     */
    @Test
    public void testFilter_switching_doesNotCrash() throws InterruptedException {
        Thread.sleep(SIMULATED_DELAY_MS);

        onView(withId(R.id.filter_pending_icon)).perform(click());
        Thread.sleep(SIMULATED_DELAY_MS);
        onView(withId(R.id.shipment_item_list_view)).check(matches(isDisplayed()));

        onView(withId(R.id.filter_all_icon)).perform(click());
        Thread.sleep(SIMULATED_DELAY_MS);
        onView(withId(R.id.shipment_item_list_view)).check(matches(isDisplayed()));

        onView(withId(R.id.filter_personal_icon)).perform(click());
        Thread.sleep(SIMULATED_DELAY_MS);
        onView(withId(R.id.shipment_item_list_view)).check(matches(isDisplayed()));
    }

    /**
     * Verifies the notification title is displayed on screen.
     */
    @Test
    public void testNotificationTitle_isDisplayed() {
        onView(withId(R.id.notification_title))
                .check(matches(isDisplayed()));
    }

    /**
     * Verifies all three filter icons are displayed in the bottom nav bar.
     */
    @Test
    public void testFilterIcons_areDisplayed() {
        onView(withId(R.id.filter_all_icon)).check(matches(isDisplayed()));
        onView(withId(R.id.filter_pending_icon)).check(matches(isDisplayed()));
        onView(withId(R.id.filter_personal_icon)).check(matches(isDisplayed()));
    }
}