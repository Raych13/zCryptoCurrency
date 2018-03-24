package com.example.jingze.zcryptocurrency.net;

import com.example.jingze.zcryptocurrency.model.Coin;
import com.example.jingze.zcryptocurrency.model.bitfinex.BitfinexEvent;
import com.example.jingze.zcryptocurrency.utils.ModelUtils;
import com.google.gson.reflect.TypeToken;

/**
 * Created by Jingze HUANG on Mar.23, 2018.
 */

public class Bitfinex {

    public static Runnable subscibeRunnable(final WebSocketManager webSocketManager, final String event, final Coin coin) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                BitfinexEvent subscribeEvent = new BitfinexEvent.Builder()
                        .setEvent(BitfinexEvent.SUBSCRIBE)
                        .setChannel(event)
                        .setSymbol(BitfinexEvent.toSymbol(coin))
                        .build();
                String subscribeJson = ModelUtils.toString(subscribeEvent, new TypeToken<BitfinexEvent>(){});
                webSocketManager.sendMessage(subscribeJson);
            }
        };
        return runnable;
    }


}
