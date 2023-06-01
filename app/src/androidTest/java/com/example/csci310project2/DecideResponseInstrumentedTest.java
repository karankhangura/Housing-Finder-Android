package com.example.csci310project2;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import static org.junit.Assert.assertEquals;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class DecideResponseInstrumentedTest {

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
    public void testingDecideResponseAccept() {
        // add info to database to simulate accepting
        FirebaseDatabase root = FirebaseDatabase.getInstance();
        DatabaseReference ref = root.getReference();

        ref = ref.child("post").child("test2").child("Test Post 2");

        ref.child("accepted").child("test3").setValue("test3");

        boilerPlateLogin("test2", "test2");

        try {
            Thread.sleep(2000);
        } catch (Exception e) {

        }

        onView(withId(R.id.button_manage)).perform(click());

        try {
            Thread.sleep(1000);
        } catch (Exception e) {

        }

        onView(withId(R.id.recyclerView))
                .perform(actionOnItemAtPosition(0, click()));

        try {
            Thread.sleep(1000);
        } catch (Exception e) {

        }

        onView(withId(R.id.viewResponsesRecyclerView))
                .perform(actionOnItemAtPosition(0, click()));

        onView(withId(R.id.acceptButton)).perform(click());

        try {
            Thread.sleep(1000);
        } catch (Exception e) {

        }

        // check if in database correctly
        ref = root.getReference();

        ref = ref.child("post").child("test2").child("Test Post 2");

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean acceptedExists = snapshot.child("chosenUsers").child("test3").exists();

                if (acceptedExists) {
                    snapshot.child("chosenUsers").getRef().removeValue();
                }
                snapshot.child("accepted").getRef().removeValue();
                assertEquals(true, acceptedExists);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Test
    public void testingDecideResponseDecline() {
        // add info to database to simulate accepting
        FirebaseDatabase root = FirebaseDatabase.getInstance();
        DatabaseReference ref = root.getReference();

        ref = ref.child("post").child("test2").child("Test Post 2");

        ref.child("accepted").child("test3").setValue("test3");

        boilerPlateLogin("test2", "test2");

        try {
            Thread.sleep(2000);
        } catch (Exception e) {

        }

        onView(withId(R.id.button_manage)).perform(click());

        try {
            Thread.sleep(1000);
        } catch (Exception e) {

        }

        onView(withId(R.id.recyclerView))
                .perform(actionOnItemAtPosition(0, click()));

        try {
            Thread.sleep(1000);
        } catch (Exception e) {

        }

        onView(withId(R.id.viewResponsesRecyclerView))
                .perform(actionOnItemAtPosition(0, click()));

        onView(withId(R.id.rejectButton)).perform(click());

        try {
            Thread.sleep(1000);
        } catch (Exception e) {

        }

        // check if in database correctly
        ref = root.getReference();

        ref = ref.child("post").child("test2").child("Test Post 2");

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean acceptedExists = snapshot.child("chosenUsers").child("test3").exists();

                if (acceptedExists) {
                    snapshot.child("chosenUsers").getRef().removeValue();
                }
                snapshot.child("accepted").getRef().removeValue();
                assertEquals(false, acceptedExists);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


}