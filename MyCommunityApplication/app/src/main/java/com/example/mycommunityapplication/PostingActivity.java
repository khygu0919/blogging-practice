package com.example.mycommunityapplication;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mycommunityapplication.adapter.ContentRecyclerAdapter;
import com.example.mycommunityapplication.adapter.ContentRecyclerAdapter.PostingViewHolder;
import com.example.mycommunityapplication.adapter.DialogRecyclerAdapter;
import com.example.mycommunityapplication.adapter.ItemTouchHelperCallback;
import com.example.mycommunityapplication.util.NetworkFunction.AsyncCreatePost;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;


public class PostingActivity extends AppCompatActivity implements ContentRecyclerAdapter.OnStartDragListener {
    private static final String TAG_TEXT = "text";
    public static TextView textViewPostingTopic;
    public static Dialog dialog;
    String[] topicArray = {"경찰승진", "간후보", "순경", "해경", "7급", "9급", "카테고리 없음"};
    private Toolbar toolbarPosting;
    private LinearLayout linearLayoutTopicPosting;
    private LinearLayout linearLayoutContentPosting;
    private RecyclerView dialogRecyclerView, recyclerViewContentPosting;
    private ContentRecyclerAdapter contentRecyclerAdapter;
    private Button btnPostingComplete;
    private ImageView btnPostingKeyboard, btnPostingPicture, btnPostingVideo;
    private EditText editTextPostingPostTitle;
    private ArrayList<String> contentArrayList;
    private ItemTouchHelper itemTouchHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posting);
        initView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.posting_appbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.btnPostingComplete:
                JSONObject postJsonInput = new JSONObject();
                if (editTextPostingPostTitle.getText().toString().equals("") | contentArrayList.size() == 0) {
                    //TODO: Alert Dialog로 경고 띄워주기
                    Toast.makeText(getApplicationContext(), "제목, 내용을 입력하세요.", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        StringBuilder contentBuilder = new StringBuilder();
                        for (int i = 0; i < contentArrayList.size(); i++) {
                            String recyclerItemContent = contentArrayList.get(i);
                            if (!recyclerItemContent.equals("")) {
                                contentBuilder.append(recyclerItemContent).append("\n");
                            }
                        }
                        if (contentBuilder.toString().equals("")) {
                            Toast.makeText(getApplicationContext(), "내용을 입력하세요.", Toast.LENGTH_SHORT).show();
                        } else {
                            postJsonInput.put("author", "example_auth_01");
                            postJsonInput.put("category", "GENERAL");
                            //postJsonInput.put("topic", textViewPostingTopic.getText().toString());
                            postJsonInput.put("topic", "POLICE_PROMOTION_EXAM");
                            postJsonInput.put("title", editTextPostingPostTitle.getText().toString());
                            postJsonInput.put("content", contentBuilder.toString());
                            Log.e("jsonInput", postJsonInput.toString());
                            Toast.makeText(getApplicationContext(), postJsonInput.toString(), Toast.LENGTH_SHORT).show();
                            AsyncCreatePost asyncCreatePost = new AsyncCreatePost(postJsonInput);
                            JSONObject response = asyncCreatePost.execute().get();
                        }

                    } catch (JSONException | InterruptedException | ExecutionException e) {
                        e.printStackTrace();
                    }
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    public void onBackPressed() {
        goToCommunityActivity();
    }

    private void initView() {
        editTextPostingPostTitle = findViewById(R.id.editTextPostingPostTitle);
        showSoftKeyboard(editTextPostingPostTitle);

        toolbarPosting = findViewById(R.id.toolbarPosting);
        setSupportActionBar(toolbarPosting);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        toolbarPosting.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToCommunityActivity();
            }
        });
        linearLayoutTopicPosting = findViewById(R.id.linearLayoutTopicPosting);
        linearLayoutTopicPosting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlertDialogTopic();
            }
        });

        textViewPostingTopic = findViewById(R.id.textViewPostingTopic);

        recyclerViewContentPosting = findViewById(R.id.recyclerViewContentPosting);
        contentArrayList = new ArrayList<>();
        recyclerViewContentPosting.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        contentRecyclerAdapter = new ContentRecyclerAdapter(this, this, contentArrayList);
        recyclerViewContentPosting.setAdapter(contentRecyclerAdapter);
        //Todo: item 이동 1칸씩 되는 현상 개선, long click 말고 버튼 클릭으로 동작되게 수정
        ItemTouchHelperCallback mCallback = new ItemTouchHelperCallback(contentRecyclerAdapter);
        itemTouchHelper = new ItemTouchHelper(mCallback);
        itemTouchHelper.attachToRecyclerView(recyclerViewContentPosting);
        recyclerViewContentPosting.setItemAnimator(null);
        recyclerViewContentPosting.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
                if (e.getAction() != MotionEvent.ACTION_UP) {
                    return false;
                }
                View child = recyclerViewContentPosting.findChildViewUnder(e.getX(), e.getY());
                if (child != null) {
                    return false;
                } else {
                    if (contentRecyclerAdapter.getItemCount() == 0) {
                        contentArrayList.add("");
                        contentRecyclerAdapter.notifyItemChanged(0);
                    }
                    if (getCurrentFocus() != null) {
                        Log.e("view:", getCurrentFocus().toString());
                        showSoftKeyboard(getCurrentFocus());
                    }
                    return true;
                }
            }

            @Override
            public void onTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
            }
        });

        btnPostingKeyboard = findViewById(R.id.btnPostingKeyboard);
        btnPostingKeyboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contentArrayList.add("");
                contentRecyclerAdapter.notifyItemChanged(contentRecyclerAdapter.getItemCount() - 1);

                if (getCurrentFocus() != null) {
                    showSoftKeyboard(getCurrentFocus());
                }
            }
        });
        btnPostingPicture = findViewById(R.id.btnPostingPicture);
        btnPostingPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contentArrayList.add("");
                contentRecyclerAdapter.notifyItemChanged(contentRecyclerAdapter.getItemCount() - 1);

            }
        });
        btnPostingVideo = findViewById(R.id.btnPostingVideo);

        btnPostingComplete = findViewById(R.id.btnPostingComplete);

    }

    private void showAlertDialogTopic() {
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        dialog = new Dialog(this);

        display.getRealSize(size);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();

        LayoutInflater inf = getLayoutInflater();
        View dialogView = inf.inflate(R.layout.dialog_layout, null);

        lp.copyFrom(dialog.getWindow().getAttributes());
        int width = size.x;
        lp.width = width * 80 / 100;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.setContentView(dialogView);
        dialog.setCanceledOnTouchOutside(true);
        dialog.getWindow().setAttributes(lp);
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.addAll(Arrays.asList(topicArray));
        dialogRecyclerView = (RecyclerView) dialogView.findViewById(R.id.dialogRecyclerView);
        dialogRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        DialogRecyclerAdapter adapter = new DialogRecyclerAdapter(arrayList);
        dialogRecyclerView.setAdapter(adapter);
        dialog.show();
    }

    public void showSoftKeyboard(View view) {
        if (view.requestFocus()) {
            InputMethodManager imm = (InputMethodManager)
                    getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
        }
    }

    private void goToCommunityActivity() {
        Intent intent = new Intent(PostingActivity.this, CommunityActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.left_in, R.anim.right_out);
        finish();
    }

    @Override
    public void onStartDrag(PostingViewHolder holder) {
        itemTouchHelper.startDrag(holder);
    }
}