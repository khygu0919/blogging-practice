package com.example.mynotification;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import static com.example.mynotification.Constants.KOREA_TIMEZONE;
import static com.example.mynotification.Constants.B_MORNING_EVENT_TIME;
import static com.example.mynotification.Constants.B_NIGHT_EVENT_TIME;


public class WorkerB extends Worker {

    public WorkerB(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        NotificationHelper mNotificationHelper = new NotificationHelper(getApplicationContext());
        long currentMillis = Calendar.getInstance(TimeZone.getTimeZone(KOREA_TIMEZONE), Locale.KOREA).getTimeInMillis();

        // 알림 범위(08:00-09:00, 20:00-21:00)에 해당하는지 기준 설정
        Calendar eventCal = NotificationHelper.getScheduledCalender(B_MORNING_EVENT_TIME);
        long morningNotifyMinRange = eventCal.getTimeInMillis();

        eventCal.add(Calendar.HOUR_OF_DAY, Constants.NOTIFICATION_INTERVAL_HOUR);
        long morningNotifyMaxRange = eventCal.getTimeInMillis();

        eventCal.set(Calendar.HOUR_OF_DAY, B_NIGHT_EVENT_TIME);
        long nightNotifyMinRange = eventCal.getTimeInMillis();

        eventCal.add(Calendar.HOUR_OF_DAY, Constants.NOTIFICATION_INTERVAL_HOUR);
        long nightNotifyMaxRange = eventCal.getTimeInMillis();

        // 현재 시각이 알림 범위에 해당하면 알림 생성
        // 그 외의 경우 가장 B 이벤트 예정 시각까지의 notificationDelay 계산하여 딜레이 호출
        boolean isMorningNotifyRange = morningNotifyMinRange <= currentMillis && currentMillis <= morningNotifyMaxRange;
        boolean isNightNotifyRange = nightNotifyMinRange <= currentMillis && currentMillis <= nightNotifyMaxRange;
        boolean isEventANotifyAvailable = isMorningNotifyRange || isNightNotifyRange;

        if (isEventANotifyAvailable) {
            mNotificationHelper.createNotification(Constants.WORK_B_NAME);
        } else {
            long notificationDelay = NotificationHelper.getNotificationDelay(Constants.WORK_B_NAME);
            OneTimeWorkRequest workRequest =
                    new OneTimeWorkRequest.Builder(WorkerA.class)
                            .setInitialDelay(notificationDelay, TimeUnit.MILLISECONDS)
                            .build();
            WorkManager.getInstance(getApplicationContext()).enqueue(workRequest);
        }
        return Result.success();
    }
}
