package com.health2world.aio.util;

import android.util.Log;

import com.health2world.aio.config.AppConfig;

/**
 * Created by efan on 2017/4/13.
 */

public class Logger {

    public static void i(String tag, String msg) {
        if (AppConfig.isDebug) {
            Log.i(tag, msg);
        }
    }

    public static void v(String tag, String msg) {
        if (AppConfig.isDebug) {
            Log.v(tag, msg);
        }
    }

    public static void d(String tag, String msg) {
        if (AppConfig.isDebug) {
            Log.d(tag, msg);
        }
    }

    public static void w(String tag, String msg) {
        if (AppConfig.isDebug) {
            Log.w(tag, msg);
        }
    }

    public static void e(String tag, String msg) {
        if (AppConfig.isDebug) {
            Log.e(tag, msg);
        }
    }

}
