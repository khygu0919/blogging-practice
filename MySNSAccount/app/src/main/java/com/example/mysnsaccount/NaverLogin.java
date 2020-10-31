package com.example.mysnsaccount;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.nhn.android.naverlogin.OAuthLogin;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class NaverLogin extends Activity {
    final String NAVER_CLIENT_ID = "zQtbz1mqovnAUiFlo7RB";
    final String NAVER_CLIENT_SECRET = "fMosFMzLxn";
    final String NAVER_RESPONSE_CODE = "00"; // 정상 반환 시 코드
    final String NAVER_REFRESH_URL = "https://nid.naver.com/oauth2.0/token?grant_type=refresh_token&";
    final String[] NAVER_JSON_KEY = {"id", "nickname"};

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        OAuthLogin mNaverLoginModule = OAuthLogin.getInstance();

        String accessToken = mNaverLoginModule.getAccessToken(getApplicationContext());
        String refresh = mNaverLoginModule.getRefreshToken(getApplicationContext());

        if (accessToken != null) {
            ReqNHNUserInfo reqNaverUserInfo = new ReqNHNUserInfo();
            reqNaverUserInfo.execute(accessToken);
        } else {
            RefreshNHNToken tokenRefresh = new RefreshNHNToken();
            String refreshedToken = "";
            try {
                refreshedToken = tokenRefresh.execute(refresh).get();
            } catch (Exception e) {
                e.printStackTrace();
            }
            ReqNHNUserInfo reqNaverUserInfo = new ReqNHNUserInfo();
            reqNaverUserInfo.execute(refreshedToken);
        }

    }

    class RefreshNHNToken extends AsyncTask<String, Void, String> {
        String result;
        String refreshedToken;

        @Override
        protected String doInBackground(String... strings) {
            String refreshToken = strings[0];
            try {
                String apiURL;
                apiURL = NAVER_REFRESH_URL;
                apiURL += "client_id=" + NAVER_CLIENT_ID;
                apiURL += "&client_secret=" + NAVER_CLIENT_SECRET;
                apiURL += "&refresh_token=" + refreshToken;
                URL url = new URL(apiURL);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("GET");
                int responseCode = con.getResponseCode();
                BufferedReader br;
                if (responseCode == 200) {
                    br = new BufferedReader(new InputStreamReader(con.getInputStream()));
                } else {
                    br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
                }
                String inputLine;
                StringBuilder response = new StringBuilder();

                while ((inputLine = br.readLine()) != null) {
                    response.append(inputLine);
                }
                result = response.toString();
                br.close();
                JSONObject object = new JSONObject(result);
                refreshedToken = object.getString("access_token");
            } catch (Exception e) {
                Log.e("Error RefreshNHNToken", e.toString());
            }
            return refreshedToken;
        }
    }

    class ReqNHNUserInfo extends AsyncTask<String, Void, String> {
        String result;

        @Override
        protected String doInBackground(String... strings) {
            String token = strings[0];// 네이버 로그인 접근 토큰;
            String header = "Bearer " + token; // Bearer 다음에 공백 추가
            try {
                String apiURL = "https://openapi.naver.com/v1/nid/me";
                URL url = new URL(apiURL);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("GET");
                con.setRequestProperty("Authorization", header);
                int responseCode = con.getResponseCode();
                BufferedReader br;
                if (responseCode == 200) { // 정상 호출
                    br = new BufferedReader(new InputStreamReader(con.getInputStream()));
                } else {  // 에러 발생
                    br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
                }
                String inputLine;
                StringBuilder response = new StringBuilder();

                while ((inputLine = br.readLine()) != null) {
                    response.append(inputLine);
                }
                result = response.toString();
                br.close();
                Log.d("ReqNHNUserInfo Response", result);
            } catch (Exception e) {
                Log.e("Error ReqNHNUserInfo", e.toString());
            }
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONObject object = new JSONObject(result);
                if (object.getString("resultcode").equals(NAVER_RESPONSE_CODE)) {
                    List<String> userInfo = new ArrayList<>();
                    JSONObject jsonObject = new JSONObject(object.getString("response"));
                    userInfo.add(String.format("%s-%s", "NAVER", jsonObject.getString(NAVER_JSON_KEY[0])));
                    userInfo.add(jsonObject.getString(NAVER_JSON_KEY[1]));
                    GlobalHelper mGlobalHelper = new GlobalHelper();
                    mGlobalHelper.setGlobalUserLoginInfo(userInfo);
                    Intent intent = new Intent(NaverLogin.this, SecondActivity.class);
                    startActivity(intent);
                    finish();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    static class DeleteTokenTask extends AsyncTask<Context, Void, Boolean> {
        Context context;
        SecondActivity secondActivity;

        public DeleteTokenTask(Context mContext, SecondActivity mActivity) {
            this.context = mContext;
            this.secondActivity = mActivity;
        }
        @Override
        protected Boolean doInBackground(Context... contexts) {
            return OAuthLogin.getInstance().logoutAndDeleteToken(contexts[0]);
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            secondActivity.directToMainActivity(result);
        }
    }

}
