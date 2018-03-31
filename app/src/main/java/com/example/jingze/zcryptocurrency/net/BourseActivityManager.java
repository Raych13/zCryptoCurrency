package com.example.jingze.zcryptocurrency.net;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.example.jingze.zcryptocurrency.model.Coin;
import com.example.jingze.zcryptocurrency.model.CoinMenu;
import com.example.jingze.zcryptocurrency.net.WebManager;

/**
 * Created by Jingze HUANG on Mar.25, 2018.
 */

public abstract class BourseActivityManager {
    WebManager webManager;
    Looper dataThreadLooper;
    Handler mainThreadHandler;
    Handler dataThreadHandler;
    CoinMenu coinMenu;
    boolean isFirstStart = true;

    //Constructor
    public BourseActivityManager(Looper dataThreadLooper, CoinMenu coinMenu) {
        this.mainThreadHandler = new Handler(Looper.getMainLooper());
        this.dataThreadLooper = dataThreadLooper;
        this.coinMenu = coinMenu;
        this.isFirstStart = true;
        this.dataThreadHandler = new Handler(dataThreadLooper, new Handler.Callback() {
            @Override
            public boolean handleMessage(Message message) {
                return handlerLogic(message);
            }
        });
    }

    public abstract void startConnection();

    public abstract void subscribe(String channel, Coin coin);

    public abstract void unsubscribe(String channel, Coin coin);

    public abstract void stop();

    abstract boolean handlerLogic(Message message);
}
