package com.example.mycommunityapplication.util;

import android.accounts.NetworkErrorException;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class NetworkFunction {
    static String COMMUNITY_GRAPHQL_API = "https://9g1fl6q64m.execute-api.ap-northeast-2.amazonaws.com/pormoking_api_stage/v2";

        public static class AsyncCreatePost extends AsyncTask<JSONObject, Void, JSONObject> {
            JSONObject input;

            public AsyncCreatePost(JSONObject input){
                this.input = input;
            }

        @Override
        protected JSONObject doInBackground(JSONObject... params) {
            JSONObject result = new JSONObject();
            try {
                result = CreatePost(input);
            } catch (Exception e) {
                Log.e("error", e.toString());
            }
            return result;
        }
    }

    public static JSONObject CreatePost(JSONObject createPostInput) {
        String bodyJsonString;
        JSONObject response;
        try {
            URL url = new URL(COMMUNITY_GRAPHQL_API);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(3000);
            conn.setConnectTimeout(3000);
            conn.setDoInput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setUseCaches(false);
            conn.setDoOutput(true);
            JSONObject body = new JSONObject();
            JSONObject input = new JSONObject();
            body.put("query", "mutation CreatePost " +
                    "($input: CreatePostInput!)" +
                    "{createPost(input:$input){author, category, topic, title, content}}");
            body.put("input", createPostInput);

            bodyJsonString = body.toString();
            DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
            wr.write(bodyJsonString.getBytes(StandardCharsets.UTF_8));
            wr.flush();
            wr.close();
            conn.connect();

            InputStream responseInputStream;
            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                conn.connect();
                responseInputStream = conn.getInputStream();
            } else {
                throw new NetworkErrorException("Can't access to the server");
            }

            InputStreamReader inputStreamReader = new InputStreamReader(responseInputStream, "EUC-KR");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            StringBuilder sb = new StringBuilder();
            String line;

            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }

            bufferedReader.close();
            conn.disconnect();

            String result = sb.toString().trim();
            Log.e("result", result);
            response = new JSONObject(result);

        } catch (Exception e) {
            e.printStackTrace();
            return new JSONObject();
        }
        return response;
    }
}
