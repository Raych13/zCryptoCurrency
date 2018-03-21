package com.example.jingze.zcryptocurrency.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Coin implements Parcelable{
    @SerializedName(value = "coinType", alternate = {"id"})
    public String coinType;
    public String currencyType;
    public Double priceUSD;
    public Double dailyChange;

    public  Coin() {

    }

    protected Coin(Parcel in) {
        coinType = in.readString();
        currencyType = in.readString();
        if (in.readByte() == 0) {
            priceUSD = null;
        } else {
            priceUSD = in.readDouble();
        }
        if (in.readByte() == 0) {
            dailyChange = null;
        } else {
            dailyChange = in.readDouble();
        }
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(coinType);
        parcel.writeString(currencyType);
        if (priceUSD == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeDouble(priceUSD);
        }
        if (dailyChange == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeDouble(dailyChange);
        }
    }
}
