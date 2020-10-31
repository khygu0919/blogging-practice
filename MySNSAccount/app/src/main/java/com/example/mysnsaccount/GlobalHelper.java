package com.example.mysnsaccount;

import android.app.Application;
import android.content.Context;

import com.kakao.auth.ApprovalType;
import com.kakao.auth.AuthType;
import com.kakao.auth.IApplicationConfig;
import com.kakao.auth.ISessionConfig;
import com.kakao.auth.KakaoAdapter;
import com.kakao.auth.KakaoSDK;

import java.util.ArrayList;
import java.util.List;

public class GlobalHelper extends Application {
    private static volatile GlobalHelper mInstance = null;
    private static List<String> mGlobalUserLoginInfo = new ArrayList<>();

    public static GlobalHelper getGlobalApplicationContext() {
        if (mInstance == null) {
            throw new IllegalStateException("This Application does not GlobalAuthHelper");
        }
        return mInstance;
    }

    public static List<String> getGlobalUserLoginInfo() {
        return mGlobalUserLoginInfo;
    }

    public static void setGlobalUserLoginInfo(List<String> userLoginInfo) {
        mGlobalUserLoginInfo = userLoginInfo;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        KakaoSDK.init(new KakaoSDKAdapter());
    }

    public class KakaoSDKAdapter extends KakaoAdapter {
        @Override
        public ISessionConfig getSessionConfig() {
            return new ISessionConfig() {
                @Override
                public AuthType[] getAuthTypes() {
                    return new AuthType[]{AuthType.KAKAO_LOGIN_ALL};
                }

                @Override
                public boolean isUsingWebviewTimer() {
                    return false;
                }

                @Override
                public boolean isSecureMode() {
                    return false;
                }

                @Override
                public ApprovalType getApprovalType() {
                    return ApprovalType.INDIVIDUAL;
                }

                @Override
                public boolean isSaveFormData() {
                    return true;
                }
            };
        }

        @Override
        public IApplicationConfig getApplicationConfig() {
            return new IApplicationConfig() {
                @Override
                public Context getApplicationContext() {
                    return GlobalHelper.getGlobalApplicationContext();
                }
            };
        }
    }
}
