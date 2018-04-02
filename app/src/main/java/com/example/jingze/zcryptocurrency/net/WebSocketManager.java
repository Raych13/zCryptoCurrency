package com.example.jingze.zcryptocurrency.net;

import android.os.Handler;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.util.Log;

import com.example.jingze.zcryptocurrency.net.base.WebManager;
import com.example.jingze.zcryptocurrency.utils.NetworkServiceUtils;
import com.example.jingze.zcryptocurrency.utils.SnackBarUtils;

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

public class WebSocketManager extends WebManager {
    public final static int MESSAGE_TYPE_TEXT = 100;
    public final static int MESSAGE_TYPE_BYTES = 101;
    private final static int RECONNECT_INTERVAL = 5 * 1000;    //Reconnection interval
    private final static long RECONNECT_MAX_TIME = 60 * 1000;   //Maximum reconnection time
    private String coinMenuName;
    private String serverURL;
    private OkHttpClient client;
    private WebSocket mWebSocket;
    private int mCurrentStatus = Status.DISCONNECTED;   //Current status of websocket connection.
    private boolean isNeedReconnect;                    //Whether you need to reconnect when websocket is disconnected.
    private boolean isManualClose = false;              //Whether you close connection manually.
    private int reconnectionCount = 0;                  //Record of the number of trying to reconnect.
    private Lock mLock;
    private WebSocketExtraListener webSocketExtraListener;
    private Handler mainThreadHandler;
    private Handler dataThreadHandler;
    private Runnable reconnectRunnable = new Runnable() {
        @Override
        public void run() {
            if (webSocketExtraListener != null) {
                webSocketExtraListener.onReconnect();
            }
            buildConnection();
        }
    };

    //Override WebSocketListener to create my own one.
    private WebSocketListener mWebSocketListener = new WebSocketListener() {
        @Override
        public void onOpen(WebSocket webSocket, Response response) {
            Log.i("Raych", "WebManager: "+ serverURL + ". "+ "Connected.");
            mWebSocket = webSocket;
            setCurrentStatus(Status.CONNECTED);
            connected();
            if (webSocketExtraListener != null) {
                webSocketExtraListener.onOpen(response);
            }
        }

        @Override
        public void onMessage(WebSocket webSocket, String text) {
            Log.v("Raych", "Get Message(text) from WebSocket. ");
            Message msg = dataThreadHandler.obtainMessage(MESSAGE_TYPE_TEXT, text);
            dataThreadHandler.sendMessage(msg);
            if (webSocketExtraListener != null) {
                webSocketExtraListener.onMessage(text);
            }
        }

        @Override
        public void onMessage(WebSocket webSocket, ByteString bytes) {
            Log.v("Raych", "Get Message from(bytes) WebSocket.");
            Message msg = dataThreadHandler.obtainMessage(MESSAGE_TYPE_BYTES, bytes);
            dataThreadHandler.sendMessage(msg);
            if (webSocketExtraListener != null) {
                webSocketExtraListener.onMessage(bytes);
            }
        }

        @Override
        public void onClosing(WebSocket webSocket, int code, String reason) {
            Log.i("Raych", "WebManager: " + serverURL + ". onClosing() is called.");
            if (webSocketExtraListener != null) {
                webSocketExtraListener.onClosing(code, reason);
            }
        }

        @Override
        public void onClosed(WebSocket webSocket, int code, String reason) {
            Log.i("Raych", "WebManager: " + serverURL + ". onClosed() is called.");
            if (client != null) {
                client.dispatcher().cancelAll();
            }
            if (webSocketExtraListener != null) {
                webSocketExtraListener.onClosed(code,reason);
            }
        }

        @Override
        public void onFailure(WebSocket webSocket, Throwable throwable, Response response) {
            Log.w("Raych", "WebManager: " + serverURL + ". onFailure() is called.");
            attemptReconnect();
            if (webSocketExtraListener != null) {
                webSocketExtraListener.onFailure(throwable,response);
            }
        }
    };

    //Constructor
    WebSocketManager(Builder builder) {
        this.coinMenuName = builder.coinMenuName;
        this.serverURL = builder.serverURL;
        this.isNeedReconnect = builder.isNeedReconnect;
        this.client = builder.client;
        this.mainThreadHandler = builder.mainThreadHandler;
        this.dataThreadHandler = builder.dataThreadHandler;
        this.webSocketExtraListener = builder.webSocketExtraListener;
        this.mLock = new ReentrantLock();
    }

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
    @Override
    public synchronized boolean isConnected() {
        return mCurrentStatus == Status.CONNECTED;
    }

    @Override
    public synchronized int getCurrentStatus() {
        return mCurrentStatus;
    }

    @Override
    public synchronized void setCurrentStatus(int currentStatus) {
        this.mCurrentStatus = currentStatus;
    }

    // Public methods of managing connection.
    @Override
    public void startConnect() {
        Log.i("Raych", this + "WebManager: startConnect() is called.");
        isManualClose = false;
        buildConnection();
    }

    @Override
    public void stopConnect() {
        Log.i("Raych", this + " WebSocketManager: stopConnect() is called.");
        isManualClose = true;
        disconnect();
    }

    @Override
    public WebSocket getWebSocket() {
        return mWebSocket;
    }

    @Override
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
                attemptReconnect();
            }
        }
        return isSend;
    }

    //Private methods below:
    private synchronized void buildConnection() {
        if (!NetworkServiceUtils.isNetworkServiceAvailable()) {
            Log.e("Raych", "NetworkServiceUnavailable.");
            setCurrentStatus(Status.DISCONNECTED);
            whenNetworkUnavailable();
            return;
        }
        switch (getCurrentStatus()) {
            case Status.CONNECTED:
            case Status.CONNECTING:
                break;
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
                Log.e("Raych", "initiateWebSocket() method: InterruptedException");
        }
    }

    private void connected() {
        Log.i("Raych", this + " WebSocketManager: connected() is called.");
        cancelReconnect();
    }

    private void disconnect() {
        if (getCurrentStatus() == Status.DISCONNECTED) {
            return;
        }
        cancelReconnect();
        if (mWebSocket != null) {
            boolean isClose = mWebSocket.close(Status.CODE.NORMAL_CLOSE, Status.REASON.NORMAL_CLOSE);
            if (isClose) {
                if (webSocketExtraListener != null) {
                    webSocketExtraListener.onClosed(Status.CODE.ABNORMAL_CLOSE, Status.REASON.ABNORMAL_CLOSE);
                }
            }
        }
        setCurrentStatus(Status.DISCONNECTED);
    }

    private void attemptReconnect() {
        if (!isNeedReconnect || isManualClose) {
            return;
        }
        if (!NetworkServiceUtils.isNetworkServiceAvailable()) {
            Log.e("Raych", "NetworkServiceUnavailable.");
            setCurrentStatus(Status.DISCONNECTED);
            whenNetworkUnavailable();
            return;
        }
        setCurrentStatus(Status.RECONNECTING);

        long delay = reconnectionCount * RECONNECT_INTERVAL;
        dataThreadHandler.postDelayed(reconnectRunnable,
                delay > RECONNECT_MAX_TIME ? RECONNECT_MAX_TIME : delay);
        reconnectionCount++;
    }

    private void cancelReconnect() {
        dataThreadHandler.removeCallbacks(reconnectRunnable);
        reconnectionCount = 0;
    }

    //Check if the networkService is available. (isNetworkConnected)
    //To be done: when network is closed.
//    private boolean isNetworkServiceAvailable(final Context context) {
//        if (context != null) {
//            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
//                    .getSystemService(Context.CONNECTIVITY_SERVICE);
//            NetworkInfo mNetworkInfo = mConnectivityManager
//                    .getActiveNetworkInfo();
//            if (mNetworkInfo != null) {
//                return mNetworkInfo.isAvailable();
//            }
//           //when network is closed.
//        }
//        return false;
//    }

    private void whenNetworkUnavailable() {
        Runnable runnable = SnackBarUtils.getSnackBarRunnable("Network is unavailable.", Snackbar.LENGTH_LONG);
        if (runnable != null) {
            mainThreadHandler.post(runnable);
        }
    }


    public interface WebSocketExtraListener{
        void onOpen(Response response);

        void onMessage(String text);

        void onMessage(ByteString bytes);

        void onReconnect();

        void onClosing(int code, String reason);

        void onClosed(int code, String reason);

        void onFailure(Throwable throwable, Response response);
    }

    public static final class Builder {
        private String coinMenuName;
        private String serverURL;
        private boolean isNeedReconnect = true;
        private Handler mainThreadHandler;
        private Handler dataThreadHandler;
        private OkHttpClient client;
        private WebSocketExtraListener webSocketExtraListener;

        public Builder(Handler mainThreadHandler,
                       Handler dataThreadHandler) {
            this.mainThreadHandler = mainThreadHandler;
            this.dataThreadHandler = dataThreadHandler;
        }

        public void setCoinMenuName(String coinMenuName) {
            this.coinMenuName = coinMenuName;
        }

        public Builder url(String serverURL) {
            this.serverURL = serverURL;
            return this;
        }

        public Builder client(OkHttpClient okHttpClient) {
            this.client = okHttpClient;
            return this;
        }

        public Builder isNeedReconnect(boolean isNeedReconnect) {
            this.isNeedReconnect = isNeedReconnect;
            return this;
        }

        public Builder okHttpClient(OkHttpClient client) {
            this.client = client;
            return this;
        }

        public Builder webSocketExtraListener(WebSocketExtraListener webSocketExtraListener) {
            this.webSocketExtraListener = webSocketExtraListener;
            return this;
        }

        public WebSocketManager build() {
            return new WebSocketManager(this);
        }

    }
}
