package com.politics.politicalapp.apputils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.politics.politicalapp.pojo.SettingsResponse;

import app.app.patidarsaurabh.apputils.AppConstants;

public class SPreferenceManager {

    private static final String PREF_NAME = "politics";
    private static SPreferenceManager mInstance;
    private SharedPreferences mPreferences;
    private SharedPreferences.Editor mEditor;
    private Gson gson;


    private SPreferenceManager(Context context) {
        mPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        mEditor = mPreferences.edit();
        gson = new Gson();
    }

    public static SPreferenceManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new SPreferenceManager(context);
        }
        return mInstance;
    }

    public Boolean isLogin() {
        return mPreferences.getBoolean(AppConstants.IS_LOGIN, false);
    }

    public void saveSession(SettingsResponse settingsResponse, String mobileNumber, String username) {
        setBoolean(AppConstants.IS_LOGIN, true);
        setString(AppConstants.MOBILE, mobileNumber);
        setString(AppConstants.NAME, username);
        saveSettings(settingsResponse);
    }

    public String getSession() {
        return mPreferences.getString(AppConstants.MOBILE, "");
    }

    public void setString(String key, String value) {
        mEditor.putString(key, value).apply();
    }

    public String getString(String key, String value) {
        return mPreferences.getString(key, value);
    }

    public void setInteger(String key, int value) {
        mEditor.putInt(key, value).apply();
    }

    public int getInteger(String key, int defaultValue) {
        return mPreferences.getInt(key, defaultValue);
    }

    public void setLong(String key, long value) {
        mEditor.putLong(key, value).apply();
    }

    public long getLong(String key, long defaultValue) {
        return mPreferences.getLong(key, defaultValue);
    }

    public void setBoolean(String key, boolean value) {
        mEditor.putBoolean(key, value).apply();
    }

    public boolean getBoolean(String key) {
        return mPreferences.getBoolean(key, false);
    }

    public boolean getBoolean(String key, Boolean defaultVal) {
        return mPreferences.getBoolean(key, defaultVal);
    }

    public void clearSession() {
        mPreferences.edit().clear().apply();
    }

    public void saveSettings(SettingsResponse settingsResponse) {
        setString(AppConstants.SETTINGS, gson.toJson(settingsResponse));
    }

    public SettingsResponse getSettings() {
        return gson.fromJson(getString(AppConstants.SETTINGS, null), SettingsResponse.class);
    }
}