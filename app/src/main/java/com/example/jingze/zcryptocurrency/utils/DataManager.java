package com.example.jingze.zcryptocurrency.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.example.jingze.zcryptocurrency.model.Coin;
import com.example.jingze.zcryptocurrency.model.CoinMenu;
import com.example.jingze.zcryptocurrency.net.Bitfinex;
import com.example.jingze.zcryptocurrency.net.WebSocketManager;
import com.example.jingze.zcryptocurrency.view.base.InfiniteAdapter;


/**
 * Created by Jingze HUANG on Mar.22, 2018.
 */

public class DataManager {
    Context context;
    Handler mainThreadHandler;
    Handler dataThreadHandler;
    Looper dataThreadLooper;
    InfiniteAdapter recyclerAdapter;
    CoinMenu coinMenu;
    WebSocketManager mWebSocketManager;
    private boolean isFirstStart;

    //Constructor
    public DataManager(Context context, Looper dataThreadLooper, InfiniteAdapter recyclerAdapter, CoinMenu coinMenu) {
        this.context = context;
        this.mainThreadHandler = new Handler(Looper.getMainLooper(), new Handler.Callback() {
            @Override
            public boolean handleMessage(Message message) {
                return false;
            }
        });
        this.dataThreadLooper = dataThreadLooper;
        this.recyclerAdapter = recyclerAdapter;
        this.coinMenu = coinMenu;
        this.isFirstStart = true;
        setDataThreadHandler();
    }

    public void connectToWeb() {
        dataThreadHandler.post(new Runnable() {
            @Override
            public void run() {
                coinMenu.updateCoinPositionToCoin();

                String url = coinMenu.getUrl();
                mWebSocketManager = new WebSocketManager
                        .Builder(context, mainThreadHandler, dataThreadHandler)
                        .url(url)
                        .isNeedReconnect(true)
                        .build();
                mWebSocketManager.startConnect();

                //Run 13000ms for testing
                dataThreadHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mWebSocketManager.stopConnect();
                    }
                }, 23000);
            }
        });
    }

    private void setDataThreadHandler() {
        this.dataThreadHandler = new Handler(dataThreadLooper, new Handler.Callback() {
            @Override
            public boolean handleMessage(Message message) {
                Bitfinex newMsg = Bitfinex.parseData((String) message.obj);
                switch (newMsg.event) {
                    case Bitfinex.EVENT.INFO: {
                        Log.i("Raych", "Get a Bitfinex event(Info): ");
                        if (isFirstStart) {
                            subscribeAllCoins();
                            isFirstStart = false;
                        }
                        break;
                    }
                    case Bitfinex.EVENT.SUBSCRIBED: {
                        Log.i("Raych", "Get a Bitfinex event(Subscribed): ");
                        buildConnection(newMsg);
                        break;
                    }
                    case Bitfinex.EVENT.ERROR: {
                        Log.i("Raych", "Get a Bitfinex event(Error): ");
                        break;
                    }
                    case Bitfinex.EVENT.UPDATE: {
                        Log.i("Raych", "Get a Bitfinex event(Update): ");
                        if (newMsg.coin != null) {
                            coinMenu.updateCoin(newMsg.chanId, newMsg.coin);
                        }
                        break;
                    }
                    case Bitfinex.EVENT.HEARTBEATTING: {
                        Log.i("Raych", "Get a Bitfinex event(HeartBeating): ");
                        coinMenu.heartbeatting(newMsg.chanId);
                        break;
                    }
                    default: {
                        Log.i("Raych", "Get a Bitfinex event(Unknown): ");
                    }
                }
                return false;
            }
        });
    }

    private void subscribeAllCoins() {
        for (int i = 0; i < coinMenu.size(); i++) {
            dataThreadHandler.post(Bitfinex.subscibeRunnable(mWebSocketManager, Bitfinex.CHANNEL.TICKER, coinMenu.getCoin(i)));
        }
    }

    //Used to build connection between chanId and coin.
    private void buildConnection(Bitfinex msg) {
        Coin coin = Bitfinex.fromSymbol(msg.symbol);
        if (coin != null) {
            coinMenu.buildConnection(coin, msg.chanId);
        }
    }

//    private void updateCoin( ) {
//        coinMenu.updateCoin();
//
//    }
}
