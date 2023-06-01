package com.example.csci310project2;

import java.io.Serializable;
import java.util.Date;

public class Decision implements Serializable {
    public boolean accepted;
    public String contents;
    public String sender;
    public String receiver;
    public Date timestamp;
}
