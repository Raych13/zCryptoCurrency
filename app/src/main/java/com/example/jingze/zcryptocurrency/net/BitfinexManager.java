package com.example.jingze.zcryptocurrency.net;

import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.example.jingze.zcryptocurrency.model.Coin;
import com.example.jingze.zcryptocurrency.model.CoinMenu;
import com.example.jingze.zcryptocurrency.net.base.BourseActivityManager;
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

    public BitfinexManager(Looper dataThreadLooper, CoinMenu coinMenu) {
        super(dataThreadLooper, coinMenu);
        defaultChannel = CHANNEL.TICKER;
    }

    @Override
    public void startConnection() {
        class StartConnection implements Runnable{
            @Override
            public void run() {
                //position may be changed.
                coinMenu.initiate();

                String url = coinMenu.getUrl();
                webManager= new WebSocketManager
                        .Builder(mainThreadHandler, dataThreadHandler)
                        .url(url)
                        .isNeedReconnect(true)
                        .build();
                webManager.startConnect();
            }
        }
        dataThreadHandler.post(new StartConnection());
    }

    @Override
    public void subscribe(Coin coin) {
        subscribe(defaultChannel, coin, 0);
    }

    @Override
    public void subscribe(final String channel, final Coin coin, int delay) {
        class SubscribeRunnable implements Runnable {
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
                webManager.send(subscribeJson);
            }
        }
        dataThreadHandler.postDelayed(new SubscribeRunnable(), delay);
    }

    @Override
    public void unsubscribe(String channel, final Coin coin) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Integer chanId = directory.getId(coin);
                Event unsubscribeEvent = new Event.Builder()
                        .setEvent(EVENT_TYPE.UNSUBSCRIBE)
                        .setChanId(chanId)
                        .build();
                String unsubscribeJson = ModelUtils
                        .toStringSpecified(ModelUtils.GSON_BITFINEX_EVENT,
                                unsubscribeEvent,
                                new TypeToken<Event>(){});
                webManager.send(unsubscribeJson);
            }
        };
        dataThreadHandler.post(runnable);
    }

    @Override
    public void stop() {
//        unsubscribeAllCoins();
        webManager.stopConnect();
    }

    @Override
    protected boolean callbackLogic(Message message) {
        if (message.what == WebSocketManager.MESSAGE_TYPE_TEXT) {
            Event newMsg = parseData((String) message.obj);
            switch (newMsg.event) {
                case EVENT_TYPE.HEARTBEATTING: {
//                    Log.v("Raych", "BitfinexManager.callbackLogic(): Get a Bitfinex.Event(HeartBeating): ");
//                        coinMenu.heartbeatting(newMsg.chanId);
                    heartbeating(newMsg.chanId);
                    break;
                }
                case EVENT_TYPE.UPDATE: {
                    Log.v("Raych", "BitfinexManager.callbackLogic(): Get a Bitfinex.Event(Update): ");
                    if (newMsg.coin != null) {
                        updateCoin(newMsg.chanId, newMsg.coin);
                    }
                    break;
                }
                case EVENT_TYPE.SUBSCRIBED: {
                    Log.v("Raych", "BitfinexManager.callbackLogic(): Get a Bitfinex.Event(Subscribed): ");
                    subscribed(newMsg);
                    break;

                }
                case EVENT_TYPE.INFO: {
                    Log.v("Raych", "BitfinexManager.callbackLogic(): Get a Bitfinex.Event(Info): ");
                    if (isFirstStart) {
                        subscribeAllCoins();
                        postFailureListCheckRunnable();
                        isFirstStart = false;
                    }
                    break;
                }
                case EVENT_TYPE.UNSUBSCRIBED: {
                    Log.v("Raych", "BitfinexManager.callbackLogic(): Get a Bitfinex.Event(Subscribed): ");
                    directory.remove(newMsg.chanId);
                    break;
                }
                case EVENT_TYPE.ERROR: {
                    Log.v("Raych", "BitfinexManager.callbackLogic(): Get a Bitfinex.Event(Error): ");
                    break;
                }
                default: {
                    Log.v("Raych", "BitfinexManager.callbackLogic(): Get a Bitfinex.Event(Unknown): ");
                }
            }
        }
//        else if (message.what == WebSocketManager.MESSAGE_TYPE_BYTES) {
//
//        }
        return false;
    }    

    //Private Methods
    private Event parseData(String data) {
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
                        newCoin.setDailyChangeRate((Double)msgArray.get(5) * 100);
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
    private void subscribed(Event newEvent) {
        Coin coin = coinFromSymbol(newEvent.symbol);
        if (coin != null) {
            pairCoinWithId(coin, newEvent.chanId);
        }
    }

    private void subscribeAllCoins() {
        for (int i = 0; i < coinMenu.size(); i++) {
            subscribe(defaultChannel, coinMenu.getCoin(i), i * 130);
        }
    }

    private void unsubscribeAllCoins() {
        for (int i = 0; i < coinMenu.size(); i++) {
            unsubscribe(CHANNEL.TICKER, coinMenu.getCoin(i));
        }
    }

    private void heartbeating(Integer id) {
        Coin coinToBeUpdated = directory.getCoin(id);
        if (coinToBeUpdated != null) {
            int position = coinToBeUpdated.getPositionInList();
            if (failureList[position]) {
                failureList[position] = false;
            }
        } else {
            Log.e("Raych", "BourseActivityManager.updateCoin(): The id "
                    + id + " didn't find the coin.(HeartBeating)");
        }
    }

    private String coinToSymbol(Coin coin) {
        return "t" + coin.getCoinType() + coin.getCurrencyType();
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
