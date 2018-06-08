package com.example.jingze.zcryptocurrency.net.base;

import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.example.jingze.zcryptocurrency.model.Coin;
import com.example.jingze.zcryptocurrency.model.CoinMenu;
import com.example.jingze.zcryptocurrency.model.Directory;

/**
 * Created by Jingze HUANG on Mar.25, 2018.
 */

public abstract class BourseActivityManager {

    protected Looper dataThreadLooper;
    protected Handler mainThreadHandler;
    protected Handler dataThreadHandler;
    protected CoinMenu coinMenu;
    protected Directory directory;
    protected String defaultChannel;
    protected boolean[] failureList;
    protected boolean isAutoFailureCheckEnabled = true;
    private static final int defaultAutoFailureCheckSpan = 15000;
    protected boolean isFirstStart = true;

    //Constructor
    public BourseActivityManager(Looper dataThreadLooper, CoinMenu coinMenu) {
        this.mainThreadHandler = new Handler(Looper.getMainLooper());
        this.dataThreadLooper = dataThreadLooper;
        this.coinMenu = coinMenu;
        if (coinMenu.size() > Directory.PRESET_COIN_SIZE) {
            this.directory = new Directory(coinMenu.size() * 2);
        } else {
            this.directory = new Directory();
        }
        this.isFirstStart = true;
        this.dataThreadHandler = new Handler(dataThreadLooper, new WebCallback());
        this.failureList = new boolean[coinMenu.size()];
    }

    public abstract void startConnection();

    public abstract void subscribe(Coin coin);

    public abstract void subscribe(String channel, Coin coin, int delay);

    public abstract void unsubscribe(String channel, Coin coin);

    public abstract void stop();

    protected abstract boolean callbackLogic(Message message);

    protected void inspectFailureList() {
        Log.d("Raych", this + ": BourseActivity.inspectFailureList(): is called.");
        if (!isAutoFailureCheckEnabled) {
            return;
        }

        int length = failureList.length;
        for (int i = 0; i < length; i++) {
            if (failureList[i]) {
                Log.w("Raych", this + ": BourseActivity.inspectFailureList(): There is a failure connection. PositionInList: " + i);
                subscribe(coinMenu.getCoin(i));
            } else {
                failureList[i] = true;
            }
        }
    }

    protected void postFailureListCheckRunnable() {
        postFailureListCheckRunnable(defaultAutoFailureCheckSpan);
    }

    protected boolean pairCoinWithId(Coin eigenCoin, Integer id) {
        int position = coinMenu.getCoinPositionInListByType(eigenCoin.getCoinType(), eigenCoin.getCurrencyType());
        if (position != -1 && position < coinMenu.size()) {
            directory.pair(coinMenu.getCoin(position), id);
            return true;
        }
        return false;
    }

    protected void updateCoin(Integer id, final Coin newCoinData) {
        Log.i("Raych", "id: " + id + " is updated.");
        Coin coinToBeUpdated = directory.getCoin(id);
        if (coinToBeUpdated != null) {
            int position = coinToBeUpdated.getPositionInList();
            if (failureList[position]) {
                failureList[position] = false;
            }
            coinMenu.updateCoin(coinToBeUpdated, newCoinData);
        } else {
            Log.e("Raych", "BourseActivityManager.updateCoin(): The id of "
                    + newCoinData.getCoinType()
                    + "-" + newCoinData.getCurrencyType() + " didn't find the coinToBeUpdated.");
        }
    }

    private void postFailureListCheckRunnable(int delay) {
        final class AutoCheck implements Runnable{
            @Override
            public void run() {
                inspectFailureList();
                if (isAutoFailureCheckEnabled) {
                    postFailureListCheckRunnable(defaultAutoFailureCheckSpan);
                }
            }
        }
        dataThreadHandler.postDelayed(new AutoCheck(), delay);
    }

    private final class WebCallback implements Callback{
        @Override
        public boolean handleMessage(Message message) {
            return callbackLogic(message);
        }
    }
}
