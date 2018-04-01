package com.example.jingze.zcryptocurrency.Huobi;

import com.example.jingze.zcryptocurrency.model.Coin;
import com.example.jingze.zcryptocurrency.net.HuobiManager;
import com.example.jingze.zcryptocurrency.utils.DateUtils;
import com.example.jingze.zcryptocurrency.utils.ModelUtils;
import com.google.gson.reflect.TypeToken;

import org.junit.Test;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;

/**
 * Created by Jingze HUANG on Mar.31, 2018.
 */

public class HuobiParse {
    @Test
    public void parseDataTest() {
//        Coin coin = new Coin.Builder("BTC", "USDT").build();
//        coin.setPositionInList(0);
//        String channel = HuobiManager.CHANNEL.KLINE;
//        String req = HuobiManager.coinToRep(coin, channel);
//
//        HuobiManager.Event subscribeEvent = (new HuobiManager.Event.Builder())
//                .setReq(req)
//                .setId(coin.getPositionInList().toString())
//                .build();
//        String subscribeJson = ModelUtils
//                .toStringSpecified(ModelUtils.GSON_HUOBI_EVENT,
//                        subscribeEvent,
//                        new TypeToken<HuobiManager.Event>(){});
//
//        assertEquals(1, 1);
    }

    @Test
    public void parseDataTest2(){
        Date date = new Date(1522535231280l);
//        String str1 = DateUtils.dateToString(date);
//        String str2 = DateUtils.dateToStringDate(date);
//        String str3 = DateUtils.dateToStringTime(date);

//        String dataStr = "{\"date\":\"Mar 31, 2018 3:27:11 PM\"}";
        String dateStr = "\"Mar 31, 2018 3:27:11 PM\"";

        Date dateFromJson = ModelUtils.toObject(dateStr, new TypeToken<Date>(){});
//        String str = ModelUtils.toString(date, new TypeToken<Date>(){});

        assertEquals(1, 1);
    }

    @Test
    public void parseHuobi(){
        String str = "{\"ch\":\"market.btcusdt.detail\",\"ts\":1522565781989,\"tick\":{\"amount\":25342.944824478842366239,\"open\":6820.550000000000000000,\"close\":6836.670000000000000000,\"high\":7200.000000000000000000,\"id\":4761357465,\"count\":102008,\"low\":6780.860000000000000000,\"version\":4761357465,\"vol\":177469138.283385056553957411260000000000000000}}\n";
        HuobiManager.Event newEvent = ModelUtils.toObject(str, new TypeToken<HuobiManager.Event>(){});


        assertEquals(1, 1);
    }
}
