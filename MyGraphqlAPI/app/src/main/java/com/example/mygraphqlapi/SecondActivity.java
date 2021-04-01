package com.example.mygraphqlapi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.mygraphqlapi.NetworkFunction.AsyncGraphQLRequest;

import java.util.concurrent.ExecutionException;

public class SecondActivity extends AppCompatActivity {
    private EditText editTextGradeSecond, editTextClassCodeSecond, editTextSubjectTypeSecond, editTextNameSecond;
    private TextView textViewPostResult;
    private Button btnListUserScore, btnDeleteUserScore, btnSecondToMain;
    private JSONObject secondScoreInput = new JSONObject();
    private JSONObject response;
    private int grade, classCode;
    private String subjectType, name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        initLayout();
    }
    private void initLayout() {
        editTextClassCodeSecond = findViewById(R.id.editTextClassCodeSecond);
        editTextGradeSecond = findViewById(R.id.editTextGradeSecond);
        editTextSubjectTypeSecond = findViewById(R.id.editTextSubjectTypeSecond);
        editTextNameSecond = findViewById(R.id.editTextNameSecond);
        btnListUserScore = findViewById(R.id.btnListUserScore);
        btnDeleteUserScore = findViewById(R.id.btnDeleteUserScore);
        textViewPostResult = findViewById(R.id.textViewPostResult);
        btnSecondToMain = findViewById(R.id.btnSecondToMain);

        btnListUserScore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    buildSecondScoreInput("Query");
                    AsyncGraphQLRequest asyncGraphQLRequest = new AsyncGraphQLRequest(secondScoreInput, "List");
                    response = asyncGraphQLRequest.execute().get();
                    textViewPostResult.setText(response.toString());
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            }
        });

        btnDeleteUserScore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    buildSecondScoreInput("Mutation");
                    AsyncGraphQLRequest asyncGraphQLRequest = new AsyncGraphQLRequest(secondScoreInput, "Delete");
                    response = asyncGraphQLRequest.execute().get();
                    textViewPostResult.setText(response.toString());
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            }
        });

        btnSecondToMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SecondActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void buildSecondScoreInput(String queryType) {
        grade = Integer.parseInt(editTextGradeSecond.getText().toString());
        classCode = Integer.parseInt(editTextClassCodeSecond.getText().toString());
        name = editTextNameSecond.getText().toString();
        if (queryType.equals("Query")) {
            try{
                secondScoreInput.put("grade", grade);
                secondScoreInput.put("classCode", classCode);
                secondScoreInput.put("name", name);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (queryType.equals("Mutation")) {
            try{
                subjectType = editTextSubjectTypeSecond.getText().toString();
                secondScoreInput.put("grade", grade);
                secondScoreInput.put("classCode", classCode);
                secondScoreInput.put("subjectType", subjectType);
                secondScoreInput.put("name", name);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}