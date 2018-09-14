package com.example.jingze.zcryptocurrency.model.websites;

import com.example.jingze.zcryptocurrency.model.websites.coin.BitfinexCoinList;
import com.example.jingze.zcryptocurrency.model.websites.coin.HuobiCoinList;
import com.example.jingze.zcryptocurrency.model.websites.currency.BitfinexCurrencyList;
import com.example.jingze.zcryptocurrency.model.websites.currency.HuobiCurrencyList;
import java.util.HashSet;
import java.util.Set;

public enum Website implements BaseWebsite{
  BITFINEX("wss://api.bitfinex.com/ws/2", BitfinexCoinList.class, BitfinexCurrencyList.class),
  HUOBI("wss://api.huobipro.com/ws", HuobiCoinList.class, HuobiCurrencyList.class);

  private final String endpoint;
  private final Set<String> coinSet;
  private final Set<String> currencySet;

  Website(String endpoint, Class coin, Class currency) {
    this.endpoint = endpoint;
    this.coinSet = new HashSet<>(coin.getEnumConstants().length * 3);
    this.currencySet = new HashSet<>(currency.getEnumConstants().length * 3);
    for (Object obj : coin.getEnumConstants()) {
      this.coinSet.add(obj.toString());
    }
    for (Object obj : currency.getEnumConstants()) {
      this.currencySet.add((obj.toString()));
    }
  }

  @Override
  public String getName() {
    return this.name();
  }

  @Override
  public String getEndpoint() {
    return this.endpoint;
  }

  @Override
  public Set<String> getCoinSet() {
    return this.coinSet;
  }

  @Override
  public Set<String> getCurrencySet() {
    return this.currencySet;
  }

}
