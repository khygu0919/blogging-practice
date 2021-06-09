package com.example.mycommunityapplication;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

public class CommunityActivity extends AppCompatActivity {
    ImageView btnCommunityPost;
    String nickname, userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community);
        Intent communityIntent = getIntent();
        nickname = communityIntent.getStringExtra("nickname");
        userId = "bot" + nickname;
        initView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //return super.onCreateOptionsMenu(menu);
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.community_tool_bar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.btnCommunityNotification:
                Toast.makeText(getApplicationContext(), "알림버튼클릭", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.btnCommunitySearch:
                Toast.makeText(getApplicationContext(), "검색버튼클릭", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        goToMainActivity();
    }

    private void initView() {
        Toolbar communityToolbar = findViewById(R.id.communityToolbar);
        setSupportActionBar(communityToolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        communityToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToMainActivity();
            }
        });

        btnCommunityPost = findViewById(R.id.btnCommunityPost);
        btnCommunityPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToPostingActivity();
            }
        });
    }

    private void goToMainActivity() {
        Intent intent = new Intent(CommunityActivity.this, MainActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.left_in, R.anim.right_out);
        finish();
    }

    private void goToPostingActivity() {
        Intent intent = new Intent(CommunityActivity.this, PostingActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.right_in, R.anim.left_out);
        finish();
    }
}