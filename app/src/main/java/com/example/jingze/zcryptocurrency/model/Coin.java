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
    private Integer positionInMenu;
    @Expose
    private Double price;
    @Expose
    private Double dailyChangeRate;
    private Double dailyChange;
    private OnChangedListener onChangedListener;

    public String getTitle() {
        return title;
    }

    //Interface
    public interface OnChangedListener{
        void onPriceOrRateChange(int positionInMenu);
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
        this.price = priceUSD;
        return false;
    }

    public Double getDailyChangeRate() {
        return dailyChangeRate;
    }

    public synchronized boolean setDailyChangeRate(Double dailyChangeRate) {
        this.dailyChangeRate = dailyChangeRate;
        return false;
    }

    public synchronized boolean setPriceAndRate(Double price, Double dailyChangeRate) {
        this.price = price;
        this.dailyChangeRate = dailyChangeRate;
        if (onChangedListener != null) {
            onChangedListener.onPriceOrRateChange(positionInMenu);
        }
        return true;
    }

    public Integer getPositionInMenu() {
        return positionInMenu;
    }

    public synchronized boolean setPositionInMenu(Integer positionInMenu) {
        this.positionInMenu = positionInMenu;
        return true;
    }

    public void setOnChangedListener(OnChangedListener onChangedListener) {
        this.onChangedListener = onChangedListener;
    }

    //Parcelable
    protected Coin(Parcel in) {
        coinType = in.readString();
        currencyType = in.readString();
        if (in.readByte() == 0) {
            positionInMenu = null;
        } else {
            positionInMenu = in.readInt();
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
        if (positionInMenu == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(positionInMenu);
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
