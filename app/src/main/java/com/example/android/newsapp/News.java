package com.example.android.newsapp;

/**
 * Created by sudha on 21-Jun-17.
 */

public class News {

    //Member variable declaration
    private String mTitle;
    private String mSection;
    private String mDate;

    //Constructor Declaration
    News (String title, String section, String date){
        mTitle = title;
        mSection = section;
        mDate = date;
    }

    //Getter methods
    public String getTitle (){ return mTitle; }
    public String getSection (){ return mSection; }
    public String getDate () { return mDate;}
}
