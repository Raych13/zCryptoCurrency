package com.example.jingze.zcryptocurrency.net;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

/**
 * Created by Jingze HUANG on Mar.20, 2018.
 */

public class WebSocketManager {

    private final static int RECONNECT_INTERVAL = 5 * 1000;    //Reconnection interval
    private final static long RECONNECT_MAX_TIME = 60 * 1000;   //Maximum reconnection time
    private Context mContext;
    private String serverURL;
    private OkHttpClient client;
    private WebSocket mWebSocket;
    private int mCurrentStatus = Status.DISCONNECTED;   //Current status of websocket connection.
    private boolean isNeedReconnect;                    //Whether you need to reconnect when websocket is disconnected.
    private boolean isManualClose = false;              //Whether you close connection manually.
    private int reconnectionCount = 0;                  //Record of the number of trying to reconnect.
    private Lock mLock;
    private Handler mainThreadHandler;
    private Handler dataThreadHandler;

    //Override WebSocketListener to create my own one.
    private WebSocketListener mWebSocketListener = new WebSocketListener() {
        @Override
        public void onOpen(WebSocket webSocket, Response response) {
            mWebSocket = webSocket;
            setCurrentStatus(Status.CONNECTED);
            connected();
        }

        @Override
        public void onMessage(WebSocket webSocket, String text) {
            Message msg = mainThreadHandler.obtainMessage(0, text);
            mainThreadHandler.sendMessage(msg);
            Log.i("Raych", "Get text-Message from WebSocket" + text);
        }

        @Override
        public void onMessage(WebSocket webSocket, ByteString bytes) {
            Log.i("Raych", "Get bytes-Message from WebSocket: ");
        }

        @Override
        public void onClosed(WebSocket webSocket, int code, String reason) {
//            Message m = statusHandler.obtainMessage(0, ConnectionStatus.DISCONNECTED);
//            statusHandler.sendMessage(m);
        }

        @Override
        public void onFailure(WebSocket webSocket, Throwable t, Response response) {
            reconnect();
        }
    };

    public WebSocketManager(Builder builder) {
        this.mContext = builder.mContext;
        this.serverURL = builder.serverURL;
        this.isNeedReconnect = builder.isNeedReconnect;
        this.client = builder.client;
        this.mainThreadHandler = builder.mainThreadHandler;
        this.dataThreadHandler = builder.dataThreadHandler;
        this.mLock = new ReentrantLock();
    }

//    public ServerConnection(String url) {
//        client = new OkHttpClient.Builder()
//                .readTimeout(3, TimeUnit.SECONDS)
//                .retryOnConnectionFailure(true)
//                .build();
//        serverURL = url;
//        isConnected = false;
//    }

//    public void connect(ServerConnection.HandlerListener listener) {
//        Request request = new Request.Builder()
//                .url(serverURL)
//                .build();
//        webSocket = client.newWebSocket(request, new ServerConnection.ZWebSocketListener());
//
//        handlerListener = listener;
//        messageHandler = new Handler(new Handler.Callback() {
//            @Override
//            public boolean handleMessage(Message message) {
//                handlerListener.handlerNewMessage((String) message.obj);
//                return true;
//            }
//        });
//        isConnected = true;
//        Log.i("Raych", "Method connect() is called.");
//    }

//    public void disconnect() {
//        isConnected = false;
//
//        webSocket.cancel();
//        client.dispatcher().executorService().shutdown();
//        handlerListener = null;
//        messageHandler.removeCallbacksAndMessages(null);
//        statusHandler.removeCallbacksAndMessages(null);
//    }

    //Methods are relative to mCurrencyStatus;
    public synchronized boolean isConnected() {
        return mCurrentStatus == Status.CONNECTED;
    }

    public synchronized int getCurrentStatus() {
        return mCurrentStatus;
    }

    public synchronized void setCurrentStatus(int currentStatus) {
        this.mCurrentStatus = currentStatus;
    }

    // Public methods of managing connection.
    public void startConnect() {
        isManualClose = false;
        buildConnection();
    }

    public void stopConnect() {
        isManualClose = true;
        disconnect();
    }

    public boolean send(Object msg) {
        boolean isSend = false;
        if (mWebSocket != null && mCurrentStatus == Status.CONNECTED) {
            if (msg instanceof String) {
                isSend = mWebSocket.send((String) msg);
            } else if (msg instanceof ByteString) {
                isSend = mWebSocket.send((ByteString) msg);
            }
            //Reconnect when fail to send message
            if (!isSend) {
                reconnect();
            }
        }
        return isSend;
    }

    //Private methods below:
    private synchronized void buildConnection() {
        if (!isNetworkServiceAvailable(mContext)) {
            setCurrentStatus(Status.DISCONNECTED);
            return;
        }
        switch (getCurrentStatus()) {
            case Status.CONNECTED:
            case Status.CONNECTING:
                return;
            default:
                setCurrentStatus(Status.CONNECTING);
                initiateWebSocket();
        }
    }

    private void initiateWebSocket() {
        Request mRequest = new Request.Builder()
                    .url(serverURL)
                    .build();

        if (client == null) {
            client = new OkHttpClient.Builder()
                    .readTimeout(3, TimeUnit.SECONDS)
                    .retryOnConnectionFailure(true)
                    .build();
        }

        client.dispatcher().cancelAll();
        try {
            mLock.lockInterruptibly();
            try {
                client.newWebSocket(mRequest, mWebSocketListener);
            } finally {
                mLock.unlock();
            }
        } catch (InterruptedException e) {

        }
    }

    private void connected() {
        cancelReconnect();
    }

    // To be done
    private void disconnect() {
        if (getCurrentStatus() == Status.DISCONNECTED) {
            return;
        }
        cancelReconnect();
        if (client != null) {
            client.dispatcher().cancelAll();
        }
        if (mWebSocket != null) {
            boolean isClose = mWebSocket.close(Status.CODE.NORMAL_CLOSE, Status.REASON.NORMAL_CLOSE);

//            if (isClose) {
//                if ()
//            }
        }
        setCurrentStatus(Status.DISCONNECTED);
    }

    // To be done!!!!!!!
    private void reconnect() {
        if (!isNeedReconnect || isManualClose) {
            return;
        }
        if (!isNetworkServiceAvailable(mContext)) {
            setCurrentStatus(Status.DISCONNECTED);
            return;
        }
        setCurrentStatus(Status.RECONNECTING);

        long delay = reconnectionCount * RECONNECT_INTERVAL;
//        ????????
//        wsMainHandler
//                .postDelayed(reconnectRunnable, delay > RECONNECT_MAX_TIME ? RECONNECT_MAX_TIME : delay);
        reconnectionCount++;
    }

    // To be done.
    private void cancelReconnect() {
//        wsMainHandler.removeCallbacks(reconnectRunnable);
        reconnectionCount = 0;
    }

    //Check if the networkService is available. (isNetworkConnected)
    //To be done: when network is closed.
    private boolean isNetworkServiceAvailable(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager
                    .getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                boolean availability = mNetworkInfo.isAvailable();
                return availability;
            } else {
                //When network is not available.
            }
        }
        return false;
    }

    public static final class Builder {
        private Context mContext;
        private String serverURL;
        private boolean isNeedReconnect = true;
        private Handler mainThreadHandler;
        private Handler dataThreadHandler;
        private OkHttpClient client;

        public Builder(Context context,
                       Handler mainThreadHandler,
                       Handler dataThreadHandler) {
            this.mContext = context;
            this.mainThreadHandler = mainThreadHandler;
            this.dataThreadHandler = dataThreadHandler;
        }

        public Builder url(String serverURL) {
            this.serverURL = serverURL;
            return this;
        }

        public Builder client(OkHttpClient okHttpClient) {
            this.client = okHttpClient;
            return this;
        }

        public Builder needReconnect(boolean isNeedReconnect) {
            this.isNeedReconnect = isNeedReconnect;
            return this;
        }

        public WebSocketManager build() {
            return new WebSocketManager(this);
        }
    }

    //The codes of status.
    public static class Status {
        public final static int DISCONNECTED = -1;
        public final static int CONNECTING = 0;
        public final static int CONNECTED = 1;
        public final static int RECONNECTING = 2;


        class CODE {

            public final static int NORMAL_CLOSE = 1000;
            public final static int ABNORMAL_CLOSE = 1001;
        }

        class REASON {
            public final static String NORMAL_CLOSE = "normal close";
            public final static String ABNORMAL_CLOSE = "abnormal close";
        }
    }
}
