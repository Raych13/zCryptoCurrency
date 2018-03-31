package com.example.jingze.zcryptocurrency.utils;

import android.util.Log;

/**
 * Created by Jingze HUANG on Mar.30, 2018.
 */

public class RunningTimeUtils {
    public static long startTime;
    public static void start() {
        startTime = System.currentTimeMillis();
    }
    public static void end(String method) {
        long endTime=System.currentTimeMillis();
        Log.i("RaychTime", method + " has run： " + (endTime - startTime) + " ms.");
    }
}
