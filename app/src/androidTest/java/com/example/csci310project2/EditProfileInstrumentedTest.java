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

import androidx.annotation.NonNull;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class EditProfileInstrumentedTest {

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
    public void testingEditFirstName() {
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

        onView(withId(R.id.button_profile)).perform(click());

        onView(withId(R.id.editProfileInputFirstName))
                .perform(typeText("Editedname"), closeSoftKeyboard());

        onView(withId(R.id.button_first)).perform(click());

        try {
            // allows for time to query user when validating user registration information
            Thread.sleep(5000);
        } catch (Exception e) {

        }

        FirebaseDatabase root = FirebaseDatabase.getInstance();
        DatabaseReference ref = root.getReference();

        DatabaseReference userRef = ref.child("user").child(email);
        userRef.getRef().removeValue();

        onView(withId(R.id.textview_second)).check(matches(withText("Welcome, AbcdEditedname")));

    }

    @Test
    public void testingEditDescription() {
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

        onView(withId(R.id.button_profile)).perform(click());

        onView(withId(R.id.editProfileDescription))
                .perform(typeText("NewDescription"), closeSoftKeyboard());

        onView(withId(R.id.button_first)).perform(click());;

        try {
            // allows for time to query user when validating user registration information
            Thread.sleep(5000);
        } catch (Exception e) {

        }

        onView(withId(R.id.button_profile)).perform(click());

        FirebaseDatabase root = FirebaseDatabase.getInstance();
        DatabaseReference ref = root.getReference();

        DatabaseReference userRef = ref.child("user").child(email);
        userRef.getRef().removeValue();

        onView(withId(R.id.editProfileDescription)).check(matches(withText("NewDescription")));
    }

    @Test
    public void testingEditPassword() {
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

        onView(withId(R.id.button_profile)).perform(click());

        onView(withId(R.id.editProfileInputTextPass))
                .perform(typeText("NewPassword"), closeSoftKeyboard());

        onView(withId(R.id.button_first)).perform(click());;

        try {
            // allows for time to query user when validating user registration information
            Thread.sleep(5000);
        } catch (Exception e) {

        }

        FirebaseDatabase root = FirebaseDatabase.getInstance();
        DatabaseReference ref = root.getReference();

        DatabaseReference userRef = ref.child("user").child(email);

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String actual = "";
                actual = snapshot.child("password").getValue(String.class);

                DatabaseReference userRefNew = ref.child("user").child(email);
                userRefNew.getRef().removeValue();

                assertEquals("NewPassword", actual);

                try {
                    // allows for time to query user when validating user registration information
                    Thread.sleep(1000);
                } catch (Exception e) {

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}