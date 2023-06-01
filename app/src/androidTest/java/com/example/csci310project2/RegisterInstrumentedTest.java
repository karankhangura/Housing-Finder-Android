package com.example.csci310project2;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.assertEquals;

import android.content.Context;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

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
public class RegisterInstrumentedTest {

    @Rule
    public ActivityScenarioRule<MainActivity> activityScenarioRule
            = new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("com.example.csci310project2", appContext.getPackageName());
    }

    @Test
    public void testingRegisterSuccess() {
        String email = "Aa3i";

        onView(withId(R.id.firstName))
                .perform(typeText("Abcd"), closeSoftKeyboard());
        onView(withId(R.id.lastName))
                .perform(typeText("Efgh"), closeSoftKeyboard());
        onView(withId(R.id.inputText))
                .perform(typeText(email), closeSoftKeyboard());
        onView(withId(R.id.inputTextPass))
                .perform(typeText("a"), closeSoftKeyboard());

        onView(withId(R.id.button_register)).perform(click());

        try {
            // allows for time to query user when validating user registration information
            Thread.sleep(5000);
        } catch (Exception e) {

        }

        FirebaseDatabase root = FirebaseDatabase.getInstance();
        DatabaseReference ref = root.getReference();

        DatabaseReference userRef = ref.child("user").child(email);
        userRef.getRef().removeValue();

        onView(withId(R.id.textview_second)).check(matches(withText("Welcome, Abcd")));

    }

    @Test
    public void testingRegisterFailureDuplicateEmail() {
        onView(withId(R.id.firstName))
                .perform(typeText("Abcd"), closeSoftKeyboard());
        onView(withId(R.id.lastName))
                .perform(typeText("Efgh"), closeSoftKeyboard());
        onView(withId(R.id.inputText))
                .perform(typeText("bob27"), closeSoftKeyboard());
        onView(withId(R.id.inputTextPass))
                .perform(typeText("a"), closeSoftKeyboard());

        onView(withId(R.id.button_register)).perform(click());

        try {
            Thread.sleep(5000);
        } catch (Exception e) {

        }
        onView(withId(R.id.registerErrorTextView)).check(matches(withText("Failed to register. Email is already registered.")));
    }

    @Test
    public void testingRegisterFailureNoEmail() {
        onView(withId(R.id.firstName))
                .perform(typeText("Abcd"), closeSoftKeyboard());
        onView(withId(R.id.lastName))
                .perform(typeText("Efgh"), closeSoftKeyboard());
        onView(withId(R.id.inputTextPass))
                .perform(typeText("a"), closeSoftKeyboard());

        onView(withId(R.id.button_register)).perform(click());

        try {
            Thread.sleep(5000);
        } catch (Exception e) {

        }
        onView(withId(R.id.registerErrorTextView)).check(matches(withText("Failed to register. Email is empty.")));
    }

    @Test
    public void testingRegisterFailureNoPassword() {
        onView(withId(R.id.firstName))
                .perform(typeText("Abcd"), closeSoftKeyboard());
        onView(withId(R.id.lastName))
                .perform(typeText("Efgh"), closeSoftKeyboard());
        onView(withId(R.id.inputText))
                .perform(typeText("testingusernopass"), closeSoftKeyboard());

        onView(withId(R.id.button_register)).perform(click());

        try {
            Thread.sleep(5000);
        } catch (Exception e) {

        }
        onView(withId(R.id.registerErrorTextView)).check(matches(withText("Failed to register. Password is empty.")));
    }

    @Test
    public void testingRegisterFailureAllBlank() {
        onView(withId(R.id.button_register)).perform(click());

        try {
            Thread.sleep(5000);
        } catch (Exception e) {

        }
        onView(withId(R.id.registerErrorTextView)).check(matches(withText("Failed to register. Enter information.")));
    }

    @Test
    public void testingRegisterFailureSpecialCharacters() {
        onView(withId(R.id.firstName))
                .perform(typeText("a"), closeSoftKeyboard());
        onView(withId(R.id.lastName))
                .perform(typeText("a"), closeSoftKeyboard());
        onView(withId(R.id.inputText))
                .perform(typeText("`~!@#$%^&*()_+-={}|[]\\:\";'<>?,./"), closeSoftKeyboard());
        onView(withId(R.id.inputTextPass))
                .perform(typeText("a"), closeSoftKeyboard());

        onView(withId(R.id.button_register)).perform(click());

        try {
            Thread.sleep(2000);
        } catch (Exception e) {

        }

        onView(withId(R.id.registerErrorTextView)).check(matches(withText("Failed to register. Email contains invalid characters")));
    }
}