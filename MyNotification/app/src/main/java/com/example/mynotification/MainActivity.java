package com.example.mynotification;

import androidx.appcompat.app.AppCompatActivity;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;


public class MainActivity extends AppCompatActivity {
    Button btnMainNotify;
    Boolean isFinished = true;
    private static final String TIME_ZONE_KST = "Asia/Seoul";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnMainNotify = findViewById(R.id.btn_main_notify);
        btnMainNotify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SecondActivity.class);
                startActivity(intent);
                finish();
            }
        });
        //MorningChallengeAlarm();
        // NightChallengePushTask();
        Calendar calendar = Calendar.getInstance();
        Long nowMilli = calendar.getTimeInMillis();
        calendar.add(Calendar.MINUTE,1);
        Long startMilli = calendar.getTimeInMillis();
        Long diff = startMilli - nowMilli;
        WorkManager.getInstance(getApplicationContext()).cancelAllWork();
        WorkerHelper.setChallengePushSchedule(WorkManager.getInstance(getApplicationContext()));
        WorkerHelper.setRankingPushSchedule(WorkManager.getInstance(getApplicationContext()));
        try {
            List<WorkInfo> foo3 = WorkManager.getInstance(this).getWorkInfosByTag("challenge").get();
            Log.e("size3", ""+foo3.size());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}