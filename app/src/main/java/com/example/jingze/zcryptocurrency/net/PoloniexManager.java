package com.example.jingze.zcryptocurrency.net;

import android.os.Looper;
import android.os.Message;

import com.example.jingze.zcryptocurrency.model.Coin;
import com.example.jingze.zcryptocurrency.model.CoinMenu;
import com.example.jingze.zcryptocurrency.net.base.BourseActivityManager;

import java.util.ArrayList;

import okhttp3.Request;

/**
 * Created by Jingze HUANG on Mar.27, 2018.
 */

public class PoloniexManager extends BourseActivityManager {
    private RESTManager restManager;
    private boolean isSubscribed = true;
    private static int updateSpan = 2000;
    private static final String COMMAND = "command";

//    private static class EVENT {
//        String event;
//        ArrayList<Coin> data;
//    }

    private static final String API_URL = "https://poloniex.com/public";

//    public static class Event{
//
//    }

    public static class CHANNEL {
        public static final String Ticker = "returnTicker";
    }

    public PoloniexManager(Looper dataThreadLooper, CoinMenu coinMenu) {
        super(dataThreadLooper, coinMenu);
    }

    @Override
    public void startConnection() {
        class StartConnection implements Runnable{
            @Override
            public void run() {
                //position may be changed.
                coinMenu.initiate();

                restManager = new RESTManager
                        .Builder(mainThreadHandler, dataThreadHandler)
                        .build();
            }
        }
        dataThreadHandler.post(new StartConnection());
        String url = getUrl(CHANNEL.Ticker);
        Request request = new Request.Builder().url(url).build();
        sendAutoRequest(request, 0);
    }

    @Override
    public void subscribe(Coin coin) {

    }

    @Override
    public void subscribe(final String channel, final Coin coin, int delay) {

    }

    @Override
    public void unsubscribe(String channel, Coin coin) {

    }

    @Override
    public void stop() {

    }

    @Override
    protected boolean callbackLogic(Message message) {

        return false;
    }


    //Private methods
    private void sendAutoRequest(final Request request, int delay) {
        class autoRequestRunnable implements Runnable{
            @Override
            public void run() {
                restManager.makeRequest(request);
                if (isSubscribed) {
                    sendAutoRequest(request, updateSpan);
                }
            }
        }
        dataThreadHandler.postDelayed(new autoRequestRunnable(), delay);

    }

    private static String getUrl(String channel) {
//        String url = Uri.parse(API_URL)
//                .buildUpon()
//                .appendQueryParameter(COMMAND, channel)
//                .build()
//                .toString();
//        return url;
        return API_URL + "?command=" + channel;
    }

//    private Event parseMessage(Message message) {
//        Event newEvent = null;
//        String data = (String) message.obj;
//        if (data.charAt(0) == '{') {
//
//        }
//
//        return newEvent;
//    }

    private void getSubscribeRequest() {
//        Request request = new Request.Builder()
//                .addHeader()
    }


}
