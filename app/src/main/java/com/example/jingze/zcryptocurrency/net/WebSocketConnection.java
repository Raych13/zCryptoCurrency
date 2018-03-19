package com.example.jingze.zcryptocurrency.net;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

public class WebSocketConnection {

    public interface HandlerListener {
        void handlerNewMessage(String message);
//        void onStatusChange(ConnectionStatus status);
    }

    private String serverURL;
    private OkHttpClient client;
    private WebSocket webSocket;

    private Handler messageHandler;
    //    private Handler statusHandler;
    private HandlerListener handlerListener;

    private class ZWebSocketListener extends WebSocketListener {
        @Override
        public void onOpen(WebSocket webSocket, Response response) {
            webSocket.send("Connected!");
            Log.i("Raych","onOpen method is called.");
//            Log.i("Raych", "Sent Message to WebSocket.");
        }

        @Override
        public void onMessage(WebSocket webSocket, String text) {
            Message msg = messageHandler.obtainMessage(0, text);
            messageHandler.sendMessage(msg);
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
            disconnect();
        }
    }

    public WebSocketConnection(String url) {
        client = new OkHttpClient.Builder()
                .readTimeout(3, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .build();
        serverURL = url;
    }

    public void connect(HandlerListener listener) {
        Request request = new Request.Builder()
                .url(serverURL)
                .build();
        webSocket = client.newWebSocket(request, new ZWebSocketListener());

        handlerListener = listener;
        messageHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message message) {
                handlerListener.handlerNewMessage((String) message.obj);
                return true;
            }
        });
        Log.i("Raych", "Method connect() is called.");
    }

    public void disconnect() {
        webSocket.cancel();
        client.dispatcher().executorService().shutdown();
        handlerListener = null;
        messageHandler.removeCallbacksAndMessages(null);
//        statusHandler.removeCallbacksAndMessages(null);
    }

    public void sendMessage(String message) {
        webSocket.send(message);
        Log.i("Raych", "Method sendMessage() is called.");
    }
}
