package com.example.csci310project2;

import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;


import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class LoginInstrumentedTest {

    @Rule
    public ActivityScenarioRule<MainActivity> activityScenarioRule
            = new ActivityScenarioRule<>(MainActivity.class);


    public static void boilerPlateLogin() {
        onView(withId(R.id.button_login_nav)).perform(click());

        onView(withId(R.id.loginEmailInput))
                .perform(typeText("bob27"), closeSoftKeyboard());
        onView(withId(R.id.loginPasswordInput))
                .perform(typeText("bob27"), closeSoftKeyboard());
        onView(withId(R.id.button_first)).perform(click());
    }

    @Test
    public void testingLoginSuccessful() {
        onView(withId(R.id.button_login_nav)).perform(click());

        onView(withId(R.id.loginEmailInput))
                .perform(typeText("bob27"), closeSoftKeyboard());
        onView(withId(R.id.loginPasswordInput))
                .perform(typeText("bob27"), closeSoftKeyboard());
        onView(withId(R.id.button_first)).perform(click());

        onView(withId(R.id.textview_second)).check(matches(withText("Welcome, bob27_3")));
    }

    @Test
    public void testingLoginUnsuccessful() {
        onView(withId(R.id.button_login_nav)).perform(click());

        onView(withId(R.id.loginEmailInput))
                .perform(typeText("bob27"), closeSoftKeyboard());
        onView(withId(R.id.loginPasswordInput))
                .perform(typeText("bob26"), closeSoftKeyboard());
        onView(withId(R.id.button_first)).perform(click());

        onView(withId(R.id.loginErrorTextView)).check(matches(withText("Failed to login. Email or password is incorrect")));
    }

}