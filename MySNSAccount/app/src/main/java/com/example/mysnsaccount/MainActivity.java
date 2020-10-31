package com.example.mysnsaccount;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.kakao.auth.Session;
import com.kakao.usermgmt.LoginButton;
import com.example.mysnsaccount.KakaoLogin.KakaoSessionCallback;
import com.nhn.android.naverlogin.OAuthLogin;
import com.nhn.android.naverlogin.OAuthLoginHandler;
import com.nhn.android.naverlogin.data.OAuthLoginState;
import com.nhn.android.naverlogin.ui.view.OAuthLoginButton;

public class MainActivity extends AppCompatActivity {
    private Button mKakaoLoginBtn, mNaverLoginBtn, mGoogleLoginBtn;
    private LoginButton mKakaoLoginBtnBasic;
    private OAuthLoginButton mNaverLoginBtnBasic;
    private KakaoSessionCallback sessionCallback;
    private OAuthLogin mNaverLoginModule;
    private NaverLogin mNaverLoginAuth;
    final String NAVER_CLIENT_ID = "zQtbz1mqovnAUiFlo7RB";
    final String NAVER_CLIENT_SECRET = "fMosFMzLxn";
    private FirebaseAuth mGoogleLoginModule;

    @SuppressLint("HandlerLeak")
    private OAuthLoginHandler mNaverLoginHandler = new OAuthLoginHandler() {
        @Override
        public void run(boolean success) {
            if (success) {
                Intent intent = new Intent(MainActivity.this, NaverLogin.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(getApplicationContext(), "Naver Login Failed!", Toast.LENGTH_LONG);
            }
        }
    };

    Button.OnClickListener mGoogleLoginListener = new ImageView.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(MainActivity.this, GoogleLogin.class);
            startActivity(intent);
            finish();
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mKakaoLoginBtn = findViewById(R.id.btn_kakao_login);
        mNaverLoginBtn = findViewById(R.id.btn_naver_login);
        mGoogleLoginBtn = findViewById(R.id.btn_google_login);
        mKakaoLoginBtnBasic = findViewById(R.id.btn_kakao_login_basic);
        mNaverLoginBtnBasic = findViewById(R.id.btn_naver_login_basic);
        mNaverLoginBtnBasic.setOAuthLoginHandler(mNaverLoginHandler);

        mKakaoLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mKakaoLoginBtnBasic.performClick();
            }
        });

        mNaverLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mNaverLoginBtnBasic.performClick();
            }
        });

        mGoogleLoginBtn.setOnClickListener(mGoogleLoginListener);

        mNaverLoginModule = OAuthLogin.getInstance();
        mNaverLoginModule.init(
                this
                , NAVER_CLIENT_ID
                , NAVER_CLIENT_SECRET
                , "네이버 아이디로 로그인"
        );

        mGoogleLoginModule = FirebaseAuth.getInstance();
        if (!HasKakaoSession() && !HasNaverSession() && !HasGoogleSession()) {
            sessionCallback = new KakaoSessionCallback(getApplicationContext(), MainActivity.this);
            Session.getCurrentSession().addCallback(sessionCallback);
        } else if (HasKakaoSession()) {
            sessionCallback = new KakaoSessionCallback(getApplicationContext(), MainActivity.this);
            Session.getCurrentSession().addCallback(sessionCallback);
            Session.getCurrentSession().checkAndImplicitOpen();
        } else if (HasNaverSession()) {
            Intent intent = new Intent(MainActivity.this, NaverLogin.class);
            startActivity(intent);
            finish();
        } else if (HasGoogleSession()) {
            Intent intent = new Intent(MainActivity.this, GoogleLogin.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // 세션 콜백 삭제
        Session.getCurrentSession().removeCallback(sessionCallback);
    }

    private boolean HasKakaoSession() {
        if (!Session.getCurrentSession().checkAndImplicitOpen()) {
            return false;
        }
        return true;
    }

    private boolean HasNaverSession() {
        if (OAuthLoginState.NEED_LOGIN.equals(mNaverLoginModule.getState(getApplicationContext())) ||
                OAuthLoginState.NEED_INIT.equals(mNaverLoginModule.getState(getApplicationContext()))) {
            return false;
        }
        return true;
    }

    private boolean HasGoogleSession() {
        if (mGoogleLoginModule.getCurrentUser() == null) {
            return false;
        }
        return true;
    }

    public void directToSecondActivity(Boolean result) {
        Intent intent = new Intent(MainActivity.this, SecondActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        if (result) {
            Toast.makeText(getApplicationContext(), "로그인 성공!", Toast.LENGTH_SHORT).show();
            startActivity(intent);;
            finish();
        }
    }
}