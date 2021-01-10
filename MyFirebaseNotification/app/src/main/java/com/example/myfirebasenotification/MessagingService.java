package com.example.myfirebasenotification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MessagingService extends FirebaseMessagingService {
    private static final String[] topics = {"/topics/custom", "/topics/notify"};

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        makeNotification(remoteMessage);
    }

    @Override
    public void onNewToken(String s) {
        Log.e("token?", s);
        super.onNewToken(s);
    }

    private void makeNotification(RemoteMessage remoteMessage) {
        try {
            int notificationId = -1;
            Context mContext = getApplicationContext();

            Intent intent = new Intent(this, MainActivity.class);
            intent.setAction(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);

            String title = remoteMessage.getData().get("title");
            String message = remoteMessage.getData().get("body");
            String topic = remoteMessage.getFrom();

            NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "10001");
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
                builder.setVibrate(new long[] {200, 100, 200});
            }
            builder.setAutoCancel(true)
                    .setDefaults(Notification.DEFAULT_SOUND)
                    .setColor(Color.CYAN)
                    .setContentTitle(title)
                    .setContentText(message);

            if (topic.equals(topics[0])) {
                notificationId = 0;
            }

            if (notificationId >= 0) {
                PendingIntent pendingIntent = PendingIntent.getActivity(this, notificationId, intent, PendingIntent.FLAG_CANCEL_CURRENT);
                builder.setContentIntent(pendingIntent);
                notificationManager.notify(notificationId, builder.build());
            }

        } catch (NullPointerException nullException) {
            Toast.makeText(getApplicationContext(), "알림에 오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
            Log.e("error Notify", nullException.toString());
        }
    }

}
