package com.example.jingze.zcryptocurrency.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Jingze HUANG on Mar.22, 2018.
 */

public class CoinMenu implements Parcelable{
    @Expose
    private String name;
    @Expose
    private String url;
    @Expose
    private ArrayList<Coin>  data;
    private Map<String, Coin> stubByCode;
//    private Map<Integer, String> stubFromCoin;
    boolean orderChangedLock;
    private OnChangeListener onChangeListener;

    interface OnChangeListener {
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
        stubByCode = new HashMap<>();
        assignPositionToCoin();
    }

    //Getters and Setters
    public ArrayList<Coin> getData() {
        return data;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public Coin get(int position) {
        return data.get(position);
    }

    public synchronized boolean set(int position, Coin coin) {
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

    //Operations of StubFromCode
    public boolean record(String code, String coinType, String currencyType) {
        int position = getPosition(coinType, currencyType);
        if (position != -1) {
            stubByCode.put(code, data.get(position));
            return true;
        }
        return false;
    }

    public Coin getCoinByCode(String code) {
        return stubByCode.get(code);
    }

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

    private int getPosition(String coinType, String currencyType) {
        int cursor = 0;
        for (; cursor < data.size(); cursor++) {
            Coin cursorCoin = data.get(cursor);
            if (coinType.equals(cursorCoin.getCoinType()) && currencyType.equals(cursorCoin.getCurrencyType())) {
                return cursor;
            }
        }
        return -1;
    }

    private void assignPositionToCoin() {
        for (int i = 0; i < data.size(); i++) {
            data.get(i).setPositionInMenu(i);
        }
    }
}
