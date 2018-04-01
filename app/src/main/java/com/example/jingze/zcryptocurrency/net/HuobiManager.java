package com.example.jingze.zcryptocurrency.net;

import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.example.jingze.zcryptocurrency.model.Coin;
import com.example.jingze.zcryptocurrency.model.CoinMenu;
import com.example.jingze.zcryptocurrency.net.base.BourseActivityManager;
import com.example.jingze.zcryptocurrency.utils.GZipUtils;
import com.example.jingze.zcryptocurrency.utils.ModelUtils;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

import okio.ByteString;

/**
 * Created by Jingze HUANG on Mar.26, 2018.
 */

public class HuobiManager extends BourseActivityManager {

    public static class Event {
        public Long ping;
        public Long pong;
        public Long ts;
        public String status;
        @SerializedName(value = "errorCode", alternate = {"err-code"})
        public String errorCode;
        @SerializedName(value = "errorMsg", alternate = {"err-msg"})
        public String errorMsg;
        @SerializedName(value = "req", alternate = {"rep",  "ch"})
        public String req;
        public String subbed;
        public String id;
        public HuobiCoin tick;
        public ArrayList<HuobiCoin> data;


        public static class HuobiCoin {
            public Double open;
            public Double close;
        }

        //Constructor
        public Event(){}

        private Event(Builder builder) {
            this.ping = builder.ping;
            this.pong = builder.pong;
            this.req = builder.req;
            this.id = builder.id;
        }


        public static class Builder{
            private String event;
            private Long ping;
            private Long pong;
            private Long ts;
            private String status;
            @SerializedName(value = "errorCode", alternate = {"err-code"})
            private String errorCode;
            @SerializedName(value = "errorMsg", alternate = {"err-msg"})
            private String errorMsg;
            @SerializedName(value = "req", alternate = {"rep", "ch"})
            private String req;
            private String id;
            private ArrayList<HuobiCoin> tick;

            public Builder setPing(Long ping) {
                this.ping = ping;
                return this;
            }

            public Builder setPong(Long pong) {
                this.pong = pong;
                return this;
            }

            public Builder setReq(String req) {
                this.req = req;
                return this;
            }

            public Builder setId(String id) {
                this.id = id;
                return this;
            }

            public Event build() {
                return new Event(this);
            }
        }
    }

    public static class CHANNEL {
        public static final String KLINE = ".kline";
        public static final String MARKET_DEPTH = ".depth";
        public static final String TRADE_DETAIL = ".trade.detail";
        public static final String MARKET_DETAIL = ".detail";
    }

    public HuobiManager(Looper dataThreadLooper, CoinMenu coinMenu) {
        super(dataThreadLooper, coinMenu);
        defaultChannel = CHANNEL.MARKET_DETAIL;
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
                coinMenu.postResetCheckListRunnable(dataThreadHandler);
            }
        }
        dataThreadHandler.post(new StartConnection());
        dataThreadHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                webManager.send("{\"ping\":1522618035242}");
            }
        }, 500);
    }

    @Override
    public void subscribe(Coin coin) {
        subscribe(defaultChannel, coin, 0);
    }

    @Override
    public void subscribe(final String channel, final Coin coin, int delay) {
        class SubscribeRunnable implements Runnable{
            @Override
            public void run() {
                String subscribeJson = coinToSub(channel, coin);
                webManager.send(subscribeJson);
            }
        }
        dataThreadHandler.postDelayed(new SubscribeRunnable(), delay);
    }

    @Override
    public void unsubscribe(String channel, Coin coin) {

    }

    @Override
    public void stop() {

    }

    @Override
    protected boolean callbackLogic(Message message) {
        if (message.what == WebSocketManager.MESSAGE_TYPE_BYTES) {
            Event newEvent = parseData(message);
            if (newEvent.tick != null) {
                Log.v("Raych", "HuobiManager.callbackLogic(): Get a Huobi.Event(Update)." + newEvent.tick);
                update(newEvent);
            } else if (newEvent.ping != null) {
//                Log.i("Raych", "HuobiManager.callbackLogic(): Get a Huobi.Event(HeartBeating).");
                Event heartbeatResponse = new Event.Builder().setPong(newEvent.ping).build();
                String heartbeatResponseJson = ModelUtils.toString(heartbeatResponse, new TypeToken<Event>(){});
                webManager.send(heartbeatResponseJson);
                if (isFirstStart) {
                    subscribeAllCoins();
                    isFirstStart = false;
                }
            } else if (newEvent.subbed != null) {
                Log.v("Raych", "HuobiManager.callbackLogic(): Get a Huobi.Event(Subscribed)." + newEvent.subbed);
                subscribed(newEvent);
            } else if(newEvent.pong != null){
                Log.d("Raych", "HuobiManager.callbackLogic(): Get a Huobi.Event(Pong).");
                if (isFirstStart) {
                    subscribeAllCoins();
                    isFirstStart = false;
                }
            } else if (newEvent.data != null) {
                Log.v("Raych", "HuobiManager.callbackLogic(): Get a Huobi.Event(Reply)." + newEvent.data);

            } else if (newEvent.errorCode != null) {
                Log.v("Raych", "HuobiManager.callbackLogic(): Get a Huobi.Event(Error)." + newEvent.errorMsg);
            }
        }
        return false;
    }

    //Private Methods
    private Event parseData(Message message) {
        String str = GZipUtils.uncompressToString(((ByteString) message.obj).toByteArray());
        Log.i("RaychTest", "Message: " + str);
        Event newEvent = null;
        try {
            newEvent = ModelUtils.toObject(str, new TypeToken<Event>(){});
        }catch (Exception huobiEventParseException) {
            Log.e("Raych", "There is exception with HuobiEventParse()" );
        }
        return newEvent;
    }

    //Used to build connection between chanId and coin.
    private void subscribed(Event newEvent) {
        Coin coin = coinFromSymbol(newEvent.subbed);
        int id = idFromSymbol(newEvent.subbed);
        if (coin != null && id != -1) {
            pairCoinWithId(coin, id);
        }
    }

    private void subscribeAllCoins() {
        for (int i = 0; i < coinMenu.size(); i++) {
            subscribe(defaultChannel, coinMenu.getCoin(i), 0);
//            subscribe(CHANNEL.TRADE_DETAIL, coinMenu.getCoin(i));
//            subscribe(CHANNEL.MARKET_DETAIL, coinMenu.getCoin(i), i * 130);
        }
    }

//    public String coinToRep(String channel, Coin coin) {
//////        return ("{\"sub\":\"market." + coin.getCoinType().toLowerCase()
//////                + coin.getCurrencyType().toLowerCase()
//////                + channel + ".1min\",\"id\":\"id1" + coin.getPositionInList().toString() + "\"}");
////                return ("{\n\t\"req\": \"market." + coin.getCoinType().toLowerCase()
////                + coin.getCurrencyType().toLowerCase()
////                + channel + ".1day\",\n\t\"id\": \"id1" + coin.getPositionInList().toString() + "\"\n}");
//        return ("market." + coin.getCoinType().toLowerCase()
//                + coin.getCurrencyType().toLowerCase()
//                + channel + ".1min");
//    }

    private String coinToSub(String channel, Coin coin) {
        String sub;
        if (channel.hashCode() == CHANNEL.KLINE.hashCode()) {
            sub = ("{\n\t\"sub\": \"market." + coin.getCoinType().toLowerCase()
                    + coin.getCurrencyType().toLowerCase()
                    + channel + ".1day\",\n\t\"id\": \"id1"
                    + coin.getPositionInList().toString() + "\"\n}");
        } else {
            sub = ("{\n\t\"sub\": \"market." + coin.getCoinType().toLowerCase()
                    + coin.getCurrencyType().toLowerCase()
                    + channel + "\",\n\t\"id\": \"id1"
                    + coin.getPositionInList().toString() + "\"\n}");
        }
        Log.i("RaychTest", sub);
        return sub;
    }

    private static Coin coinFromSymbol(String symbol) {
        Coin newCoin = null;
        int index = 15;
        for (; index > 6; index--){
            if (symbol.charAt(index) == '.') {
                break;
            }
        }
        if (index == 15) {
            String coinType = symbol.substring(7, 11);
            String currencyType = symbol.substring(11,15);
            newCoin = new Coin.Builder(coinType, currencyType).build();
        } else if (index == 14) {
            String coinType = symbol.substring(7, 10);
            String currencyType = symbol.substring(10,14);
            newCoin = new Coin.Builder(coinType, currencyType).build();
        }
        return  newCoin;
    }

    private Integer idFromSymbol(String symbol) {
        int index = 15;
        for (; index > 6; index--){
            if (symbol.charAt(index) == '.') {
                break;
            }
        }
        if (index == 15) {
            String namePair = symbol.substring(7, 15);
            return namePair.hashCode();
        } else if (index == 14) {
            String namePair = symbol.substring(7, 14);
            return namePair.hashCode();
        }
        return -1;
    }

    private void update(Event newEvent) {
        int id = idFromSymbol(newEvent.req);
        double priceClose = newEvent.tick.close;
        double priceOpen = newEvent.tick.open;
        double changeRate = (priceClose - priceOpen) * 100 / priceOpen;
        Coin newCoinData = new Coin();
        newCoinData.setPriceAndChangeRate(priceClose, changeRate);
        updateCoin(id, newCoinData);
    }
}
