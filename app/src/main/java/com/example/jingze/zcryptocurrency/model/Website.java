package com.example.jingze.zcryptocurrency.model;

public enum Website implements BaseWebsite{

  BITFINEX("wss://api.bitfinex.com/ws/2"),
  HUOBI("wss://api.huobipro.com/ws");

  private String endpoint;

  Website(String endpoint) {
    this.endpoint = endpoint;
  }

  @Override
  public String getName() {
    return this.name();
  }

  @Override
  public String getEndpoint() {
    return this.endpoint;
  }

}
