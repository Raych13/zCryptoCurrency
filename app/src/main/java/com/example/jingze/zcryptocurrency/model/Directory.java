package com.example.jingze.zcryptocurrency.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Jingze HUANG on Mar.24, 2018.
 */

public class Directory {

    private Map<Integer, Coin> idToCoin;
    private Map<Coin, Integer> coinToId;
    private static final int DEFAULT_INITIAL_CAPACITY = 53;
    private static final float DEFAULT_LOAD_FACTOR = 0.618f;
    public static final int PRESET_COIN_SIZE = 33;

    //Constructors
    public Directory() {
        this(DEFAULT_INITIAL_CAPACITY, DEFAULT_LOAD_FACTOR);
    }

    public Directory(int initialCapacity) {
        this(initialCapacity, DEFAULT_LOAD_FACTOR);
    }

    public Directory(int initialCapacity, float loadFactor) {
        idToCoin = new HashMap(initialCapacity, loadFactor);
        coinToId = new HashMap(initialCapacity, loadFactor);
    }

    public Integer getId(Coin coin) {
        return coinToId.get(coin);
    }

    public Coin getCoin(Integer id) {
        return idToCoin.get(id);
    }

    public boolean pair(Coin coin, Integer id) {
        coinToId.put(coin, id);
        idToCoin.put(id, coin);
        return true;
    }

    public boolean remove(Coin coin) {
        Integer id = coinToId.get(coin);
        if (id != null) {
            coinToId.remove(coin);
            idToCoin.remove(id);
            return true;
        }
        return false;
    }

    public boolean remove(Integer id) {
        Coin coin = idToCoin.get(id);
        if (coin != null) {
            coinToId.remove(coin);
            idToCoin.remove(id);
            return true;
        }
        return false;
    }
}
