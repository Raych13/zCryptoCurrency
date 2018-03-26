package com.example.jingze.zcryptocurrency.Bitfinex;

import com.example.jingze.zcryptocurrency.model.Coin;
import com.example.jingze.zcryptocurrency.net.BitfinexManager;
import com.example.jingze.zcryptocurrency.utils.ModelUtils;
import com.google.gson.reflect.TypeToken;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by Jingze HUANG on Mar.24, 2018.
 */

public class BitfinexTest {

    @Test
    public void parseDataTest() {
        String data1 = "{\"event\":\"subscribe\",\"channel\":\"ticker\",\"symbol\":\"tBTCUSD\"}";
        String data2 = "{\"event\":\"info\",\"version\":2,\"platform\":{\"status\":1}}";
        String data3 = "{\"event\":\"subscribed\",\"channel\":\"ticker\",\"chanId\":220,\"symbol\":\"tBTCUSD\",\"pair\":\"BTCUSD\"}";
        String data4 = "{\"channel\":\"ticker\",\"symbol\":\"tBTCUSD\",\"event\":\"error\",\"msg\":\"subscribe: dup\",\"code\":10301,\"pair\":\"BTCUSD\"}";
        String data5 = "[4,[7839.9,39.98836294,7840,718.34207625,-645.3,-0.076,7840,51239.69240822,8498.1,7728.1]]";
        String data6 = "[1,\"hb\"]";
        String data7 = "[5,[520,1000.82531096,520.01,155.2598692,-22.88,-0.0421,520,98244.53481982,544.14,515]]";

        BitfinexManager bitfinexManager = new BitfinexManager(null, null,null);

        BitfinexManager.Event event1 = bitfinexManager.parseData(data1);
        BitfinexManager.Event event2 = bitfinexManager.parseData(data2);
        BitfinexManager.Event event3 = bitfinexManager.parseData(data3);
        BitfinexManager.Event event4 = bitfinexManager.parseData(data4);
        BitfinexManager.Event event5 = bitfinexManager.parseData(data5);
        BitfinexManager.Event event6 = bitfinexManager.parseData(data6);
        BitfinexManager.Event event7 = bitfinexManager.parseData(data7);

//        Bitfinex event2 = Bitfinex.parseData(data2);
//        Bitfinex event3 = Bitfinex.parseData(data3);
//        Bitfinex event4 = Bitfinex.parseData(data4);
//        Bitfinex event5 = Bitfinex.parseData(data5);
//        Bitfinex event6 = Bitfinex.parseData(data6);
//        Bitfinex event7 = Bitfinex.parseData(data7);

//        String jsonEvent1 = ModelUtils.toStringSpecified(ModelUtils.GSON_BITFINEX, event1, new TypeToken<Bitfinex>(){});
//        String jsonEvent2 = ModelUtils.toStringSpecified(ModelUtils.GSON_BITFINEX, event2, new TypeToken<Bitfinex>(){});
//        String jsonEvent3 = ModelUtils.toStringSpecified(ModelUtils.GSON_BITFINEX, event3, new TypeToken<Bitfinex>(){});
//        String jsonEvent4 = ModelUtils.toStringSpecified(ModelUtils.GSON_BITFINEX, event4, new TypeToken<Bitfinex>(){});
//        String jsonEvent5 = ModelUtils.toStringSpecified(ModelUtils.GSON_BITFINEX, event5, new TypeToken<Bitfinex>(){});
//        String jsonEvent6 = ModelUtils.toStringSpecified(ModelUtils.GSON_BITFINEX, event6, new TypeToken<Bitfinex>(){});

        assertEquals(1, 1);
    }

}
