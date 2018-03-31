package com.example.jingze.zcryptocurrency.net.handler;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

/**
 * Created by Jingze HUANG on Mar.30, 2018.
 */

public class UIHandler extends Handler{
    public static final int ITEM_UPDATE = 100;
    public static final int PAGE_UPDATE = 101;
    public static final int ONLOADMORE_UPDATE = 102;

    public static final int ONLOADMORE_DELAY = 1300;
    public static final int PAGE_UPDATE_DELAY = 200;


    public static final int RV_DRAGGED_RESEND_DELAY = 1500;
    public static final int RV_FLYING_RESEND_DELAY = 800;

    public static final int VP_DRAGGED_RESEND_DELAY = 300;

    public UIHandler() {
        super(Looper.getMainLooper());
    }

    public UIHandler(Callback callback) {
        super(Looper.getMainLooper(), callback);
    }

    public void sendItemUpdateMessage(int itemPosition, int attempts) {
        sendItemUpdateMessageDelay(itemPosition, attempts, 0);
    }

    public void sendItemUpdateMessageDelay(int itemPosition, int attempts, int delay) {
        Message msg = obtainMessage(ITEM_UPDATE, itemPosition, attempts);
        sendMessageDelayed(msg, delay);
    }

    public void sendPageUpdateMessage(int attempts) {
        sendPageUpdateMessageDelay(attempts, 0);
    }

    public void sendPageUpdateMessageDelay(int attempts, int delay) {
        Message msg = obtainMessage(PAGE_UPDATE, attempts);
        sendMessageDelayed(msg, delay);
    }

    public void sendLoadmoreMessage() {
        Message msg = obtainMessage(ONLOADMORE_UPDATE);
        sendMessageDelayed(msg, ONLOADMORE_DELAY);
    }


}
