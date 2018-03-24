package com.example.jingze.zcryptocurrency;

import com.example.jingze.zcryptocurrency.model.Coin;
import com.example.jingze.zcryptocurrency.model.CoinMenu;
import com.example.jingze.zcryptocurrency.utils.ModelUtils;
import com.google.gson.reflect.TypeToken;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {

    @Test
    public void addition_isCorrect() throws Exception {
//        String jsonCoin = "{\"coinType\":\"BTC\",\"currencyType\":\"USD\",\"price\":13000.13,\"dailyChange\":1.3333}";
//        Coin coin = ModelUtils.toObject(jsonCoin, new TypeToken<Coin>(){});
//        Coin coin = new Coin.Builder("BTC", "USD").setPrice(13000.13).setDailyChange(0.133).build();
//        Coin coin = new Coin();
//        String jsonCoin = ModelUtils.toString(coin, new TypeToken<Coin>(){});



//        String jsonListList = "[{\"name\":\"favorite\",\"url\":\"wss://api.bitfinex.com/ws/2\",\"data\":[{\"coinType\":\"BTC\",\"currencyType\":\"USD\",\"priceUSD\":13000.13,\"dailyChange\":0.3333},{\"coinType\":\"ETH\",\"currencyType\":\"USD\",\"priceUSD\":1333.13,\"dailyChange\":0.1313},{\"coinType\":\"LTC\",\"currencyType\":\"USD\",\"priceUSD\":393.13,\"dailyChange\":0.3131}]},{\"name\":\"Bitfinex\",\"url\":\"wss://api.bitfinex.com/ws/2\",\"data\":[{\"coinType\":\"BTC\",\"currencyType\":\"USD\",\"priceUSD\":13000.13,\"dailyChange\":0.3333},{\"coinType\":\"ETH\",\"currencyType\":\"USD\",\"priceUSD\":1333.13,\"dailyChange\":0.1313},{\"coinType\":\"LTC\",\"currencyType\":\"USD\",\"priceUSD\":393.13,\"dailyChange\":0.3131},{\"coinType\":\"NEM\",\"currencyType\":\"USD\",\"priceUSD\":0.31,\"dailyChange\":-0.13},{\"coinType\":\"ETC\",\"currencyType\":\"USD\"},{\"coinType\":\"XRP\",\"currencyType\":\"USD\",\"priceUSD\":0.74,\"dailyChange\":0.0}]}]";
//        String jsonCoinMenu = "{\"name\":\"Bitfinex\",\"url\":\"wss://api.bitfinex.com/ws/2\",\"data\":[{\"coinType\":\"BTC\",\"currencyType\":\"USD\",\"price\":13000.13,\"dailyChange\":0.133}]}";

//        CoinMenu coinMenu = new CoinMenu("");
//        CoinMenu coinMenu = ModelUtils.toObject(jsonCoinMenu, new TypeToken<CoinMenu>(){});

//        String jsonCoinMenuList = "[{\"name\":\"Favorite\",\"url\":\"wss://api.bitfinex.com/ws/2\",\"data\":[{\"coinType\":\"BTC\",\"currencyType\":\"USD\",\"priceUSD\":13000.13,\"dailyChange\":1.3333},{\"coinType\":\"ETH\",\"currencyType\":\"USD\",\"priceUSD\":1333.13,\"dailyChange\":1.1313},{\"coinType\":\"LTC\",\"currencyType\":\"USD\",\"priceUSD\":393.13,\"dailyChange\":1.3131}]},{\"name\":\"Bitfinex\",\"url\":\"wss://api.bitfinex.com/ws/2\",\"data\":[{\"coinType\":\"BTC\",\"currencyType\":\"USD\",\"priceUSD\":13000.13,\"dailyChange\":1.3333},{\"coinType\":\"ETH\",\"currencyType\":\"USD\",\"priceUSD\":1333.13,\"dailyChange\":1.1313},{\"coinType\":\"LTC\",\"currencyType\":\"USD\",\"priceUSD\":393.13,\"dailyChange\":1.3131},{\"coinType\":\"BTC\",\"currencyType\":\"USD\",\"priceUSD\":13000.13,\"dailyChange\":1.3333},{\"coinType\":\"ETH\",\"currencyType\":\"USD\",\"priceUSD\":1333.13,\"dailyChange\":1.1313},{\"coinType\":\"LTC\",\"currencyType\":\"USD\",\"priceUSD\":393.13,\"dailyChange\":1.3131}]}";
        String jsonCoinMenuList = "[{\"name\":\"Favorite\",\"url\":\"wss://api.bitfinex.com/ws/2\",\"data\":[{\"coinType\":\"BTC\",\"currencyType\":\"USD\",\"price\":13000.13,\"dailyChange\":1.3333},{\"coinType\":\"ETH\",\"currencyType\":\"USD\",\"price\":1333.13,\"dailyChange\":1.1313},{\"coinType\":\"LTC\",\"currencyType\":\"USD\",\"price\":393.13,\"dailyChange\":1.3131}]},{\"name\":\"Bitfinex\",\"url\":\"wss://api.bitfinex.com/ws/2\",\"data\":[{\"coinType\":\"BTC\",\"currencyType\":\"USD\",\"price\":13000.13,\"dailyChange\":1.3333},{\"coinType\":\"ETH\",\"currencyType\":\"USD\",\"price\":1333.13,\"dailyChange\":1.1313},{\"coinType\":\"LTC\",\"currencyType\":\"USD\",\"price\":393.13,\"dailyChange\":1.3131},{\"coinType\":\"BTC\",\"currencyType\":\"USD\",\"price\":13000.13,\"dailyChange\":1.3333},{\"coinType\":\"ETH\",\"currencyType\":\"USD\",\"price\":1333.13,\"dailyChange\":1.1313},{\"coinType\":\"LTC\",\"currencyType\":\"USD\",\"price\":393.13,\"dailyChange\":1.3131}]}]";
        ArrayList<CoinMenu> coinMenuArrayList = ModelUtils.toObject(jsonCoinMenuList, new TypeToken<ArrayList<CoinMenu>>(){});

//        String one = jsonCoin;

        assertEquals(4, 2 + 2);
    }
}