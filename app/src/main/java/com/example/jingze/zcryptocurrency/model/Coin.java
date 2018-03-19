package com.example.jingze.zcryptocurrency.model;

import com.google.gson.annotations.SerializedName;

public class Coin {
    @SerializedName(value = "coinType", alternate = {"id"})
    public String coinType;
    public String currencyTypr;
    public Double pricaUSD;
    public Double dailyChange;
}
