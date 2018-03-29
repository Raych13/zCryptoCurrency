package com.example.jingze.zcryptocurrency.utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;

public class CoinUtils {
    private static final String TWO_DIGITS = "%.2f".toLowerCase();
    private static final String SIX_DIGITS = "%.6f".toLowerCase();
    private static final String PERCENTAGE_SYMBOY = "%";
    private static final String USD_SYMBOL = "$";

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
                price = (USD_SYMBOL + String.format(SIX_DIGITS, value));
            } else {
                price = (USD_SYMBOL + String.format(TWO_DIGITS, value));
            }
        }
        return price;
    }

    public static String convertToPercentage(Double value) {
        if (value != null) {
            return (String.format(TWO_DIGITS, Math.abs(value)) + PERCENTAGE_SYMBOY);
        }
        return null;
    }
}
