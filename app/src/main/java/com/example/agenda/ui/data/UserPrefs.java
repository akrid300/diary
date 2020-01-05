package com.example.agenda.ui.data;

import android.content.SharedPreferences;

public class UserPrefs {

    private String USER_NAME = "USER_NAME";
    private String USER_EMAIL = "USER_EMAIL";
    private String USER_AGE = "USER_AGE";
    private String USER_EVENTS = "USER_EVENTS";

    //region  Main Constructor

    private static UserPrefs mUserPrefs = null;

    public static UserPrefs getInstance() {
        return mUserPrefs;
    }

    public static void setUpUserPrefs(SharedPreferences sharedPreferences) {
        mUserPrefs = new UserPrefs(sharedPreferences);
    }

    private SharedPreferences sharedPreferences;

    public UserPrefs(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }
    //endregion


    public String getUserName() {
        return sharedPreferences.getString(USER_NAME, "");
    }

    public void setUserNmae(String userName) {
        sharedPreferences.edit().putString(USER_NAME, userName).apply();
    }

    public String getUserEmail(){
        return sharedPreferences.getString(USER_EMAIL,"");
    }

    public void setUserEmail(String userEmail){
        sharedPreferences.edit().putString(USER_EMAIL, userEmail).apply();
    }

    public Integer getUserAge(){
        return sharedPreferences.getInt(USER_AGE,0);
    }

    public void setUserAge(int userAge){
        sharedPreferences.edit().putInt(USER_AGE,userAge).apply();
    }

    public Integer getUserEvents(){
        return sharedPreferences.getInt(USER_EVENTS,0);
    }

    public void setUserEvents(int userEvents){
        sharedPreferences.edit().putInt(USER_EVENTS, userEvents).apply();
    }
}