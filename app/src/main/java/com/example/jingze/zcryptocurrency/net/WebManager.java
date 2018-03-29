package com.example.jingze.zcryptocurrency.net;

import okhttp3.WebSocket;
import okio.ByteString;

public abstract class WebManager {

    abstract boolean isConnected();

    abstract void startConnect();

    abstract void stopConnect();

    abstract WebSocket getWebSocket();

    abstract int getCurrentStatus();

    abstract void setCurrentStatus(int currentStatus);

    abstract boolean send(Object msg);

    //The codes of status.
    public static class Status {
        //DISCONNECTED indicates that network is unavailable or being disconnected manually.
        final static int DISCONNECTED = -1;
        //CONNECTING indicates that manager is trying to connect.
        final static int CONNECTING = 0;
        //
        final static int CONNECTED = 1;
        //RECONNECTING indicates that manager has post a reconnectRunnable.
        final static int RECONNECTING = 2;

        class CODE {
            final static int NORMAL_CLOSE = 1000;
            final static int ABNORMAL_CLOSE = 1001;
        }

        class REASON {
            final static String NORMAL_CLOSE = "normal close";
            final static String ABNORMAL_CLOSE = "abnormal close";
        }
    }
}