package com.example.jingze.zcryptocurrency.net;

import android.os.Looper;
import android.os.Message;

import com.example.jingze.zcryptocurrency.model.Coin;
import com.example.jingze.zcryptocurrency.model.CoinMenu;
import com.example.jingze.zcryptocurrency.net.base.BourseActivityManager;

/**
 * Created by Jingze HUANG on Mar.27, 2018.
 */

public class PoloniexManager extends BourseActivityManager {
    public static class Event{
    }

    public PoloniexManager(Looper dataThreadLooper, CoinMenu coinMenu) {
        super(dataThreadLooper, coinMenu);
    }

    @Override
    public void startConnection() {

    }

    @Override
    public void subscribe(Coin coin) {

    }

    @Override
    public void subscribe(String channel, Coin coin, int delay) {

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
}
