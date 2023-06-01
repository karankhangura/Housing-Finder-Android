package com.example.csci310project2;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class MessageInstrumentedTest {

    @Rule
    public ActivityScenarioRule<MainActivity> activityScenarioRule
            = new ActivityScenarioRule<>(MainActivity.class);


    public static void boilerPlateLogin(String loginEmail, String loginPassword) {
        onView(withId(R.id.button_login_nav)).perform(click());

        onView(withId(R.id.loginEmailInput))
                .perform(typeText(loginEmail), closeSoftKeyboard());
        onView(withId(R.id.loginPasswordInput))
                .perform(typeText(loginPassword), closeSoftKeyboard());
        onView(withId(R.id.button_first)).perform(click());
    }

    @Test
    public void testingViewMessageNone() {
        boilerPlateLogin("testNotAccepted", "testNotAccepted");

        try {
            Thread.sleep(2000);
        } catch (Exception e) {

        }

        onView(withId(R.id.button_messages)).perform(click());
        onView(withId(R.id.messageContentView)).check(matches(withText("You have not been accepted to any housing spots :(")));
    }

    @Test
    public void testingViewMessageAccepted() {
        boilerPlateLogin("testAccepted", "testAccepted");

        try {
            Thread.sleep(2000);
        } catch (Exception e) {

        }

        onView(withId(R.id.button_messages)).perform(click());

        try {
            Thread.sleep(1000);
        } catch (Exception e) {

        }

        onView(withId(R.id.messageContentView)).check(matches(withText("You have been accepted to post Test Post 3 by testNotAccepted!")));
    }
}