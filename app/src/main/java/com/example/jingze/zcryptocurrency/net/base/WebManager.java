package com.example.jingze.zcryptocurrency.net.base;

import okhttp3.WebSocket;
import okio.ByteString;

public abstract class WebManager {

    public abstract boolean isConnected();

    public abstract void startConnect();

    public abstract void stopConnect();

    public abstract WebSocket getWebSocket();

    public abstract int getCurrentStatus();

    public abstract void setCurrentStatus(int currentStatus);

    public abstract boolean send(Object msg);


    //The codes of status.
    public static class Status {
        //DISCONNECTED indicates that network is unavailable or being disconnected manually.
        public final static int DISCONNECTED = -1;
        //CONNECTING indicates that manager is trying to connect.
        public final static int CONNECTING = 0;
        //
        public final static int CONNECTED = 1;
        //RECONNECTING indicates that manager has post a reconnectRunnable.
        public final static int RECONNECTING = 2;

        public class CODE {
            public final static int NORMAL_CLOSE = 1000;
            public final static int ABNORMAL_CLOSE = 1001;
        }

        public class REASON {
            public final static String NORMAL_CLOSE = "normal close";
            public final static String ABNORMAL_CLOSE = "abnormal close";
        }
    }
}
