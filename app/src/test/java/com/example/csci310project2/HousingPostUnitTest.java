package com.example.csci310project2;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(MockitoJUnitRunner.class)
public class HousingPostUnitTest {
    @Mock
    User user;

    String testAddress = "TestAddress";
    String testRent = "TestRent";
    String testLocation = "TestLocation";
    String testBed = "TestBed";
    String testUtilities = "TestUtilities";
    String testSchedule = "TestSchedule";
    String testAcademicFocus = "TestAcademicFocus";
    String testPersonality = "TestPersonality";

    @Test
    public void testHousingPostToString() {
        HashMap<String, String> housingInformation = new HashMap<>();
        housingInformation.put("address", testAddress);
        housingInformation.put("rent", testRent);
        housingInformation.put("location", testLocation);
        housingInformation.put("bed", testBed);
        housingInformation.put("utilities", testUtilities);
        housingInformation.put("schedule", testSchedule);
        housingInformation.put("academicFocus", testAcademicFocus);
        housingInformation.put("personality", testPersonality);
        HousingPost housingPost = new HousingPost("TestName", new User(), housingInformation, new Date(), new ArrayList<Decision>());
        String toStringResult = "address: TestAddress, rent: TestRent, location: TestLocation, bed: TestBed, " +
                "utilities: TestUtilities, schedule: TestSchedule, academicFocus: TestAcademicFocus, personality: TestPersonality";

        assertEquals(housingPost.toString(), toStringResult);
    }

    @Test
    public void testGetHousingPostName_happyCase() {
        String testName = "TestName";
        HousingPost housingPost = new HousingPost(testName, new User(), new HashMap<String, String>(), new Date(), new ArrayList<Decision>());

        assertEquals(housingPost.getHousingPostName(), testName);
    }

    @Test
    public void testGetHousingPostName_nullCase() {
        HousingPost housingPost = new HousingPost(null, new User(), new HashMap<String, String>(), new Date(), new ArrayList<Decision>());

        assertNull(housingPost.getHousingPostName());
    }

    @Test
    public void testSetHousingPostName_happyCase() {
        String changedName = "ChangedName";
        HousingPost housingPost = new HousingPost("TestName", new User(), new HashMap<String, String>(), new Date(), new ArrayList<Decision>());
        housingPost.setHousingPostName(changedName);

        assertEquals(housingPost.getHousingPostName(), changedName);
    }

    @Test
    public void testSetHousingPostName_nullCase() {
        HousingPost housingPost = new HousingPost("TestName", new User(), new HashMap<String, String>(), new Date(), new ArrayList<Decision>());
        housingPost.setHousingPostName(null);

        assertNull(housingPost.getHousingPostName());
    }

    @Test
    public void testGetUser_happyCase() {
        HousingPost housingPost = new HousingPost("TestName", user, new HashMap<String, String>(), new Date(), new ArrayList<Decision>());

        assertEquals(housingPost.getUser(), user);
    }

    @Test
    public void testGetUser_nullCase() {
        HousingPost housingPost = new HousingPost("TestName", null, new HashMap<String, String>(), new Date(), new ArrayList<Decision>());

        assertNull(housingPost.getUser());
    }

    @Test
    public void testSetUser_happyCase() {
        HousingPost housingPost = new HousingPost("TestName", new User(), new HashMap<String, String>(), new Date(), new ArrayList<Decision>());
        housingPost.setUser(user);

        assertEquals(housingPost.getUser(), user);
    }

    @Test
    public void testSetUser_nullCase() {
        HousingPost housingPost = new HousingPost("TestName", user, new HashMap<String, String>(), new Date(), new ArrayList<Decision>());
        housingPost.setUser(null);

        assertNull(housingPost.getUser());
    }

    @Test
    public void testGetHousingInformation_happyCase() {
        HashMap<String, String> housingInformation = new HashMap<>();
        HousingPost housingPost = new HousingPost("TestName", user, housingInformation, new Date(), new ArrayList<Decision>());

        assertEquals(housingPost.getHousingInformation(), housingInformation);
    }

    @Test
    public void testGetHousingInformation_nullCase() {
        HousingPost housingPost = new HousingPost("TestName", user, null, new Date(), new ArrayList<Decision>());

        assertNull(housingPost.getHousingInformation());
    }

    @Test
    public void testSetHousingInformation_happyCase() {
        HashMap<String, String> housingInformation = new HashMap<>();
        HousingPost housingPost = new HousingPost("TestName", user, new HashMap<String, String>(), new Date(), new ArrayList<Decision>());
        housingPost.setHousingInformation(housingInformation);

        assertEquals(housingPost.getHousingInformation(), housingInformation);
    }

    @Test
    public void testSetHousingInformation_nullCase() {
        HashMap<String, String> housingInformation = new HashMap<>();
        HousingPost housingPost = new HousingPost("TestName", user, housingInformation, new Date(), new ArrayList<Decision>());
        housingPost.setHousingInformation(null);

        assertNull(housingPost.getHousingInformation());
    }

    @Test
    public void testGetDeadline_happyCase() {
        Date deadline = new Date(1234567);
        HousingPost housingPost = new HousingPost("TestName", user, new HashMap<String, String>(), deadline, new ArrayList<Decision>());

        assertEquals(housingPost.getDeadline(), deadline);
    }

    @Test
    public void testGetDeadline_nullCase() {
        HousingPost housingPost = new HousingPost("TestName", user, new HashMap<String, String>(), null, new ArrayList<Decision>());

        assertNull(housingPost.getDeadline());
    }

    @Test
    public void testSetDeadline_happyCase() {
        Date deadline = new Date(1234567);
        HousingPost housingPost = new HousingPost("TestName", user, new HashMap<String, String>(), new Date(), new ArrayList<Decision>());
        housingPost.setDeadline(deadline);

        assertEquals(housingPost.getDeadline(), deadline);
    }

    @Test
    public void testSetDeadline_nullCase() {
        Date deadline = new Date(1234567);
        HousingPost housingPost = new HousingPost("TestName", user, new HashMap<String, String>(), deadline, new ArrayList<Decision>());
        housingPost.setDeadline(null);

        assertNull(housingPost.getDeadline());
    }

}