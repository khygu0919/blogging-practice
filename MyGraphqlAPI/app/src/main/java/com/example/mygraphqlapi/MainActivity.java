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

public class MainActivity extends AppCompatActivity {
    private Button btnInsertScore, btnUpdateScore, btnGetScore, btnMainToSecond;
    private EditText editTextGradeMain, editTextClassCodeMain, editTextSubjectTypeMain, editTextNameMain, editTextScoreMain;
    private TextView txtShowResult;
    private JSONObject mainScoreInput = new JSONObject();
    private JSONObject response;
    private int grade, classCode, score;
    private String subjectType, name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initLayout();
    }

    private void initLayout() {
        btnInsertScore = findViewById(R.id.btnInsertScore);
        btnUpdateScore = findViewById(R.id.btnUpdateScore);
        btnGetScore = findViewById(R.id.btnGetScore);
        editTextGradeMain = findViewById(R.id.editTextGradeMain);
        editTextClassCodeMain = findViewById(R.id.editTextClassCodeMain);
        editTextSubjectTypeMain = findViewById(R.id.editTextSubjectTypeMain);
        editTextNameMain = findViewById(R.id.editTextNameMain);
        editTextScoreMain = findViewById(R.id.editTextScoreMain);
        btnMainToSecond = findViewById(R.id.btnMainToSecond);
        txtShowResult = findViewById(R.id.txtShowResult);
        btnMainToSecond.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SecondActivity.class);
                startActivity(intent);
                finish();
            }
        });
        btnInsertScore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    buildScoreInput("Mutation");
                    AsyncGraphQLRequest asyncGraphQLRequest = new AsyncGraphQLRequest(mainScoreInput, "Insert");
                    response = asyncGraphQLRequest.execute().get();
                    txtShowResult.setText(response.toString());
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            }
        });
        btnUpdateScore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    buildScoreInput("Mutation");
                    AsyncGraphQLRequest asyncGraphQLRequest = new AsyncGraphQLRequest(mainScoreInput, "Update");
                    response = asyncGraphQLRequest.execute().get();
                    txtShowResult.setText(response.toString());
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            }
        });
        btnGetScore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    buildScoreInput("Query");
                    AsyncGraphQLRequest asyncGraphQLRequest = new AsyncGraphQLRequest(mainScoreInput, "Get");
                    response = asyncGraphQLRequest.execute().get();
                    txtShowResult.setText(response.toString());
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    private void buildScoreInput(String queryType) {
        grade = Integer.parseInt(editTextGradeMain.getText().toString());
        classCode = Integer.parseInt(editTextClassCodeMain.getText().toString());
        subjectType = editTextSubjectTypeMain.getText().toString();
        name = editTextNameMain.getText().toString();
        if (queryType.equals("Mutation")) {
            score = Integer.parseInt(editTextScoreMain.getText().toString());
            try {
                mainScoreInput.put("grade", grade);
                mainScoreInput.put("classCode", classCode);
                mainScoreInput.put("subjectType", subjectType);
                mainScoreInput.put("name", name);
                mainScoreInput.put("score", score);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (queryType.equals("Query")) {
            try {
                mainScoreInput.put("grade", grade);
                mainScoreInput.put("classCode", classCode);
                mainScoreInput.put("subjectType", subjectType);
                mainScoreInput.put("name", name);
            }  catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}