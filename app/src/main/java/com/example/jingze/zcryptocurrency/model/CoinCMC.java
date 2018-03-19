package com.example.jingze.zcryptocurrency.model;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * Data from CoinMarketCap
 */

public class CoinCMC {
    public String id;
    public String name;
    public String symbol;

    public int rank;

    public double price_usd;
    public double price_btc;
    @SerializedName("24h_volume_usd")
    public double daily_volume_usd;
    public double market_cap_use;
    public double available_supply;
    public float percent_change_1h;
    public float percent_change_24h;
    public float percent_change_7d;

    public Date last_updated;

}
