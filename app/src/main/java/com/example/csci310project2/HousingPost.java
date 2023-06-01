package com.example.csci310project2;

import java.io.Serializable;
import java.sql.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Date;

public class HousingPost implements Serializable {
    private String housingPostName;
    private User user;
    private HashMap<String, String> housingInformation;
    private Date deadline;
    private ArrayList<Decision> respondedList;

    public int getUsersAccepted() {
        return usersAccepted;
    }

    public void setUsersAccepted(int usersAccepted) {
        this.usersAccepted = usersAccepted;
    }

    public int getMaxAccepted() {
        return maxAccepted;
    }

    public void setMaxAccepted(int maxAccepted) {
        this.maxAccepted = maxAccepted;
    }

    private int usersAccepted = 0;
    private int maxAccepted = 2;

    public HousingPost(String name, User user, HashMap<String, String> housingInformation, Date deadline, ArrayList<Decision> respondedList) {
        this.housingPostName = name;
        this.user = user;
        this.housingInformation = housingInformation;
        this.deadline = deadline;
        this.respondedList = respondedList;
    }

    public HousingPost() {
        this("", null, new HashMap<String, String>(), new Date(), new ArrayList<Decision>());
    }

    public String getHousingPostName() {
        return housingPostName;
    }

    public void setHousingPostName(String name) {
        this.housingPostName = name;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public HashMap<String, String> getHousingInformation() {
        return housingInformation;
    }

    public void setHousingInformation(HashMap<String, String> housingInformation) {
        this.housingInformation = housingInformation;
    }

    public Date getDeadline() {
        return deadline;
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }

    public ArrayList<Decision> getRespondedList() {
        return respondedList;
    }

    public void setRespondedList(ArrayList<Decision> respondedList) {
        this.respondedList = respondedList;
    }

    public String toString() {
        String answer = "";
        answer+="address: "+housingInformation.get("address");
        answer+=", rent: "+housingInformation.get("rent");
        answer+=", location: "+housingInformation.get("location");

        answer+=", bed: "+housingInformation.get("bed");
        answer+=", utilities: "+housingInformation.get("utilities");
        answer+=", schedule: "+housingInformation.get("schedule");

        answer+=", academicFocus: "+housingInformation.get("academicFocus");
        answer+=", personality: "+housingInformation.get("personality");
        return answer;
    }

    public boolean equals(HousingPost obj){
        if(obj==null){
            return false;
        }
        return this.housingInformation.get("rent").equals(obj.getHousingInformation().get("rent"))
                && this.housingInformation.get("bed").equals(obj.getHousingInformation().get("bed"))
                && this.housingInformation.get("location").equals(obj.getHousingInformation().get("location"));
    }
}
