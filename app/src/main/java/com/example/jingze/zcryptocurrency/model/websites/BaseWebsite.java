package com.example.jingze.zcryptocurrency.model;

import java.util.Set;

public interface BaseWebsite {

  String getName();

  String getEndpoint();

  /**
   * Return a set of available crypto-currency of this website.
   */
  Set<String> getCoinSet();

  /**
   * Return a set of available currency of this website.
   */
  Set<String> getCurrencySet();

}
