package com.example.jingze.zcryptocurrency.utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;

public class CoinUtils {

    private static DecimalFormat twoDigits = new DecimalFormat("0.00");

//    public static Double roundToNDigits(Double value, int numberOfDigits) {
//        if (value == null || numberOfDigits < 1) {
//            return null;
//        }
//         return new BigDecimal(value).setScale(numberOfDigits, BigDecimal.ROUND_HALF_UP).doubleValue();
//    }

//    public static Double roundToNDigits(Double value, int numberOfDigits) {
//        if (value == null || numberOfDigits < 1) {
//            return null;
//        }
//        return new BigDecimal(value).setScale(numberOfDigits, BigDecimal.ROUND_HALF_UP).doubleValue();
//    }

    public static String priceWithCurrencySymbol(Double value, String currencyType) {
        //Currency Symbol to be done!
        String price = null;
        if (value != null) {
            if (value < 1.0){
                price = ("$" + String.format("%.6f".toLowerCase(), value));
            } else {
                price = ("$" + String.format("%.2f".toLowerCase(), value));
            }
        }
        return price;
    }

    public static String convertToPercentage(Double value) {
        if (value != null) {
            return (String.format("%.2f".toLowerCase(), Math.abs(value * 100)) + "%");
        }
        return null;
    }
}
