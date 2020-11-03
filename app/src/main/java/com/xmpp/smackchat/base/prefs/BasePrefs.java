package com.xmpp.smackchat.base.prefs;

import android.content.Context;
import android.content.SharedPreferences;

import com.xmpp.smackchat.base.ChatApp;

import java.util.Set;

public class BasePrefs {

    private static SharedPreferences sharedPreferences = ChatApp.getInstance().getSharedPreferences("chat-prefs", Context.MODE_PRIVATE);;
    
    public static void writeBoolean(String prefKey, boolean value) {
        sharedPreferences.edit().putBoolean(prefKey, value).apply();
    }

    public static void writeLong(String prefKey, long value) {
        sharedPreferences.edit().putLong(prefKey, value).apply();
    }

    public static void writeInt(String prefKey, int value) {
        sharedPreferences.edit().putInt(prefKey, value).apply();
    }

    public static void writeString(String prefKey, String value) {
        sharedPreferences.edit().putString(prefKey, value).apply();
    }

    public static void writeFloat(String prefKey, float value) {
        sharedPreferences.edit().putFloat(prefKey, value).apply();
    }

    public static void writeStringSet(String prefKey, Set<String> value) {
        sharedPreferences.edit().putStringSet(prefKey, value).apply();
    }

    public static boolean readBoolean(String prefKey, boolean defValue) {
        return sharedPreferences.getBoolean(prefKey, defValue);
    }

    public static long readLong(String prefKey, long defValue) {
        return sharedPreferences.getLong(prefKey, defValue);
    }

    public static int readInt(String prefKey, int defValue) {
        return sharedPreferences.getInt(prefKey, defValue);
    }

    public static String readString(String prefKey, String defValue) {
        return sharedPreferences.getString(prefKey, defValue);
    }

    public static float readFloat(String prefKey, float defValue) {
        return sharedPreferences.getFloat(prefKey, defValue);
    }

    public static Set<String> readStringSet(String prefKey, Set<String> defValue) {
        return sharedPreferences.getStringSet(prefKey, defValue);
    }
}
