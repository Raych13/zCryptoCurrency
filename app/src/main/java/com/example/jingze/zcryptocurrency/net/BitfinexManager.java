package com.example.jingze.zcryptocurrency.net;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.example.jingze.zcryptocurrency.model.Coin;
import com.example.jingze.zcryptocurrency.model.CoinMenu;
import com.example.jingze.zcryptocurrency.utils.ModelUtils;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

/**
 * Created by Jingze HUANG on Mar.25, 2018.
 */

public class BitfinexManager extends BourseActivityManager {

    public static class Event {
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

        //Constructors
        public Event(){};

        private Event(Builder builder) {
            this.event = builder.event;
            this.channel = builder.channel;
            this.chanId = builder.chanId;
            this.symbol = builder.symbol;
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

            public Event build() {
                return new Event(this);
            }
        }
    }
    public static class EVENT_TYPE {
        public static final String INFO = "info";
        public static final String SUBSCRIBE = "subscribe";
        public static final String SUBSCRIBED = "subscribed";
        public static final String UNSUBSCRIBE = "unsubscribe";
        public static final String UNSUBSCRIBED = "unsubscribed";
        public static final String UPDATE = "update";
        public static final String HEARTBEATTING = "hb";
        public static final String ERROR = "error";
    }
    public static class CHANNEL {
        public static final String TICKER = "ticker";
        public static final String BOOKS = "books";
    }

    public BitfinexManager(Context context, Looper dataThreadLooper, CoinMenu coinMenu) {
        super(context, dataThreadLooper, coinMenu);
    }

    @Override
    public void startConnection() {
        dataThreadHandler.post(new Runnable() {
            @Override
            public void run() {
                //position may be changed.
                coinMenu.updateCoinPositionToCoin();

                String url = coinMenu.getUrl();
                webManager= new WebSocketManager
                        .Builder(context, mainThreadHandler, dataThreadHandler)
                        .url(url)
                        .isNeedReconnect(true)
                        .build();
                webManager.startConnect();
            }
        });

    }

    @Override
    public void subscribe(final String channel, final Coin coin) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Event subscribeEvent = (new Event.Builder())
                        .setEvent(EVENT_TYPE.SUBSCRIBE)
                        .setChannel(channel)
                        .setSymbol(coinToSymbol(coin))
                        .build();
                String subscribeJson = ModelUtils
                        .toStringSpecified(ModelUtils.GSON_BITFINEX_EVENT,
                                subscribeEvent,
                                new TypeToken<Event>(){});
                webManager.sendMessage(subscribeJson);
            }
        };
        dataThreadHandler.post(runnable);
    }

    @Override
    public void unsubscribe(String channel, final Coin coin) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Integer chanId = coinMenu.directory.getId(coin);
                Event unsubscribeEvent = new Event.Builder()
                        .setEvent(EVENT_TYPE.UNSUBSCRIBE)
                        .setChanId(chanId)
                        .build();
                String unsubscribeJson = ModelUtils
                        .toStringSpecified(ModelUtils.GSON_BITFINEX_EVENT,
                                unsubscribeEvent,
                                new TypeToken<Event>(){});
                webManager.sendMessage(unsubscribeJson);
            }
        };
        dataThreadHandler.post(runnable);
    }

    @Override
    public void stop() {
        Log.i("RaychTest", "stop() is called.");
//        unsubscribeAllCoins();
        webManager.stopConnect();
    }

    @Override
    boolean handlerLogic(Message message) {
        if (message.what == WebSocketManager.MESSAGE_TYPE_TEXT) {
            Event newMsg = parseData((String) message.obj);
            switch (newMsg.event) {
                case EVENT_TYPE.INFO: {
                    Log.i("Raych", "Get a Bitfinex event(Info): ");
                    if (isFirstStart) {
                        subscribeAllCoins();
                        isFirstStart = false;
                    }
                    break;
                }
                case EVENT_TYPE.SUBSCRIBED: {
                    Log.i("Raych", "Get a Bitfinex event(Subscribed): ");
                    buildConnectionWithCoin(newMsg);
                    break;
                }
                case EVENT_TYPE.UNSUBSCRIBED: {
                    Log.i("Raych", "Get a Bitfinex event(Subscribed): ");
                    coinMenu.directory.remove(newMsg.chanId);
                    break;
                }
                case EVENT_TYPE.ERROR: {
                    Log.i("Raych", "Get a Bitfinex event(Error): ");
                    break;
                }
                case EVENT_TYPE.UPDATE: {
                    Log.i("Raych", "Get a Bitfinex event(Update): ");
                    if (newMsg.coin != null) {
                        coinMenu.updateCoin(newMsg.chanId, newMsg.coin);
                    }
                    break;
                }
                case EVENT_TYPE.HEARTBEATTING: {
                    Log.i("Raych", "Get a Bitfinex event(HeartBeating): ");
//                        coinMenu.heartbeatting(newMsg.chanId);
                    break;
                }
                default: {
                    Log.i("Raych", "Get a Bitfinex event(Unknown): ");
                }
            }
        } else if (message.what == WebSocketManager.MESSAGE_TYPE_BYTES) {

        }
        return false;
    }    

    //Private Methods
    public Event parseData(String data) {
        Event newEvent = null;
        if ((data.charAt(0) == '{') && (data.charAt(data.length() - 1) == '}')) {
            try {
                newEvent = ModelUtils.toObject(data, new TypeToken<Event>(){});
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
                newEvent = new Event();
                newEvent.chanId = ((Double) dataArray.get(0)).intValue();
                if (dataArray.get(1) instanceof ArrayList) {
                    newEvent.event = EVENT_TYPE.UPDATE;
                    ArrayList<Object> msgArray = (ArrayList<Object>) dataArray.get(1);
                    Coin newCoin = new Coin();
                    if (!(msgArray.get(0) instanceof ArrayList)) {
                        newCoin.setDailyChangeRate((Double)msgArray.get(5));
                        newCoin.setPrice((Double)msgArray.get(6));
                    }
                    newEvent.coin = newCoin;
                    //Reserve for future.
                } else {
                    newEvent.event = EVENT_TYPE.HEARTBEATTING;
                }
            }
        }
        return newEvent;
    }

    //Used to build connection between chanId and coin.
    private void buildConnectionWithCoin(Event msg) {
        Coin coin = coinFromSymbol(msg.symbol);
        if (coin != null) {
            coinMenu.buildConnection(coin, msg.chanId);
        }
    }

    private void subscribeAllCoins() {
        for (int i = 0; i < coinMenu.size(); i++) {
            subscribe(CHANNEL.TICKER, coinMenu.getCoin(i));
        }
    }

    private void unsubscribeAllCoins() {
        for (int i = 0; i < coinMenu.size(); i++) {
            unsubscribe(CHANNEL.TICKER, coinMenu.getCoin(i));
        }
    }

    private String coinToSymbol(Coin coin) {
        String str = "t" + coin.getCoinType() + coin.getCurrencyType();
        return str;
    }

    private Coin coinFromSymbol(String symbol) {
        Coin coin = null;
        if (symbol != null) {
            if (symbol.length() == 7) {
                String coinType = symbol.substring(1, 4);
                String currencyType = symbol.substring(4, 7);
                coin = new Coin.Builder(coinType, currencyType).build();
            } else if (symbol.length() == 7){
                String coinType = symbol.substring(1, 5);
                String currencyType = symbol.substring(5, 8);
                coin = new Coin.Builder(coinType, currencyType).build();
            }
        }
        return coin;
    }
}
