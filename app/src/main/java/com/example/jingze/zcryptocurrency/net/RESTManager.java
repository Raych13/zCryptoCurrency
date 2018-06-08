package com.example.jingze.zcryptocurrency.net;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.support.design.widget.Snackbar;
import android.util.Log;

import com.example.jingze.zcryptocurrency.utils.NetworkServiceUtils;
import com.example.jingze.zcryptocurrency.utils.SnackBarUtils;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Jingze HUANG on Apr.04, 2018.
 */

public class RESTManager {

    private static Looper restLooper;
    private Handler mainThreadHandler;
    private Handler dataThreadHandler;
    private Handler restHandler;
    private OkHttpClient client;
    private OnResponseListener onResponseListener;

    public interface OnResponseListener {
        void onFailure(Call call);

        void onResponse(Call call, Response response);
    }

    private RESTManager(Builder builder) {
        if (restLooper == null) {
            HandlerThread restThread = new HandlerThread("RestHread", Process.THREAD_PRIORITY_BACKGROUND);
            restThread.start();
            restLooper = restThread.getLooper();
        }
        this.mainThreadHandler = builder.mainThreadHandler;
        this.dataThreadHandler = builder.dataThreadHandler;
        this.restHandler = new Handler(restLooper);
        if (!NetworkServiceUtils.isNetworkServiceAvailable()) {
            Log.e("Raych", "NetworkServiceUnavailable.");
            whenNetworkUnavailable();
            return;
        } else {
            this.client = new OkHttpClient.Builder().readTimeout(5, TimeUnit.SECONDS).build();
        }
        this.onResponseListener = builder.onResponseListener;
    }

    public void makeRequest(Request request) {
        Call call = client.newCall(request);
        class callback implements Callback {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("Raych", this + " RESTManager.makeRequest.onFailure(): Response failed.");
                if (onResponseListener != null) {
                    onResponseListener.onFailure(call);
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.v("Raych", this + " RESTManager.makeRequest.onResponse(): Got Response.");
                String responseString = null;
                try {
                    responseString = response.body().string();
                    Log.v("Raych", this + " RESTManager.makeRequest.onResponse(): Got Response." + responseString);
                } catch (IOException e) {
                    Log.e("Raych", this + " RESTManager.makeRequest.onResponse(): Failed to get responseString.");
                }
                Message msg = dataThreadHandler.obtainMessage();
                msg.obj = responseString;
                dataThreadHandler.sendMessage(msg);
                if (onResponseListener != null) {
                    onResponseListener.onResponse(call, response);
                }
            }
        }
        call.enqueue(new callback());
    }

    private void whenNetworkUnavailable() {
        Runnable runnable = SnackBarUtils.getSnackBarRunnable("Network is unavailable.", Snackbar.LENGTH_LONG);
        if (runnable != null) {
            mainThreadHandler.post(runnable);
        }
    }

    public static class Builder {
        private Handler mainThreadHandler;
        private Handler dataThreadHandler;
        private OnResponseListener onResponseListener;

        public Builder(Handler mainThreadHandler,
                       Handler dataThreadHandler) {
            this.mainThreadHandler = mainThreadHandler;
            this.dataThreadHandler = dataThreadHandler;
        }

        public Builder setOnResponseListener(OnResponseListener onResponseListener) {
            this.onResponseListener = onResponseListener;
            return this;
        }

        public RESTManager build() {
            return new RESTManager(this);
        }
    }

}
