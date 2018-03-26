package com.example.jingze.zcryptocurrency.net;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.example.jingze.zcryptocurrency.model.Coin;
import com.example.jingze.zcryptocurrency.model.CoinMenu;
import com.example.jingze.zcryptocurrency.net.WebManager;

/**
 * Created by Jingze HUANG on Mar.25, 2018.
 */

public abstract class BourseActivityManager {
    Context context;
    WebManager webManager;
    Looper dataThreadLooper;
    Handler mainThreadHandler;
    Handler dataThreadHandler;
    CoinMenu coinMenu;
    boolean isFirstStart = true;

    //Constructor
    public BourseActivityManager(Context context, Looper dataThreadLooper, CoinMenu coinMenu) {
        this.context = context;
        this.mainThreadHandler = new Handler(Looper.getMainLooper());
        this.dataThreadLooper = dataThreadLooper;
        this.coinMenu = coinMenu;
        this.isFirstStart = true;
    }

    public abstract void buildWebSocket();

    public abstract void subscribe(String channel, Coin coin);

    public abstract void unsubscribe(String channel, Coin coin);

    public abstract void stop();

    abstract void setDataThreadHandler();
}
