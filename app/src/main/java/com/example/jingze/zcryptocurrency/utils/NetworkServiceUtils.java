package com.example.jingze.zcryptocurrency.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Jingze HUANG on Mar.29, 2018.
 */

public class NetworkServiceUtils {

//    public interface GetContext {
//        Context getContext();
//    }
//
//    private static GetContext mGetContext;
//
//    public static void setGetContext(GetContext mGetContext) {
//        NetworkServiceUtils.mGetContext = mGetContext;
//    }
//
//    public static boolean isNetworkServiceAvailable() {
//        if (mGetContext != null) {
//            ConnectivityManager mConnectivityManager = (ConnectivityManager) mGetContext.getContext()
//                    .getSystemService(Context.CONNECTIVITY_SERVICE);
//            NetworkInfo mNetworkInfo = mConnectivityManager
//                    .getActiveNetworkInfo();
//            if (mNetworkInfo != null) {
//                return mNetworkInfo.isAvailable();
//            }
//        }
//        return false;
//    }
    private static Context mContext;

    public static void setContext(Context context) {
        NetworkServiceUtils.mContext = context;
    }

    public static boolean isNetworkServiceAvailable() {
        if (mContext != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) mContext
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager
                    .getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }

}
