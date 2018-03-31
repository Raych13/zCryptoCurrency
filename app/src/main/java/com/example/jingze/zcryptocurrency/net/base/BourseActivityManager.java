package com.example.jingze.zcryptocurrency.net.base;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.example.jingze.zcryptocurrency.model.Coin;
import com.example.jingze.zcryptocurrency.model.CoinMenu;
import com.example.jingze.zcryptocurrency.net.base.WebManager;

/**
 * Created by Jingze HUANG on Mar.25, 2018.
 */

public abstract class BourseActivityManager {
    protected WebManager webManager;
    protected Looper dataThreadLooper;
    protected Handler mainThreadHandler;
    protected Handler dataThreadHandler;
    protected CoinMenu coinMenu;
    protected boolean isFirstStart = true;

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

    protected abstract boolean handlerLogic(Message message);
}
