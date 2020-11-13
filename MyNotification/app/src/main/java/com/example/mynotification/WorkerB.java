package com.example.mynotification;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

public class WorkerB extends Worker {

    public WorkerB(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        NotificationHelper mNotificationHelper = new NotificationHelper(getApplicationContext());
        mNotificationHelper.NotifyNightChallenge();
        Calendar foo = Calendar.getInstance(TimeZone.getTimeZone("Asia/Seoul"), Locale.KOREA);
        Log.e("night challenge time", foo.getTime().toString());
        return Result.success();
    }
}
