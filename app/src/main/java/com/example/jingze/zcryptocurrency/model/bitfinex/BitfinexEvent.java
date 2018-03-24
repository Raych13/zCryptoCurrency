package com.example.jingze.zcryptocurrency.model.bitfinex;

import com.example.jingze.zcryptocurrency.model.Coin;
import com.google.gson.annotations.SerializedName;

public class BitfinexEvent {
    public static final String SUBSCRIBE = "subscribe";
    public static final String UNSUBSCRIBE = "unsubscribe";
    public static final String CHANNEL_TICKER = "ticker";
    public static final String CHANNEL_BOOKS = "books";

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

    //Constructor
    private BitfinexEvent(Builder builder) {
        this.event = builder.event;
        this.channel = builder.channel;
        this.chanId = builder.chanId;
        this.symbol = builder.symbol;
    }

    //Public Methods
    public static String toSymbol(Coin coin) {
        String str = "t" + coin.getCoinType() + coin.getCurrencyType();
        return str;
    }


    private class PlatformInfo {
        private Integer status;
    }

//    public static String getSubscribeReq() {
//
//    }
    
    public static class Builder{
        private String event;
        private String channel;
        private Integer chanId;
        private String symbol;
        private String pair;
        private Integer version;
        private PlatformInfo platform;
        private String msg;

        public Builder(){
        }

        public Builder setEvent(String event) {
            this.event = event;
            return this;
        }

        public Builder setChannel(String channel) {
            this.channel = channel;
            return this;
        }

        public Builder setChanId(Integer chanId) {
            this.chanId = chanId;
            return this;
        }

        public Builder setSymbol(String symbol) {
            this.symbol = symbol;
            return this;
        }

        public Builder setPair(String pair) {
            this.pair = pair;
            return this;
        }

        public Builder setVersion(Integer version) {
            this.version = version;
            return this;
        }

        public Builder setPlatform(PlatformInfo platform) {
            this.platform = platform;
            return this;
        }

        public Builder setMsg(String msg) {
            this.msg = msg;
            return this;
        }
        
        public BitfinexEvent build() {
            return new BitfinexEvent(this);
        }
    }
}
