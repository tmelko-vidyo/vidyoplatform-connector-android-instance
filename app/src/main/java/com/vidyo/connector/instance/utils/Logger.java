package com.vidyo.connector.instance.utils;

import android.util.Log;

import com.vidyo.connector.instance.BuildConfig;

public class Logger {

    public enum LogType {
        ERROR, INFO, WARNING
    }

    private static final boolean ENABLED = BuildConfig.DEBUG;

    private static final String TAG = "VidyoConnector";

    public static void e(String error) {
        log(null, error, LogType.ERROR);
    }

    public static void e(String error, Object... arg) {
        log(null, String.format(error, arg), LogType.ERROR);
    }

    public static void e(Class cls, String error) {
        log(cls, error, LogType.ERROR);
    }

    public static void i(String info) {
        log(null, info, LogType.INFO);
    }

    public static void i(String info, Object... arg) {
        log(null, String.format(info, arg), LogType.INFO);
    }

    public static void i(Class cls, String info) {
        log(cls, info, LogType.INFO);
    }

    public static void w(String warning) {
        log(null, warning, LogType.WARNING);
    }

    public static void w(Class cls, String warning) {
        log(cls, warning, LogType.WARNING);
    }

    private static void log(Class cls, String message, LogType logType) {
        StringBuilder builder = new StringBuilder();
        if (cls != null) {
            builder.append(cls.getSimpleName());
            builder.append(": ");
        }

        if (message != null) {
            builder.append(message);
        }

        String out = builder.toString();

        if (!ENABLED) return;

        switch (logType) {
            case ERROR:
                Log.e(TAG, out);
                break;
            case WARNING:
                Log.w(TAG, out);
                break;
            case INFO:
                Log.i(TAG, out);
                break;
        }
    }
}