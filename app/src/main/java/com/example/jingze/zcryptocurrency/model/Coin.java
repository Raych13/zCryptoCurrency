package com.example.jingze.zcryptocurrency.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Coin implements Parcelable{
    private String title;
    @Expose
    @SerializedName(value = "coinType", alternate = {"coin"})
    private String coinType;
    @Expose
    private String currencyType;
    private Integer positionInList;
    @Expose
    private Double price;
    @Expose
    private Double dailyChangeRate;
    private Double dailyChange;
    private OnPriceChangedListener onPriceChangedListener;
    private OnDailyChangeRateChangedListener onDailyChangeRateChangedListener;

    interface OnPriceChangedListener {
        //If return true, it indicates that this Listener will be deleted after recall.
        boolean onPriceChanged(Double prevPrice, Double newPrice);
    }

    interface OnDailyChangeRateChangedListener {
        //If return true, it indicates that this Listener will be deleted after recall.
        boolean onChangeRateChanged(Double prevDailyChangeRate, Double newDailyChangeRate);
    }

   //Constructor with Builder
    public Coin(){
    }

    private Coin(Builder builder) {
        this.coinType = builder.coinType;
        this.currencyType = builder.currencyType;
        this.price = builder.price;
        this.dailyChangeRate = builder.dailyChange;
    }

    //Getters and Setters
    public String getTitle() {
        if (title == null) {
            title = "○  " + coinType + " - " + currencyType;
        }
        return title;
    }

    public String getCoinType() {
        return coinType;
    }

    public String getCurrencyType() {
        return currencyType;
    }

    public Double getPrice() {
        return price;
    }

    public synchronized boolean setPrice(Double priceUSD) {
        if (onPriceChangedListener != null) {
            if (onPriceChangedListener.onPriceChanged(this.price, price)) {
                onPriceChangedListener = null;
            }
        }
        this.price = priceUSD;
        return false;
    }

    public Double getDailyChangeRate() {
        return dailyChangeRate;
    }

    public synchronized boolean setDailyChangeRate(Double dailyChangeRate) {
        if (onDailyChangeRateChangedListener != null) {
            if (onDailyChangeRateChangedListener.onChangeRateChanged(this.dailyChangeRate, dailyChangeRate)) {
                onDailyChangeRateChangedListener = null;
            }
        }
        this.dailyChangeRate = dailyChangeRate;
        return false;
    }

    public synchronized boolean setPriceAndChangeRate(Double price, Double dailyChangeRate) {
        if (onPriceChangedListener != null) {
            if (onPriceChangedListener.onPriceChanged(this.price, price)) {
                onPriceChangedListener = null;
            }
        }
        if (onDailyChangeRateChangedListener != null) {
            if (onDailyChangeRateChangedListener.onChangeRateChanged(this.dailyChangeRate, dailyChangeRate)) {
                onDailyChangeRateChangedListener = null;
            }
        }
        this.price = price;
        this.dailyChangeRate = dailyChangeRate;
        return true;
    }

    public Integer getPositionInList() {
        return positionInList;
    }

    public synchronized boolean setPositionInList(Integer positionInList) {
        this.positionInList = positionInList;
        return true;
    }

    public void setOnPriceChangedListener(OnPriceChangedListener onPriceChangedListener) {
        this.onPriceChangedListener = onPriceChangedListener;
    }

    public void setOnDailyChangeRateChangedListener(OnDailyChangeRateChangedListener onDailyChangeRateChangedListener) {
        this.onDailyChangeRateChangedListener = onDailyChangeRateChangedListener;
    }

    //Parcelable
    protected Coin(Parcel in) {
        coinType = in.readString();
        currencyType = in.readString();
        if (in.readByte() == 0) {
            positionInList = null;
        } else {
            positionInList = in.readInt();
        }
        if (in.readByte() == 0) {
            price = null;
        } else {
            price = in.readDouble();
        }
        if (in.readByte() == 0) {
            dailyChangeRate = null;
        } else {
            dailyChangeRate = in.readDouble();
        }
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(coinType);
        dest.writeString(currencyType);
        if (positionInList == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(positionInList);
        }
        if (price == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(price);
        }
        if (dailyChangeRate == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(dailyChangeRate);
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Coin> CREATOR = new Creator<Coin>() {
        @Override
        public Coin createFromParcel(Parcel in) {
            return new Coin(in);
        }

        @Override
        public Coin[] newArray(int size) {
            return new Coin[size];
        }
    };

    public static class Builder{
        private String title;
        private String coinType;
        private String currencyType;
        private Double price;
        private Double dailyChange;

        public Builder(String coinType, String currencyType) {
            this.title =  "○  " + coinType + " - " + currencyType;
            this.coinType = coinType.toUpperCase();
            this.currencyType = currencyType.toUpperCase();
        }

        public Builder setPrice(Double price) {
            this.price = price;
            return this;
        }

        public Builder setDailyChange(Double dailyChange) {
            this.dailyChange = dailyChange;
            return this;
        }

        public Coin build() {
            return new Coin(this);
        }
    }
}
