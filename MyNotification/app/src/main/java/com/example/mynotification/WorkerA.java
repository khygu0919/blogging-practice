package com.example.mynotification;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

public class WorkerA extends Worker {
    public WorkerA(@NonNull Context context, @NonNull WorkerParameters params) {
        super(context, params);

    }

    @NonNull
    @Override
    public Result doWork() {
        NotificationHelper mNotificationHelper = new NotificationHelper(getApplicationContext());
        mNotificationHelper.NotifyMorningChallenge();
        Calendar foo = Calendar.getInstance(TimeZone.getTimeZone("Asia/Seoul"), Locale.KOREA);
        Log.e("morning challenge time", foo.getTime().toString());
        return Result.success();
    }

}
