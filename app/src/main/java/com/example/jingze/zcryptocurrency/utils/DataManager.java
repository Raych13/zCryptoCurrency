package com.example.jingze.zcryptocurrency.utils;

import android.os.Handler;

import com.example.jingze.zcryptocurrency.model.Coin;
import com.example.jingze.zcryptocurrency.model.CoinMenu;
import com.example.jingze.zcryptocurrency.net.WebSocketManager;
import com.example.jingze.zcryptocurrency.view.base.InfiniteAdapter;

import java.util.List;


/**
 * Created by Jingze HUANG on Mar.22, 2018.
 */

public class DataManager {
    Handler mainThreadHandler;
    Handler dataThreadHandler;
    InfiniteAdapter recyclerAdapter;
    CoinMenu coinMenu;
    WebSocketManager mWebSocketManager;

    DataManager (Handler mainThreadHandler, Handler dataThreadHandler, InfiniteAdapter infiniteAdapter, CoinMenu coinMenu) {
        this.mainThreadHandler = mainThreadHandler;
        this.dataThreadHandler = dataThreadHandler;
        this.recyclerAdapter = infiniteAdapter;
        this.coinMenu = coinMenu;
    }


}
