package com.example.jingze.zcryptocurrency.net;

import android.content.Context;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.example.jingze.zcryptocurrency.model.Coin;
import com.example.jingze.zcryptocurrency.model.CoinMenu;
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


    public HuobiManager(Context context, Looper dataThreadLooper, CoinMenu coinMenu) {
        super(context, dataThreadLooper, coinMenu);
    }

    @Override
    public void startConnection() {

    }

    @Override
    public void subscribe(String channel, Coin coin) {

    }

    @Override
    public void unsubscribe(String channel, Coin coin) {

    }

    @Override
    public void stop() {

    }

    @Override
    boolean handlerLogic(Message message) {
        if (message.what == WebSocketManager.MESSAGE_TYPE_TEXT) {
        } else if (message.what == WebSocketManager.MESSAGE_TYPE_BYTES) {

        }
        return false;
    }

    private Event parseData(Message message) {
        String str = GZipUtils.uncompressToString(((ByteString) message.obj).toByteArray());
        Event newEvent = null;
        try {
            newEvent = ModelUtils.toObject(str, new TypeToken<Event>(){});
        }catch (Exception huobiEventParseException) {
            Log.e("Raych", "There is exception with HuobiEventParse()" );
        }
        return newEvent;
    }

}
