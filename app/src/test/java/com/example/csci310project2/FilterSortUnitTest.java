package com.example.csci310project2;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */

public class FilterSortUnitTest {
    private ArrayList<HousingPost> tester;
    private ViewListingFragment frag;

    @Before
    public void setup() {
        tester = new ArrayList<HousingPost>();
        User user = new User();
        Date deadline = new Date();
        ArrayList<Decision> respondedList = new ArrayList<>();

        HashMap<String, String> housingInformation1 = new HashMap<>();
        housingInformation1.put("location","1");
        housingInformation1.put("rent","1");
        housingInformation1.put("bed","1");
        housingInformation1.put("address", "test");
        housingInformation1.put("utilities", "test");
        housingInformation1.put("schedule", "test");
        housingInformation1.put("academicFocus", "test");
        housingInformation1.put("personality", "test");
        HousingPost one = new  HousingPost("name", user, housingInformation1, deadline,  respondedList);

        HashMap<String, String> housingInformation2 = new HashMap<>();
        housingInformation2.put("location","2");
        housingInformation2.put("rent","2");
        housingInformation2.put("bed","2");
        HousingPost two = new  HousingPost("name", user, housingInformation2, deadline,  respondedList);

        HashMap<String, String> housingInformation3 = new HashMap<>();
        housingInformation3.put("location","3");
        housingInformation3.put("rent","3");
        housingInformation3.put("bed","3");
        HousingPost three = new  HousingPost("name", user, housingInformation3, deadline,  respondedList);

        tester.add(one);
        tester.add(two);
        tester.add(three);
        frag = new ViewListingFragment();

    }

    @Test
    public void sortRent() {
        ArrayList<HousingPost> answer = frag.filterHousingPosts(tester, "sortRent", "");

        ArrayList<HousingPost> exp = new ArrayList<>();
        User user = new User();
        Date deadline = new Date();
        ArrayList<Decision> respondedList = new ArrayList<>();

        HashMap<String, String> housingInformation1 = new HashMap<>();
        housingInformation1.put("location","1");
        housingInformation1.put("rent","1");
        housingInformation1.put("bed","1");
        HousingPost one = new  HousingPost("name", user, housingInformation1, deadline,  respondedList);

        HashMap<String, String> housingInformation2 = new HashMap<>();
        housingInformation2.put("location","2");
        housingInformation2.put("rent","2");
        housingInformation2.put("bed","2");
        HousingPost two = new  HousingPost("name", user, housingInformation2, deadline,  respondedList);

        HashMap<String, String> housingInformation3 = new HashMap<>();
        housingInformation3.put("location","3");
        housingInformation3.put("rent","3");
        housingInformation3.put("bed","3");
        HousingPost three = new  HousingPost("name", user, housingInformation3, deadline,  respondedList);

        exp.add(one);
        exp.add(two);
        exp.add(three);

        assertEquals(exp.get(0).getHousingInformation().get("rent"), answer.get(0).getHousingInformation().get("rent"));
        assertEquals(exp.get(1).getHousingInformation().get("rent"), answer.get(1).getHousingInformation().get("rent"));
        assertEquals(exp.get(2).getHousingInformation().get("rent"), answer.get(2).getHousingInformation().get("rent"));
    }

    @Test
    public void filterRent() {
        ArrayList<HousingPost> answer = frag.filterHousingPosts(tester, "filterRent", "1");

        ArrayList<HousingPost> exp = new ArrayList<>();
        User user = new User();
        Date deadline = new Date();
        ArrayList<Decision> respondedList = new ArrayList<>();

        HashMap<String, String> housingInformation1 = new HashMap<>();
        housingInformation1.put("location","1");
        housingInformation1.put("rent","1");
        housingInformation1.put("bed","1");
        HousingPost one = new  HousingPost("name", user, housingInformation1, deadline,  respondedList);

        exp.add(one);
        assertEquals(exp.get(0).getHousingInformation().get("rent"), answer.get(0).getHousingInformation().get("rent"));
    }

    @Test
    public void sortBed() {
        ArrayList<HousingPost> answer = frag.filterHousingPosts(tester, "sortBeds", "");

        ArrayList<HousingPost> exp = new ArrayList<>();
        User user = new User();
        Date deadline = new Date();
        ArrayList<Decision> respondedList = new ArrayList<>();

        HashMap<String, String> housingInformation1 = new HashMap<>();
        housingInformation1.put("location","1");
        housingInformation1.put("rent","1");
        housingInformation1.put("bed","1");
        HousingPost one = new  HousingPost("name", user, housingInformation1, deadline,  respondedList);

        HashMap<String, String> housingInformation2 = new HashMap<>();
        housingInformation2.put("location","2");
        housingInformation2.put("rent","2");
        housingInformation2.put("bed","2");
        HousingPost two = new  HousingPost("name", user, housingInformation2, deadline,  respondedList);

        HashMap<String, String> housingInformation3 = new HashMap<>();
        housingInformation3.put("location","3");
        housingInformation3.put("rent","3");
        housingInformation3.put("bed","3");
        HousingPost three = new  HousingPost("name", user, housingInformation3, deadline,  respondedList);

        exp.add(one);
        exp.add(two);
        exp.add(three);
        assertEquals(exp.get(0).getHousingInformation().get("bed"), answer.get(0).getHousingInformation().get("bed"));
        assertEquals(exp.get(1).getHousingInformation().get("bed"), answer.get(1).getHousingInformation().get("bed"));
        assertEquals(exp.get(2).getHousingInformation().get("bed"), answer.get(2).getHousingInformation().get("bed"));
    }

    @Test
    public void filterBed() {
        ArrayList<HousingPost> answer = frag.filterHousingPosts(tester, "filterBeds", "1");

        ArrayList<HousingPost> exp = new ArrayList<>();
        User user = new User();
        Date deadline = new Date();
        ArrayList<Decision> respondedList = new ArrayList<>();

        HashMap<String, String> housingInformation1 = new HashMap<>();
        housingInformation1.put("address", "test");
        housingInformation1.put("utilities", "test");
        housingInformation1.put("schedule", "test");
        housingInformation1.put("academicFocus", "test");
        housingInformation1.put("personality", "test");

        housingInformation1.put("location","1");
        housingInformation1.put("rent","1");
        housingInformation1.put("bed","1");
        HousingPost one = new  HousingPost("name", user, housingInformation1, deadline,  respondedList);

        exp.add(one);
        assertEquals(exp.get(0).getHousingInformation().get("bed"), answer.get(0).getHousingInformation().get("bed"));

    }

    @Test
    public void sortLoc() {
        ArrayList<HousingPost> answer = frag.filterHousingPosts(tester, "sortLoc", "");

        ArrayList<HousingPost> expected = new ArrayList<>();
        ArrayList<HousingPost> exp = new ArrayList<>();
        User user = new User();
        Date deadline = new Date();
        ArrayList<Decision> respondedList = new ArrayList<>();

        HashMap<String, String> housingInformation1 = new HashMap<>();
        housingInformation1.put("location","1");
        housingInformation1.put("rent","1");
        housingInformation1.put("bed","1");
        HousingPost one = new  HousingPost("name", user, housingInformation1, deadline,  respondedList);

        HashMap<String, String> housingInformation2 = new HashMap<>();
        housingInformation2.put("location","2");
        housingInformation2.put("rent","2");
        housingInformation2.put("bed","2");
        HousingPost two = new  HousingPost("name", user, housingInformation2, deadline,  respondedList);

        HashMap<String, String> housingInformation3 = new HashMap<>();
        housingInformation3.put("location","3");
        housingInformation3.put("rent","3");
        housingInformation3.put("bed","3");
        HousingPost three = new  HousingPost("name", user, housingInformation3, deadline,  respondedList);

        exp.add(one);
        exp.add(two);
        exp.add(three);
        assertEquals(exp.get(0).getHousingInformation().get("location"), answer.get(0).getHousingInformation().get("location"));
        assertEquals(exp.get(1).getHousingInformation().get("location"), answer.get(1).getHousingInformation().get("location"));
        assertEquals(exp.get(2).getHousingInformation().get("location"), answer.get(2).getHousingInformation().get("location"));
    }

    @Test
    public void filterLoc() {
        ArrayList<HousingPost> answer = frag.filterHousingPosts(tester, "filterLoc", "3");

        ArrayList<HousingPost> exp = new ArrayList<>();
        User user = new User();
        Date deadline = new Date();
        ArrayList<Decision> respondedList = new ArrayList<>();

        HashMap<String, String> housingInformation1 = new HashMap<>();
        housingInformation1.put("location","3");
        housingInformation1.put("rent","3");
        housingInformation1.put("bed","3");
        HousingPost one = new  HousingPost("name", user, housingInformation1, deadline,  respondedList);

        exp.add(one);
        assertEquals(exp.get(0).getHousingInformation().get("location"), answer.get(0).getHousingInformation().get("location"));
    }

}