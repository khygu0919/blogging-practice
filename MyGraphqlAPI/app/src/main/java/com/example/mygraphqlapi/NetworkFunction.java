package com.example.mygraphqlapi;

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
    static String TEST_GRAPHQL_API = "API_URL";

    public static class AsyncGraphQLRequest extends AsyncTask<Void, Void, JSONObject> {
        JSONObject input;
        String queryType;

        public AsyncGraphQLRequest(JSONObject input, String queryType){
            this.input = input;
            this.queryType = queryType;
        }

        @Override
        protected JSONObject doInBackground(Void... params) {
            JSONObject result = new JSONObject();
            try {
                result = graphQLRequest(input, queryType);
            } catch (Exception e) {
                Log.e("error", e.toString());
            }
            return result;
        }
    }

    public static JSONObject graphQLRequest(JSONObject queryScoreInput, String queryType) {
        JSONObject queryScoreData = new JSONObject();
        JSONArray queryScoreList = new JSONArray();
        String bodyJsonString;
        try {
            URL url = new URL(TEST_GRAPHQL_API);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(3000);
            conn.setConnectTimeout(3000);
            conn.setDoInput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setUseCaches(false);
            conn.setDoOutput(true);
            JSONObject body = new JSONObject();
            if (queryType.equals("Get")) {
                body.put("query", "query GetScore " +
                        "($input: GetScoreInput!)" +
                        "{getScore(input: $input) {grade, classCode, subjectType, name, score}}");
            } else if (queryType.equals("List")) {
                body.put("query", "query ListScore " +
                        "($input: GetScoreInput!)" +
                        "{listScore(input: $input) {subjectType, name, score}}");
            } else if (queryType.equals("Insert")) {
                body.put("query", "mutation CreateScore " +
                        "($input: PostScoreInput!)" +
                        "{createScore(input:$input){grade, classCode, subjectType, name, score}}");
            } else if (queryType.equals("Update")) {
                body.put("query", "mutation UpdateScore " +
                        "($input: PostScoreInput!)" +
                        "{updateScore(input:$input){grade, classCode, subjectType, name, score}}");
            } else if (queryType.equals("Delete")) {
                body.put("query", "mutation DeleteScore " +
                        "($input: PostScoreInput!)" +
                        "{deleteScore(input:$input){grade, classCode, subjectType, name, score}}");
            }

            body.put("input", queryScoreInput);

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
            JSONObject jsonObject = new JSONObject(result);
            if (queryType.equals("Get")) {
                queryScoreData = jsonObject.getJSONObject("getScore");
            } else if (queryType.equals("List")) {
                queryScoreData = jsonObject;
            } else if (queryType.equals("Insert")) {
                queryScoreData = jsonObject.getJSONObject("createScore");
            } else if (queryType.equals("Update")) {
                queryScoreData = jsonObject.getJSONObject("updateScore");
            } else if (queryType.equals("Delete")) {
                queryScoreData = jsonObject.getJSONObject("deleteScore");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new JSONObject();
        }
        return queryScoreData;
    }
}
