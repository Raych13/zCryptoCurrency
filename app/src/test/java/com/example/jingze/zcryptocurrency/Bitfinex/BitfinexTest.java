package com.example.jingze.zcryptocurrency.Bitfinex;

import com.example.jingze.zcryptocurrency.model.Coin;
import com.example.jingze.zcryptocurrency.net.Bitfinex;
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

        Bitfinex event1 = Bitfinex.parseData(data1);
        Bitfinex event2 = Bitfinex.parseData(data2);
        Bitfinex event3 = Bitfinex.parseData(data3);
        Bitfinex event4 = Bitfinex.parseData(data4);
        Bitfinex event5 = Bitfinex.parseData(data5);
        Bitfinex event6 = Bitfinex.parseData(data6);
        Bitfinex event7 = Bitfinex.parseData(data7);

        String jsonEvent1 = ModelUtils.toStringSpecified(ModelUtils.GSON_BITFINEX, event1, new TypeToken<Bitfinex>(){});
        String jsonEvent2 = ModelUtils.toStringSpecified(ModelUtils.GSON_BITFINEX, event2, new TypeToken<Bitfinex>(){});
        String jsonEvent3 = ModelUtils.toStringSpecified(ModelUtils.GSON_BITFINEX, event3, new TypeToken<Bitfinex>(){});
        String jsonEvent4 = ModelUtils.toStringSpecified(ModelUtils.GSON_BITFINEX, event4, new TypeToken<Bitfinex>(){});
        String jsonEvent5 = ModelUtils.toStringSpecified(ModelUtils.GSON_BITFINEX, event5, new TypeToken<Bitfinex>(){});
        String jsonEvent6 = ModelUtils.toStringSpecified(ModelUtils.GSON_BITFINEX, event6, new TypeToken<Bitfinex>(){});

        assertEquals(1, 1);
    }

    @Test
    public void fromSymbolTest() {
        String symbol1 = "tBTCUSD";
        String symbol2 = "tbtcusd";
        String symbol3 = "tBTCUSDa";
        String symbol4 = "tBTCUS";

        Coin coin1 = Bitfinex.fromSymbol(symbol1);
        Coin coin2 = Bitfinex.fromSymbol(symbol2);
        Coin coin3 = Bitfinex.fromSymbol(symbol3);
        Coin coin4 = Bitfinex.fromSymbol(symbol4);

        assertEquals(1, 1);
    }
}
