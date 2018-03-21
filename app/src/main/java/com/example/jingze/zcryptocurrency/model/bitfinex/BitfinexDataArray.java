package com.example.jingze.zcryptocurrency.model.bitfinex;


import com.example.jingze.zcryptocurrency.model.Coin;
import com.example.jingze.zcryptocurrency.utils.ModelUtils;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;

public class BitfinexDataArray {

    public static HashMap<Integer, Integer> redirectionMap = new HashMap<>();

    public static boolean isDataArray(String data) {
        return (data.charAt(0) == '[');
    }

    public static Coin parseDataArray(String data) {
        ArrayList<Object> dataArray = ModelUtils.toObject(data,  new TypeToken<ArrayList<Object>>(){});
        Coin newCoin = new Coin();
        Integer chanId;
        ArrayList<Double> coinData;
        try {
            chanId = ((Double) dataArray.get(0)).intValue();
            if (dataArray.get(1).getClass().equals(ArrayList.class)) {
                coinData = (ArrayList<Double>) dataArray.get(1);

            } else {
                return newCoin;
            }
        } catch (Exception bitfinexDataArrayParseException) {

        }

        return new Coin();
    }

    public static int getMenuPosition(int chanId) {
        if (redirectionMap.containsKey(chanId)) {
            return redirectionMap.get(chanId);
        }
//        int position =position
        return -1;
    }
}
