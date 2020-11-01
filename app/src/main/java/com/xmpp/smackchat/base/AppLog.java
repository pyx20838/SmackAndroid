package com.xmpp.smackchat.base;

import android.util.Log;

public class AppLog {
    private static final String LOGTAG = "nt.dung";

    public static void d(String msg) {
        Log.d(LOGTAG, getCodeLocation() + msg);
    }

    public static void i(String msg) {
        Log.i(LOGTAG, getCodeLocation() + msg);
    }

    public static void w(String msg) {
        Log.w(LOGTAG, getCodeLocation() + msg);
    }

    public static void e(String msg) {
        Log.e(LOGTAG, getCodeLocation() + msg);
    }

    public static void v(String msg) {
        Log.v(LOGTAG, getCodeLocation() + msg);
    }

    private static String getCodeLocation() {
        Thread cthread = Thread.currentThread();
        String fullClassName = cthread.getStackTrace()[4].getClassName();
        String className = fullClassName.substring(fullClassName.lastIndexOf(".") + 1);
        String methodName = cthread.getStackTrace()[4].getMethodName();
        int lineNumber = cthread.getStackTrace()[4].getLineNumber();
        return "(" + cthread.getId() + ") " + className + "." + methodName + "():" + lineNumber + ": ";
    }
}
