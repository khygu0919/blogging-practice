package com.example.mynotification;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.util.Calendar;
import java.util.Date;

public class NotificationHelper {
    private Context mContext;
    private static final String NOTIFICATION_CHANNEL_ID = "10001";
    private static final String NOTIFICATION_CHANNEL_ID2 = "10002";
    NotificationHelper(Context context){
        mContext = context;
    }
    public void NotifyMorningChallenge() {
        Intent intent = new Intent(mContext , MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
        intent.setAction(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);

        NotificationManager notificationmanager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(mContext, NOTIFICATION_CHANNEL_ID);
        notificationBuilder.setSmallIcon(R.drawable.smile)
                .setContentTitle("주간 챌린지 시작!").setContentText("참여해서 수험자들과 경쟁하세요!")
                .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);



        assert notificationmanager != null;
        notificationmanager.notify(0, notificationBuilder.build());

    }

    public void NotifyNightChallenge() {
        Intent intent = new Intent(mContext , MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
        intent.setAction(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);

        NotificationManager notificationmanager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 1, intent, PendingIntent.FLAG_CANCEL_CURRENT);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(mContext, NOTIFICATION_CHANNEL_ID);
        notificationBuilder.setSmallIcon(R.drawable.smile)
                .setContentTitle("야간 챌린지 시작!").setContentText("참여해서 수험자들과 경쟁하세요!")
                .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        assert notificationmanager != null;
        notificationmanager.notify(1, notificationBuilder.build());
    }

}
