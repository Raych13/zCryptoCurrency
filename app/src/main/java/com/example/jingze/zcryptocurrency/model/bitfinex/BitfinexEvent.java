package com.example.jingze.zcryptocurrency.model.bitfinex;

import com.google.gson.annotations.SerializedName;

public class BitfinexEvent {
    public String event;
    public String channel;
    public Integer chanId;
    public String symbol;
    public String pair;
    public Integer version;
    public PlatformInfo platform;
    public String msg;
    @SerializedName(value = "errorCode", alternate = {"code"})
    public Integer errorCode;

    private class PlatformInfo {
        private Integer status;
    }

    public BitfinexEvent(String event, String channel, String symbol) {
        this.event = event;
        this.channel = channel;
        this.symbol = symbol;
    }

//    public static String getSubscribeReq() {
//
//    }
}
