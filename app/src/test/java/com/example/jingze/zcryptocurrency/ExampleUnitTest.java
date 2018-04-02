package com.example.jingze.zcryptocurrency;

import android.util.Log;

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
        String jsonListList = "[{\"name\":\"Bitfinex\",\"url\":\"wss://api.bitfinex.com/ws/2\",\"data\":[{\"title\":\"○  BTC - USD\",\"coinType\":\"BTC\",\"currencyType\":\"USD\"},{\"title\":\"○  ETH - USD\",\"coinType\":\"ETH\",\"currencyType\":\"USD\"},{\"title\":\"○  LTC - USD\",\"coinType\":\"LTC\",\"currencyType\":\"USD\"},{\"title\":\"○  EOS - USD\",\"coinType\":\"EOS\",\"currencyType\":\"USD\"},{\"title\":\"○  ETC - USD\",\"coinType\":\"ETC\",\"currencyType\":\"USD\"},{\"title\":\"○  XRP - USD\",\"coinType\":\"XRP\",\"currencyType\":\"USD\"},{\"title\":\"○  BCH - USD\",\"coinType\":\"BCH\",\"currencyType\":\"USD\"},{\"title\":\"○  NEO - USD\",\"coinType\":\"NEO\",\"currencyType\":\"USD\"},{\"title\":\"○  OMG - USD\",\"coinType\":\"OMG\",\"currencyType\":\"USD\"},{\"title\":\"○  XMR - USD\",\"coinType\":\"XMR\",\"currencyType\":\"USD\"},{\"title\":\"○  BTG - USD\",\"coinType\":\"BTG\",\"currencyType\":\"USD\"},{\"title\":\"○  ELF - USD\",\"coinType\":\"ELF\",\"currencyType\":\"USD\"},{\"title\":\"○  ZEC - USD\",\"coinType\":\"ZEC\",\"currencyType\":\"USD\"},{\"title\":\"○  BAT - USD\",\"coinType\":\"BAT\",\"currencyType\":\"USD\"},{\"title\":\"○  TRX - USD\",\"coinType\":\"TRX\",\"currencyType\":\"USD\"},{\"title\":\"○  SAN - USD\",\"coinType\":\"SAN\",\"currencyType\":\"USD\"},{\"title\":\"○  RCN - USD\",\"coinType\":\"RCN\",\"currencyType\":\"USD\"},{\"title\":\"○  EDO - USD\",\"coinType\":\"EDO\",\"currencyType\":\"USD\"},{\"title\":\"○  TNB - USD\",\"coinType\":\"TNB\",\"currencyType\":\"USD\"},{\"title\":\"○  ETP - USD\",\"coinType\":\"ETP\",\"currencyType\":\"USD\"},{\"title\":\"○  ZRX - USD\",\"coinType\":\"ZRX\",\"currencyType\":\"USD\"},{\"title\":\"○  SNT - USD\",\"coinType\":\"SNT\",\"currencyType\":\"USD\"},{\"title\":\"○  SPK - USD\",\"coinType\":\"SPK\",\"currencyType\":\"USD\"},{\"title\":\"○  REP - USD\",\"coinType\":\"REP\",\"currencyType\":\"USD\"},{\"title\":\"○  YYW - USD\",\"coinType\":\"YYW\",\"currencyType\":\"USD\"},{\"title\":\"○  FUN - USD\",\"coinType\":\"FUN\",\"currencyType\":\"USD\"},{\"title\":\"○  GNT - USD\",\"coinType\":\"GNT\",\"currencyType\":\"USD\"},{\"title\":\"○  AID - USD\",\"coinType\":\"AID\",\"currencyType\":\"USD\"},{\"title\":\"○  MNA - USD\",\"coinType\":\"MNA\",\"currencyType\":\"USD\"},{\"title\":\"○  RLC - USD\",\"coinType\":\"RLC\",\"currencyType\":\"USD\"},{\"title\":\"○  AVT - USD\",\"coinType\":\"AVT\",\"currencyType\":\"USD\"},{\"title\":\"○  SNG - USD\",\"coinType\":\"SNG\",\"currencyType\":\"USD\"}]}," +
                "{\"name\":\"Huobi\",\"url\":\"wss://api.huobipro.com/ws\",\"data\":[{\"title\":\"○  BTC - USDT\",\"coinType\":\"BTC\",\"currencyType\":\"USDT\"},{\"title\":\"○  BCH - USDT\",\"coinType\":\"BCH\",\"currencyType\":\"USDT\"},{\"title\":\"○  ETH - USDT\",\"coinType\":\"ETH\",\"currencyType\":\"USDT\"},{\"title\":\"○  ETC - USDT\",\"coinType\":\"ETC\",\"currencyType\":\"USDT\"},{\"title\":\"○  LTC - USDT\",\"coinType\":\"LTC\",\"currencyType\":\"USDT\"},{\"title\":\"○  EOS - USDT\",\"coinType\":\"EOS\",\"currencyType\":\"USDT\"},{\"title\":\"○  XRP - USDT\",\"coinType\":\"XRP\",\"currencyType\":\"USDT\"},{\"title\":\"○  OMG - USDT\",\"coinType\":\"OMG\",\"currencyType\":\"USDT\"},{\"title\":\"○  DASH - USDT\",\"coinType\":\"DASH\",\"currencyType\":\"USDT\"},{\"title\":\"○  ZEC - USDT\",\"coinType\":\"ZEC\",\"currencyType\":\"USDT\"},{\"title\":\"○  HT - USDT\",\"coinType\":\"HT\",\"currencyType\":\"USDT\"},{\"title\":\"○  IOST - USDT\",\"coinType\":\"IOST\",\"currencyType\":\"USDT\"},{\"title\":\"○  NEO - USDT\",\"coinType\":\"NEO\",\"currencyType\":\"USDT\"},{\"title\":\"○  HSR - USDT\",\"coinType\":\"HSR\",\"currencyType\":\"USDT\"},{\"title\":\"○  QTUM - USDT\",\"coinType\":\"QTUM\",\"currencyType\":\"USDT\"},{\"title\":\"○  XEM - USDT\",\"coinType\":\"XEM\",\"currencyType\":\"USDT\"},{\"title\":\"○  TRX - USDT\",\"coinType\":\"TRX\",\"currencyType\":\"USDT\"},{\"title\":\"○  SMT - USDT\",\"coinType\":\"SMT\",\"currencyType\":\"USDT\"},{\"title\":\"○  VEN - USDT\",\"coinType\":\"VEN\",\"currencyType\":\"USDT\"},{\"title\":\"○  ELF - USDT\",\"coinType\":\"ELF\",\"currencyType\":\"USDT\"},{\"title\":\"○  SNT - USDT\",\"coinType\":\"SNT\",\"currencyType\":\"USDT\"},{\"title\":\"○  NAS - USDT\",\"coinType\":\"NAS\",\"currencyType\":\"USDT\"},{\"title\":\"○  GNT - USDT\",\"coinType\":\"GNT\",\"currencyType\":\"USDT\"}]}]";


        ArrayList<CoinMenu> total = null;
        total = ModelUtils.toObject(jsonListList, new TypeToken<ArrayList<CoinMenu>>(){});


        assertEquals(4, 2 + 2);
    }
}