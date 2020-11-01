package com.xmpp.smackchat.base.repository.api;


import com.xmpp.smackchat.base.thread.ThreadManager;

import okhttp3.Dispatcher;
import okhttp3.OkHttpClient;

public class BaseHttpClientBuilder {

    public static OkHttpClient.Builder defaultBuilder() {
        OkHttpClient.Builder builder = customBuilder();
        Dispatcher apiDispatcher = new Dispatcher(ThreadManager.getInstance().getNetworkPool());
        builder.dispatcher(apiDispatcher);
        return builder;
    }

    public static OkHttpClient.Builder customBuilder() {
        return new OkHttpClient.Builder();
    }
}
