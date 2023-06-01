package com.example.csci310project2;

import static com.example.csci310project2.Utility.specialCharacterRegex;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */

@RunWith(MockitoJUnitRunner.class)
public class EncodeDecodeUnitTest {
    @Mock
    DataSnapshot inputSnapshot;

    @Mock
    DataSnapshot firstNameSnapShot;


    @Test
    public void testEncodeEmail() {
        String email = "Example.Email@email.com";

        String encodedEmail = Utility.encodeEmail(email);

        // ensures string does not contain special characters .#$[] which
        // firebase does not allow in the string for a key
        assertEquals(false, encodedEmail.matches(specialCharacterRegex));

    }

    @Test
    public void testDecodeEmail() {
        String email = "Example.Email@email.com";

        String encodedEmail = Utility.encodeEmail(email);

        String decodedEmail = Utility.decodeEmail(encodedEmail);

        assertEquals(email, decodedEmail);

    }

}