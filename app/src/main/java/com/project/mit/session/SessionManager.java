package com.project.mit.session;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import java.util.HashMap;

public class SessionManager {
    private static final String LOGIN = "IS_LOGIN";

    public static final String ID = "ID";
    public static final String UID = "UID";
    public static final String FIRSTNAME = "FIRSTNAME";
    public static final String LASTNAME = "LASTNAME";
    public static final String PROFILE_PICTURE = "PROFILE_PICTURE";
    public static final String BIRTHDAY = "BIRTHDAY";
    public static final String EMAIL = "EMAIL";
    public static final String PHONE_NO = "PHONE_NO";
    public static final String ADDRESS01 = "ADDRESS01";
    public static final String ADDRESS02 = "ADDRESS02";
    public static final String CITY = "DIVISION";
    public static final String POSTCODE = "POSTCODE";
    public static final String STATE = "STATE";
    public SharedPreferences.Editor editor;
    public Context context;
    SharedPreferences sharedPreferences;
    int PRIVATE_MODE = 0;

    @SuppressLint("CommitPrefEdits")
    public SessionManager(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences("LOGIN", PRIVATE_MODE);
        editor = sharedPreferences.edit();
    }

    public void createSession(String Uid, String FirstName, String LastName, String ProfilePicture,
                              String Birthday, String Email, String PhoneNo,
                              String Address01, String Address02, String City,
                              String State, String PostCode) {
        editor.putBoolean(LOGIN, true);
        editor.putString(FIRSTNAME, FirstName);
        editor.putString(LASTNAME, LastName);
        editor.putString(PROFILE_PICTURE, ProfilePicture);
        editor.putString(EMAIL, Email);
        editor.putString(PHONE_NO, PhoneNo);
        editor.putString(ADDRESS01, Address01);
        editor.putString(ADDRESS02, Address02);
        editor.putString(CITY, City);
        editor.putString(POSTCODE, PostCode);
        editor.putString(BIRTHDAY, Birthday);
        editor.putString(STATE, State);
        editor.putString(UID, Uid);
        editor.apply();
    }

    public boolean isLoggin() {
        return sharedPreferences.getBoolean(LOGIN, false);
    }

    public void checkLogin() {
        if (!this.isLoggin()) {
//            Intent intent = new Intent(context, Homepage.class);
//            context.startActivity(intent);
//            ((MainActivity) context).finish();
        }
    }

    public HashMap<String, String> getUserDetail() {
        HashMap<String, String> user = new HashMap<>();
        user.put(FIRSTNAME, sharedPreferences.getString(FIRSTNAME, null));
        user.put(LASTNAME, sharedPreferences.getString(LASTNAME, null));
        user.put(PROFILE_PICTURE, sharedPreferences.getString(PROFILE_PICTURE, null));
        user.put(EMAIL, sharedPreferences.getString(EMAIL, null));
        user.put(PHONE_NO, sharedPreferences.getString(PHONE_NO, null));
        user.put(ADDRESS01, sharedPreferences.getString(ADDRESS01, null));
        user.put(ADDRESS02, sharedPreferences.getString(ADDRESS02, null));
        user.put(CITY, sharedPreferences.getString(CITY, null));
        user.put(POSTCODE, sharedPreferences.getString(POSTCODE, null));
        user.put(BIRTHDAY, sharedPreferences.getString(BIRTHDAY, null));
        user.put(STATE, sharedPreferences.getString(STATE, null));
        user.put(UID, sharedPreferences.getString(UID, null));
        user.put(ID, sharedPreferences.getString(ID, null));
        return user;
    }

    public void logout() {
        editor.clear();
        editor.commit();
//        Intent intent = new Intent(context, MainActivity.class);
//        context.startActivity(intent);
//        ((Homepage) context).finish();
    }

}
