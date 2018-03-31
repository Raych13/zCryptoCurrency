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
        public String event;
        public Long ping;
        public Long pong;
        public Long ts;
        public String status;
        @SerializedName(value = "errorCode", alternate = {"err-code"})
        public String errorCode;
        @SerializedName(value = "errorMsg", alternate = {"err-msg"})
        public String errorMsg;
        public String req;
        public String id;
        public ArrayList<HuobiCoin> tick;

        public static class HuobiCoin {
            public Double amount;
            public Integer count;
            public Long id;
            public Double open;
            public Double close;
            public Double low;
            public Double high;
            public Double vol;
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
            @SerializedName(value = "req", alternate = {"rep"})
            private String req;
            private String sub;
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
        public static final String KLINE = ".kline";
        public static final String MARKET_DEPTH = ".depth";
        public static final String TRADE_DETAIL = ".trade";
        public static final String MARKET_DETAIL = ".detail";
    }


    public HuobiManager(Looper dataThreadLooper, CoinMenu coinMenu) {
        super(dataThreadLooper, coinMenu);
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
                        .Builder(mainThreadHandler, dataThreadHandler)
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
//                Event subscribeEvent = (new Event.Builder())
//                        .setReq(coinToRep(coin, channel))
//                        .setId(coin.getPositionInMenu().toString())
//                        .build();
//                String subscribeJson = ModelUtils
//                        .toStringSpecified(ModelUtils.GSON_BITFINEX_EVENT,
//                                subscribeEvent,
//                                new TypeToken<Event>(){});
                String subscribeJson = coinToRep(coin, channel);
                webManager.send(subscribeJson);
            }
        };
        dataThreadHandler.post(runnable);
    }

    @Override
    public void unsubscribe(String channel, Coin coin) {

    }

    @Override
    public void stop() {

    }

    @Override
    protected boolean handlerLogic(Message message) {
        if (message.what == WebSocketManager.MESSAGE_TYPE_TEXT) {
        } else if (message.what == WebSocketManager.MESSAGE_TYPE_BYTES) {
            Event newEvent = parseData(message);
            if (newEvent.ping != null) {
                Log.i("Raych", "HuobiManager.handlerLogic(): Get a Huobi.Event(HeartBeating).");
                Event heartbeatResponse = new Event.Builder().setPong(newEvent.ping).build();
                String heartbeatResponseJson = ModelUtils.toString(heartbeatResponse, new TypeToken<Event>(){});
                webManager.send(heartbeatResponseJson);
                if (isFirstStart) {
                    subscribeAllCoins();
                    isFirstStart = false;
                }
            }else if (newEvent.req != null) {
                Log.i("Raych", "HuobiManager.handlerLogic(): Get a Huobi.Event(Reply)." + newEvent.tick.toString());

            } else if (newEvent.errorCode != null) {

            }
        }
        return false;
    }

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

    private void subscribeAllCoins() {
        for (int i = 0; i < coinMenu.size(); i++) {
            subscribe(CHANNEL.KLINE, coinMenu.getCoin(i));
        }
    }

    private String coinToRep(Coin coin, String channel) {
//        return ("{\"sub\":\"market." + coin.getCoinType().toLowerCase()
//                + coin.getCurrencyType().toLowerCase()
//                + channel + ".1min\",\"id\":\"id1" + coin.getPositionInMenu().toString() + "\"}");
                return ("{\n\t\"req\": \"market." + coin.getCoinType().toLowerCase()
                + coin.getCurrencyType().toLowerCase()
                + channel + ".1min\",\n\t\"id\": \"id1" + coin.getPositionInMenu().toString() + "\"\n}");
    }


}
