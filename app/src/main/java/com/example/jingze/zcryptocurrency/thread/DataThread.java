package com.example.jingze.zcryptocurrency.thread;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Process;

/**
 * Created by Jingze HUANG on Mar.22, 2018.
 */

public class DataThread {
    Handler dataThreadHandler;
    public void start() {

        Thread one = new Thread(new Runnable() {
            @Override
            public void run() {
                Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
                Looper.prepare();
                dataThreadHandler = new Handler();
                Looper.loop();

            }
        });
        one.start();
//
//        HandlerThread dataThread = new HandlerThread("Data");
//        Handler dataThreadHanlder = (Handler) dataThread.getUncaughtExceptionHandler();
//        dataThread.s
    }
}
