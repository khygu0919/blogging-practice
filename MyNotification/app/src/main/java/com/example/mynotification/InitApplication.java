package com.example.mynotification;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import androidx.work.WorkManager;

public class InitApplication extends Application {
    private Context mContext;
    @Override
    public void onCreate() {
        super.onCreate();
        NotificationHelper.createNotificationChannel(getApplicationContext());
        NotificationHelper.refreshScheduledNotification(getApplicationContext());
    }
}
