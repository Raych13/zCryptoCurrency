package com.example.jingze.zcryptocurrency.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Jingze HUANG on Mar.24, 2018.
 */

public class Directory<T> {

    private Map<T, Coin> idToCoin;
    private Map<Coin, T> coinToId;

    public Directory() {
        idToCoin = new HashMap<T, Coin>();
        coinToId = new HashMap<Coin, T>();
    }

    public T getId(int coin) {
        return coinToId.get(coin);
    }

    public Coin getCoin(int id) {
        return idToCoin.get(id);
    }

    public boolean write(Coin coin, T id) {
        coinToId.put(coin, id);
        idToCoin.put(id, coin);
        return true;
    }

    public boolean remove(Coin coin) {
        T id = coinToId.get(coin);
        if (id != null) {
            coinToId.remove(coin);
            idToCoin.remove(id);
            return true;
        }
        return false;
    }

    public boolean remove(T id) {
        Coin coin = idToCoin.get(id);
        if (coin != null) {
            coinToId.remove(coin);
            idToCoin.remove(id);
            return true;
        }
        return false;
    }
}
