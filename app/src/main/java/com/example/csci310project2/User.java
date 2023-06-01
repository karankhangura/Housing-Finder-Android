package com.example.csci310project2;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.ArrayList;

public class User implements Serializable {
    //private String username;
    private String firstName;
    private String lastName;
    private String email;
    private HashMap<String, String> profileInformation;
    private ArrayList<HousingPost> housingPosts;

    public User(String firstName, String lastName, String email, HashMap<String, String> profileInformation,
                ArrayList<HousingPost> housingPosts) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.profileInformation = profileInformation;
        this.housingPosts = housingPosts;
    }

    public User() {
        this("", "", "", new HashMap<String, String>(), new ArrayList<HousingPost>());
    }

    public User(DataSnapshot userItem) {
        firstName = userItem.child("firstName").getValue(String.class);
        lastName = userItem.child("lastName").getValue(String.class);
        email = Utility.decodeEmail(userItem.getKey());
        profileInformation = new HashMap<>();
        if(userItem.hasChild("description")) {
            profileInformation.put("description", userItem.child("description").getValue(String.class));
        }
        housingPosts = new ArrayList<>();
    }

    public void copy(User altUser) {
        firstName = altUser.firstName;
        lastName = altUser.lastName;
        email = altUser.email;
        profileInformation = altUser.profileInformation;
        housingPosts = altUser.housingPosts;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public HashMap<String, String> getProfileInformation() {
        return profileInformation;
    }

    public void setProfileInformation(HashMap<String, String> profileInformation) {
        this.profileInformation = profileInformation;
    }

    public ArrayList<HousingPost> getHousingPosts() {
        return housingPosts;
    }

    public void setHousingPosts(ArrayList<HousingPost> housingPosts) {
        this.housingPosts = housingPosts;
    }
}
