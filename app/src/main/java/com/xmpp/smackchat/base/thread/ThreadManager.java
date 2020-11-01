package com.xmpp.smackchat.base.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadManager {
    private static ThreadManager sInstance = null;

    private ExecutorService networkPool;

    private ThreadManager() {
        networkPool = Executors.newFixedThreadPool(10);
    }

    public static ThreadManager getInstance() {
        if (sInstance == null) {
            sInstance = new ThreadManager();
        }
        return sInstance;
    }

    public ExecutorService getNetworkPool() {
        return networkPool;
    }
}
