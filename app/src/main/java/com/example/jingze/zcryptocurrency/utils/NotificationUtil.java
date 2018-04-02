package com.example.jingze.zcryptocurrency.utils;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.example.jingze.zcryptocurrency.MainActivity;
import com.example.jingze.zcryptocurrency.R;

/**
 * Created by Jingze HUANG on Apr.01, 2018.
 */

public class NotificationUtil {

        @SuppressLint("NewApi")
        public static void sendNotication(Context context) {
            // 在Android进行通知处理，首先需要重系统哪里获得通知管理器NotificationManager，它是一个系统Service。
            NotificationManager manager = (NotificationManager) context
                    .getSystemService(Context.NOTIFICATION_SERVICE);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
                    new Intent(context, MainActivity.class), 0);
            // 通过Notification.Builder来创建通知，注意API Level
            // API16之后才支持
            Notification notify = new Notification.Builder(context)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setTicker("您有新短消息，请注意查收！").setContentTitle("我是标题")
                    .setContentText("点我打开主页").setContentIntent(pendingIntent)
                    .setNumber(1).build();

            notify.flags |= Notification.FLAG_AUTO_CANCEL; // FLAG_AUTO_CANCEL表明当通知被用户点击时，通知将被清除。
            manager.notify(1, notify);
        }
}
