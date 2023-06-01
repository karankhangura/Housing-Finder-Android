package com.example.csci310project2;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.junit.Assert.assertEquals;

import android.provider.ContactsContract;

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

import java.util.HashMap;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class CreateListingInstrumentedTest {

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
    public void testingCreateListingGoodInput() {
        boilerPlateLogin();

        try {
            Thread.sleep(5000);
        } catch (Exception e) {

        }

        onView(withId(R.id.button_post)).perform(click());

        onView(withId(R.id.inputPostName))
                .perform(typeText("test PostName"), closeSoftKeyboard());
        onView(withId(R.id.inputAddress))
                .perform(typeText("test address"), closeSoftKeyboard());
        onView(withId(R.id.inputRent))
                .perform(typeText("test rent"), closeSoftKeyboard());
        onView(withId(R.id.inputLocation))
                .perform(typeText("test location"), closeSoftKeyboard());
        onView(withId(R.id.inputBeds))
                .perform(typeText("test bed"), closeSoftKeyboard());
        onView(withId(R.id.inputUtilities))
                .perform(typeText("test utilities"), closeSoftKeyboard());
        onView(withId(R.id.inputSch))
                .perform(typeText("test schedule"), closeSoftKeyboard());
        onView(withId(R.id.inputAcademic))
                .perform(typeText("test academicFocus"), closeSoftKeyboard());
        onView(withId(R.id.inputPersonality))
                .perform(typeText("test personality"), closeSoftKeyboard());
        onView(withId(R.id.inputDeadline))
                .perform(typeText("test"), closeSoftKeyboard());

        onView(withId(R.id.buttonCreateListing)).perform(click());

        try {
            Thread.sleep(5000);
        } catch (Exception e) {

        }

        FirebaseDatabase root = FirebaseDatabase.getInstance();
        DatabaseReference ref = root.getReference();
        ref = ref.child("post").child("test").child("test PostName");

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // get all values
                String postName = snapshot.child("postName").getValue(String.class);
                HashMap<String, String> postInfo = new HashMap<>();
                for (DataSnapshot item : snapshot.child("housingInfo").getChildren()) {
                    postInfo.put(item.getKey(), item.getValue(String.class));
                }

                // delete from database
                DatabaseReference refNew = root.getReference();
                refNew = refNew.child("post").child("test").child("test");
                refNew.getRef().removeValue();

                // assert equals
                assertEquals("test PostName", postName);
                assertEquals("test academicFocus", postInfo.get("academicFocus"));
                assertEquals("test address", postInfo.get("address"));
                assertEquals("test bed", postInfo.get("bed"));
                assertEquals("test location", postInfo.get("location"));
                assertEquals("test personality", postInfo.get("personality"));
                assertEquals("test rent", postInfo.get("rent"));
                assertEquals("test schedule", postInfo.get("schedule"));
                assertEquals("test utilities", postInfo.get("utilities"));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Test
    public void testingCreateListingBadInput() {
        boilerPlateLogin();
        onView(withId(R.id.button_post)).perform(click());
        onView(withId(R.id.buttonCreateListing)).perform(click());
        onView(withId(R.id.createErrorTextView)).check(matches(withText("Failed to create. Enter all information.")));
    }


}