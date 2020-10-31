package com.example.mysnsaccount;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class SecondActivity extends AppCompatActivity {
    private TextView tvSecondUserID, tvSecondNickname;
    private Button btnSecondLogout, btnSecondResign;
    private List<String> userInfo = new ArrayList<>();

    Button.OnClickListener mLogoutListener = new Button.OnClickListener() {
        @Override
        public void onClick(View v) {
            GlobalAuthHelper.accountLogout(getApplicationContext(),SecondActivity.this);
        }
    };

    Button.OnClickListener mResignListener = new Button.OnClickListener() {
        @Override
        public void onClick(View v) {
            GlobalAuthHelper.accountResign(getApplicationContext(), SecondActivity.this);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        tvSecondUserID = findViewById(R.id.tv_second_userid);
        tvSecondNickname = findViewById(R.id.tv_second_nickname);
        btnSecondLogout = findViewById(R.id.btn_second_logout);
        btnSecondResign = findViewById(R.id.btn_second_resign);

        initView();

        btnSecondLogout.setOnClickListener(mLogoutListener);
        btnSecondResign.setOnClickListener(mResignListener);

    }

    private void initView() {
        GlobalHelper mGlobalHelper = new GlobalHelper();
        userInfo = mGlobalHelper.getGlobalUserLoginInfo();
        tvSecondUserID.setText("UserId : " + userInfo.get(0));
        tvSecondNickname.setText("Nickname : " + userInfo.get(1));
    }

    public void directToMainActivity(Boolean result) {
        Intent intent = new Intent(SecondActivity.this, MainActivity.class);
        if (result) {
            Toast.makeText(getApplicationContext(), "성공!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplicationContext(), "실패! 다시 로그인해주세요.", Toast.LENGTH_SHORT).show();
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
