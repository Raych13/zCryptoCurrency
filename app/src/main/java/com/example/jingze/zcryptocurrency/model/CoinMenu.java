package com.example.jingze.zcryptocurrency.model;

import android.os.Handler;
import android.os.Parcel;
import android.os.Parcelable;
import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;

/**
 * Created by Jingze HUANG on Mar.22, 2018.
 */

public class CoinMenu implements Parcelable{
    private static final int minUpdateMinSpan = 1000;
    private static int updateMinSpan = minUpdateMinSpan;

    @Expose
    private String name;
    @Expose
    private String url;
    @Expose
    private ArrayList<Coin>  data;
    boolean orderChangedLock;
    //true: data has been updated, it's unable to update again.
    private boolean[] checkList;
    private OnChangeListener onChangeListener;

    public interface OnChangeListener {
        void onCoinChanged(int position);
        void onDataOrderChanged();
    }

    //Constructors
    public CoinMenu(String name, String url) {
        this(name, url, new ArrayList<Coin>());
    }

    public CoinMenu(String name, String url, ArrayList data) {
        this.name = name;
        this.url = url;
        this.data = data;
        orderChangedLock = false;
        updateCoinPositionInList();
    }

    //Getters and Setters
    public static void setUpdateMinSpan(int updateMinSpan) {
        CoinMenu.updateMinSpan = updateMinSpan;
    }

    public ArrayList<Coin> getData() {
        return data;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public Coin getCoin(int position) {
        return data.get(position);
    }

    public synchronized boolean setCoin(int position, Coin coin) {
        if (!orderChangedLock && coin != null) {
            data.set(position, coin);
            if (onChangeListener != null) {
                onChangeListener.onCoinChanged(position);
            }
            return true;
        }
        return false;
    }

    //Operations of List
    public synchronized boolean delete(int position) {
        if (!orderChangedLock) {
            orderChangedLock = true;
            data.remove(position);
            if (onChangeListener != null) {
                onChangeListener.onDataOrderChanged();
            }
            return true;
        }
        return false;
    }

    public synchronized boolean resetDeletingLock() {
        orderChangedLock = false;
        return orderChangedLock;
    }

    public int size() {
        return data.size();
    }

    //Data Operations
    public synchronized void initiate() {
        checkList = new boolean[data.size()];
        updateCoinPositionInList();
    }

    public synchronized void updateCoinPositionInList() {
        for (int i = 0; i < data.size(); i++) {
            data.get(i).setPositionInList(i);
        }
    }

    public void updateCoin(@NonNull final Coin coinToBeUpdated, @NonNull final Coin newCoinData) {
        coinToBeUpdated.setPriceAndChangeRate(newCoinData.getPrice(), newCoinData.getDailyChangeRate());
        if (onChangeListener != null) {
            Integer position = coinToBeUpdated.getPositionInList();
            if (position != null && !checkList[position]) {
                onChangeListener.onCoinChanged(position);
                checkList[position] = true;
            }
        }
    }

    //setOnChangeListener
    public void setOnChangeListener(OnChangeListener onChangeListener) {
        this.onChangeListener = onChangeListener;
    }

    //Parcelable
    protected CoinMenu(Parcel in) {
        name = in.readString();
        url = in.readString();
        data = in.createTypedArrayList(Coin.CREATOR);
        orderChangedLock = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(url);
        dest.writeTypedList(data);
        dest.writeByte((byte) (orderChangedLock ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<CoinMenu> CREATOR = new Creator<CoinMenu>() {
        @Override
        public CoinMenu createFromParcel(Parcel in) {
            return new CoinMenu(in);
        }

        @Override
        public CoinMenu[] newArray(int size) {
            return new CoinMenu[size];
        }
    };

    //Private Methods
    public int getCoinPositionInListByType(String coinType, String currencyType) {
        int cursor = 0;
        for (; cursor < data.size(); cursor++) {
            Coin cursorCoin = data.get(cursor);
            if (coinType.equals(cursorCoin.getCoinType()) && currencyType.equals(cursorCoin.getCurrencyType())) {
                return cursor;
            }
        }
        return -1;
    }

    public void resetCheckList() {
        for (int i = 0; i < checkList.length; i++) {
            checkList[i] = false;
        }
    }

    public void postResetCheckListRunnable(final Handler handler) {
        final class ResetCheckListRunnable implements Runnable{
            @Override
            public void run() {
                resetCheckList();
                postResetCheckListRunnable(handler);
            }
        }
        handler.postDelayed(new ResetCheckListRunnable(), updateMinSpan);
    }
}
