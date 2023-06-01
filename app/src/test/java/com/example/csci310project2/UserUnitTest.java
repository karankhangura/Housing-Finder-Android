package com.example.csci310project2;

import static org.junit.Assert.assertEquals;

import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * User Class Testing Suite
 */
@RunWith(MockitoJUnitRunner.class)
public class UserUnitTest {
    @Mock
    DataSnapshot inputSnapshot;

    @Mock
    DataSnapshot firstNameSnapShot;

    @Mock
    DataSnapshot lastNameSnapShot;

    @Mock
    DataSnapshot descriptionSnapShot;

    String testEmail = "TestEmail";
    String testFirstName = "TestFirst";
    String testLastName = "TestLast";
    String testDescription = "TestDescription";

    @Test
    public void testUserDataSnapshotConstructor() {
        // Arrange
        when(inputSnapshot.child("firstName")).thenReturn(firstNameSnapShot);
        when(firstNameSnapShot.getValue(String.class)).thenReturn(testFirstName);
        when(inputSnapshot.child("lastName")).thenReturn(lastNameSnapShot);
        when(lastNameSnapShot.getValue(String.class)).thenReturn(testLastName);
        when(inputSnapshot.getKey()).thenReturn(testEmail);
        when(inputSnapshot.hasChild("description")).thenReturn(true);
        when(inputSnapshot.child("description")).thenReturn(descriptionSnapShot);
        when(descriptionSnapShot.getValue(String.class)).thenReturn(testDescription);

        // Act
        User user = new User(inputSnapshot);

        // Assert
        assertEquals(user.getFirstName(), testFirstName);
        assertEquals(user.getLastName(), testLastName);
        assertEquals(user.getEmail(), testEmail);
        assertEquals(user.getProfileInformation().get("description"), testDescription);
    }

    @Test
    public void testGetUserFirstName_happyCase() {
        User user = new User(testFirstName, testLastName, testEmail, new HashMap<String, String>(), new ArrayList<HousingPost>());

        assertEquals(user.getFirstName(), testFirstName);
    }

    @Test
    public void testGetUserFirstName_nullCase() {
        User user = new User(null, testLastName, testEmail, new HashMap<String, String>(), new ArrayList<HousingPost>());

        assertNull(user.getFirstName());
    }

    @Test
    public void testSetUserFirstName_happyCase() {
        String changedFirstName = "ChangedFirstName";
        User user = new User(testFirstName, testLastName, testEmail, new HashMap<String, String>(), new ArrayList<HousingPost>());
        user.setFirstName(changedFirstName);

        assertEquals(user.getFirstName(), changedFirstName);
    }

    @Test
    public void testSetUserFirstName_nullCase() {
        User user = new User(testFirstName, testLastName, testEmail, new HashMap<String, String>(), new ArrayList<HousingPost>());
        user.setFirstName(null);

        assertNull(user.getFirstName());
    }

    @Test
    public void testGetUserLastName_happyCase() {
        User user = new User(testFirstName, testLastName, testEmail, new HashMap<String, String>(), new ArrayList<HousingPost>());

        assertEquals(user.getLastName(), testLastName);
    }

    @Test
    public void testGetUserLastName_nullCase() {
        User user = new User(testFirstName, null, testEmail, new HashMap<String, String>(), new ArrayList<HousingPost>());

        assertNull(user.getLastName());
    }

    @Test
    public void testSetUserLastName_happyCase() {
        String changedLastName = "ChangedLastName";
        User user = new User(testFirstName, testLastName, testEmail, new HashMap<String, String>(), new ArrayList<HousingPost>());
        user.setLastName(changedLastName);

        assertEquals(user.getLastName(), changedLastName);
    }

    @Test
    public void testSetUserLastName_nullCase() {
        User user = new User(testFirstName, testLastName, testEmail, new HashMap<String, String>(), new ArrayList<HousingPost>());
        user.setLastName(null);

        assertNull(user.getLastName());
    }

    @Test
    public void testGetUserEmail_happyCase() {
        User user = new User(testFirstName, testLastName, testEmail, new HashMap<String, String>(), new ArrayList<HousingPost>());

        assertEquals(user.getEmail(), testEmail);
    }

    @Test
    public void testGetUserEmail_nullCase() {
        User user = new User(testFirstName, testLastName, null, new HashMap<String, String>(), new ArrayList<HousingPost>());

        assertNull(user.getEmail());
    }

    @Test
    public void testSetUserEmail_happyCase() {
        String changedEmail = "ChangedEmail";
        User user = new User(testFirstName, testLastName, testEmail, new HashMap<String, String>(), new ArrayList<HousingPost>());
        user.setEmail(changedEmail);

        assertEquals(user.getEmail(), changedEmail);
    }

    @Test
    public void testSetUserEmail_nullCase() {
        User user = new User(testFirstName, testLastName, testEmail, new HashMap<String, String>(), new ArrayList<HousingPost>());
        user.setEmail(null);

        assertNull(user.getEmail());
    }

}
