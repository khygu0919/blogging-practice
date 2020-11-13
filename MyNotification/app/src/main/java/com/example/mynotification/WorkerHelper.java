package com.example.mynotification;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

class WorkerHelper {
    public static void setChallengePushSchedule(WorkManager workManager) {
        boolean isFinished = true;
        try {
            List<WorkInfo> foo = workManager.getWorkInfosForUniqueWork("test work").get();
            isFinished = foo.get(0).getState().isFinished();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (isFinished) {
            PeriodicWorkRequest saveRequest =
                    new PeriodicWorkRequest.Builder(WorkerA.class, 15, TimeUnit.MINUTES, 5, TimeUnit.SECONDS)
                            .addTag("challenge")
                            .build();
            workManager.enqueueUniquePeriodicWork("test work", ExistingPeriodicWorkPolicy.KEEP, saveRequest);
            Log.e("start", "challenge routine started");
        } else {
            Log.e("running", "challenge routine is running");
        }
    }

    public static void setRankingPushSchedule(WorkManager workManager) {
        boolean isFinished = true;
        try {
            List<WorkInfo> foo = workManager.getWorkInfosForUniqueWork("test work2").get();
            isFinished = foo.get(0).getState().isFinished();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (isFinished) {
            PeriodicWorkRequest saveRequest =
                    new PeriodicWorkRequest.Builder(WorkerB.class, 15, TimeUnit.MINUTES, 5, TimeUnit.SECONDS)
                            .addTag("ranking")
                            .build();
            workManager.enqueueUniquePeriodicWork("test work2", ExistingPeriodicWorkPolicy.KEEP, saveRequest);
            Log.e("start", "ranking routine started");
        } else {
            Log.e("running", "ranking routine is running");
        }
    }

}
