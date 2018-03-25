package com.example.jingze.zcryptocurrency.net;

import android.util.Log;

import com.example.jingze.zcryptocurrency.model.Coin;
import com.example.jingze.zcryptocurrency.utils.ModelUtils;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

/**
 * Created by Jingze HUANG on Mar.23, 2018.
 */

public class Bitfinex {
    public static class EVENT {
        public static final String INFO = "info";
        public static final String SUBSCRIBE = "subscribe";
        public static final String UNSUBSCRIBE = "unsubscribe";
        public static final String SUBSCRIBED = "subscribed";
        public static final String UPDATE = "update";
        public static final String HEARTBEATTING = "hb";
        public static final String ERROR = "error";
    }
    public static class CHANNEL {
        public static final String TICKER = "ticker";
        public static final String BOOKS = "books";
    }

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
    public String flags;
    public Coin coin;

    //Constructor
    public Bitfinex(){};

    private Bitfinex(Bitfinex.Builder builder) {
        this.event = builder.event;
        this.channel = builder.channel;
        this.chanId = builder.chanId;
        this.symbol = builder.symbol;
    }

    //Public Methods
    public static Bitfinex parseData(String data) {
        Bitfinex newEvent = null;
        if ((data.charAt(0) == '{') && (data.charAt(data.length() - 1) == '}')) {
            try {
                newEvent = ModelUtils.toObject(data, new TypeToken<Bitfinex>(){});
            } catch (Exception bitfinexEventParseException) {
                Log.e("Raych", "There is exception with BitfinexEventParse(): ObjectParse" );
            }
        } else if ((data.charAt(0) == '[') && (data.charAt(data.length() - 1) == ']')){
            ArrayList<Object> dataArray = null;
            try {
                dataArray = ModelUtils.toObject(data,  new TypeToken<ArrayList<Object>>(){});
            } catch (Exception bitfinexDataArrayParseException) {
                Log.e("Raych", "There is exception with BitfinexEventParse(): ArrayParse");
            }
            if (dataArray != null) {
                newEvent = new Bitfinex();
                newEvent.chanId = ((Double) dataArray.get(0)).intValue();
                if (dataArray.get(1) instanceof ArrayList) {
                    newEvent.event = EVENT.UPDATE;
                    ArrayList<Object> msgArray = (ArrayList<Object>) dataArray.get(1);
                    Coin newCoin = new Coin();
                    if (!(msgArray.get(0) instanceof ArrayList)) {
                        newCoin.setDailyChangeRate((Double)msgArray.get(5));
                        newCoin.setPrice((Double)msgArray.get(6));
                    }
                    newEvent.coin = newCoin;
                    //Reserve for future.
                } else {
                    newEvent.event = EVENT.HEARTBEATTING;
                }
            }
        }
        return newEvent;
    }

    public static String toSymbol(Coin coin) {
        String str = "t" + coin.getCoinType() + coin.getCurrencyType();
        return str;
    }

    public static Coin fromSymbol(String symbol) {
        Coin coin = null;
        if (symbol != null && symbol.length() == 7) {
            String coinType = symbol.substring(1, 4);
            String currencyType = symbol.substring(4, 7);
            coin = new Coin.Builder(coinType, currencyType).build();
        }
        return coin;
    }

    public static Runnable subscibeRunnable(final WebSocketManager webSocketManager, final String channel, final Coin coin) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Bitfinex subscribeEvent = new Bitfinex.Builder()
                        .setEvent(Bitfinex.EVENT.SUBSCRIBE)
                        .setChannel(channel)
                        .setSymbol(Bitfinex.toSymbol(coin))
                        .build();
                String subscribeJson = ModelUtils
                        .toStringSpecified(ModelUtils.GSON_BITFINEX,
                                subscribeEvent,
                                new TypeToken<Bitfinex>(){});
                webSocketManager.sendMessage(subscribeJson);
            }
        };
        return runnable;
    }

    public static Runnable unsubscibeRunnable(final WebSocketManager webSocketManager, final Integer chanId) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Bitfinex subscribeEvent = new Bitfinex.Builder()
                        .setEvent(Bitfinex.EVENT.UNSUBSCRIBE)
                        .setChanId(chanId)
                        .build();
                String subscribeJson = ModelUtils.toString(subscribeEvent, new TypeToken<Bitfinex>(){});
                webSocketManager.sendMessage(subscribeJson);
            }
        };
        return runnable;
    }

    public class PlatformInfo {
        private Integer status;
    }

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

        public Bitfinex build() {
            return new Bitfinex(this);
        }
    }
}
