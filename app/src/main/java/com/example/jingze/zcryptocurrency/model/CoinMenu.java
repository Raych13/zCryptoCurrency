package com.example.jingze.zcryptocurrency.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;

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
    public Directory<Integer> directory;
    boolean orderChangedLock;
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
        this.directory = new Directory<>();
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

    //
    public void updateCoinPositionToCoin() {
        for (int i = 0; i < data.size(); i++) {
            data.get(i).setPositionInMenu(i);
        }
    }

    //Data Operations
    public void buildConnection(Coin coin, Integer chanId) {
        int position = getPosition(coin.getCoinType(), coin.getCurrencyType());
        if (position != -1 && position < data.size()) {
            if (directory == null) {
                directory = new Directory<>();
            }
            directory.write(data.get(position), chanId);
        }
    }

    public void updateCoin(int chanId, final Coin newCoinData) {
        Log.i("Raych", "chanId: " + chanId + " is updated.");
        Coin coinToBeUpdate = directory.getCoin(chanId);
        if (coinToBeUpdate != null) {
            coinToBeUpdate.setPriceAndRate(newCoinData.getPrice(), newCoinData.getDailyChangeRate());
            if (onChangeListener != null) {
                Integer position = coinToBeUpdate.getPositionInMenu();
                if (position != null) {
                    onChangeListener.onCoinChanged(position);
                }
            }
        } else {
            Log.e("Raych", "CoinMenu.updateCoin(), Coin: "
                    + newCoinData.getCoinType()
                    + "-" + newCoinData.getCurrencyType() + " is Not Found.");
        }
    }

    public void heartbeatting(int chanId) {
        Log.i("Raych", "chanId: " + chanId + " is updated.");
        Coin coinToBeUpdate = directory.getCoin(chanId);
        if (onChangeListener != null) {
            Integer position = coinToBeUpdate.getPositionInMenu();
            if (position != null) {
                onChangeListener.onCoinChanged(position);
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
