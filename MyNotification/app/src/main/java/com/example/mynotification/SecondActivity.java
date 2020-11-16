package com.example.mynotification;

import androidx.appcompat.app.AppCompatActivity;
import androidx.work.WorkManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;

public class SecondActivity extends AppCompatActivity {
    Button btnSecondToMain;
    CompoundButton switchActivateNotify;

    // 푸시알림 설정
    private void initSwitchLayout(final WorkManager workManager) {
        switchActivateNotify = (CompoundButton) findViewById(R.id.switch_second_notify);
        switchActivateNotify.setChecked(PreferenceHelper.getBoolean(getApplicationContext(), Constants.SHARED_PREF_NOTIFICATION_KEY));
        switchActivateNotify.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    boolean isChannelCreated = NotificationHelper.isNotificationChannelCreated(getApplicationContext());
                    if (isChannelCreated) {
                        PreferenceHelper.setBoolean(getApplicationContext(), Constants.SHARED_PREF_NOTIFICATION_KEY, true);
                        NotificationHelper.setScheduledNotification(workManager);
                    } else {
                        NotificationHelper.createNotificationChannel(getApplicationContext());
                    }
                } else {
                    PreferenceHelper.setBoolean(getApplicationContext(), Constants.SHARED_PREF_NOTIFICATION_KEY, false);
                    workManager.cancelAllWork();
                }
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        btnSecondToMain = findViewById(R.id.btn_second_go_main);

        btnSecondToMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SecondActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        initSwitchLayout(WorkManager.getInstance(getApplicationContext()));
    }
}