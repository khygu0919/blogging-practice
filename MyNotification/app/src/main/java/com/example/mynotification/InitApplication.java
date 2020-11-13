package com.example.mynotification;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

public class InitApplication extends Application {
    private Context mContext;
    NotificationManager notificationManager;
    private static final String NOTIFICATION_CHANNEL_ID = "10001";
    @Override
    public void onCreate() {
        super.onCreate();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "My Notifications", NotificationManager.IMPORTANCE_HIGH);
            notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
            // Configure the notification channel.
            notificationChannel.setDescription("Channel description");
            notificationChannel.enableLights(true);
            notificationChannel.setVibrationPattern(new long[]{0, 1000, 500});
            notificationChannel.enableVibration(true);
            assert notificationManager != null;
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }

}
