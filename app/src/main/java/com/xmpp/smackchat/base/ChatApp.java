package com.xmpp.smackchat.base;

import android.app.Application;

import com.xmpp.smackchat.BuildConfig;

import org.jivesoftware.smack.SmackConfiguration;

public class ChatApp extends Application {
    private static ChatApp sInstance;

    public static synchronized ChatApp getInstance() {
        return sInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;

        SmackConfiguration.DEBUG = BuildConfig.DEBUG;
    }
}
