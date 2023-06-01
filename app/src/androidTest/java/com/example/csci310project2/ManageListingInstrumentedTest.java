package com.example.csci310project2;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static com.example.csci310project2.Utility.atPosition;

import android.view.View;

//import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ManageListingInstrumentedTest {

    @Rule
    public ActivityScenarioRule<MainActivity> activityScenarioRule
            = new ActivityScenarioRule<>(MainActivity.class);


    public static void boilerPlateLogin() {
        onView(withId(R.id.button_login_nav)).perform(click());

        onView(withId(R.id.loginEmailInput))
                .perform(typeText("test"), closeSoftKeyboard());
        onView(withId(R.id.loginPasswordInput))
                .perform(typeText("test"), closeSoftKeyboard());
        onView(withId(R.id.button_first)).perform(click());
    }

    @Test
    public void testingManageListingHousingList() {
        boilerPlateLogin();


        String answer = "";
        answer += "address: " + 1;
        answer += ", rent: " + 1;
        answer += ", location: " + 1;

        answer += ", bed: " + 1;
        answer += ", utilities: " + 1;
        answer += ", schedule: " + 2;

        answer += ", academicFocus: " + 1;
        answer += ", personality: " + 1;
        answer += "";

        onView(withId(R.id.button_manage)).perform(click());

        try {
            Thread.sleep(5000);
        } catch (Exception e) {

        }

        onView(withId(R.id.recyclerView))
                .check(matches(atPosition(0, hasDescendant(withText(answer)))));
    }

}