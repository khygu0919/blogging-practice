package com.example.mysnsaccount;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import com.kakao.auth.ISessionCallback;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.MeV2ResponseCallback;
import com.kakao.usermgmt.response.MeV2Response;
import com.kakao.util.exception.KakaoException;

import java.util.ArrayList;
import java.util.List;

public class KakaoLogin extends Activity {
    public static class KakaoSessionCallback implements ISessionCallback {
        private Context mContext;
        private MainActivity mainActivity;

        public KakaoSessionCallback(Context context, MainActivity activity) {
            this.mContext = context;
            this.mainActivity = activity;
        }

        @Override
        public void onSessionOpened() {
            requestMe();
        }

        @Override
        public void onSessionOpenFailed(KakaoException e) {
            Toast.makeText(mContext, "KaKao 로그인 오류가 발생했습니다. " + e.toString(), Toast.LENGTH_SHORT).show();
        }

        protected void requestMe() {
            UserManagement.getInstance().me(new MeV2ResponseCallback() {
                @Override
                public void onSessionClosed(ErrorResult errorResult) {
                    mainActivity.directToSecondActivity(false);
                }

                @Override
                public void onSuccess(MeV2Response result) {
                    List<String> userInfo = new ArrayList<>();
                    userInfo.add(String.valueOf(result.getId()));
                    userInfo.add(result.getKakaoAccount().getProfile().getNickname());
                    GlobalHelper mGlobalHelper = new GlobalHelper();
                    mGlobalHelper.setGlobalUserLoginInfo(userInfo);

                    mainActivity.directToSecondActivity(true);
                }
            });
        }
    }
}
