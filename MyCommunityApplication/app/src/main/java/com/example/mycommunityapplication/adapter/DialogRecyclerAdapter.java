package com.example.mycommunityapplication.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.mycommunityapplication.PostingActivity;
import com.example.mycommunityapplication.R;

import java.util.ArrayList;

public class DialogRecyclerAdapter extends RecyclerView.Adapter<DialogRecyclerAdapter.ViewHolder> {
    private ArrayList<String> mData = null;

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        ViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.textViewRecyclerItem);
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String topic = mData.get(getAdapterPosition());
                    PostingActivity.textViewPostingTopic.setText(topic);
                    if (topic.equals("카테고리 없음")) {
                        PostingActivity.textViewPostingTopic.setTextColor(Color.LTGRAY);
                    } else {
                        PostingActivity.textViewPostingTopic.setTextColor(Color.BLACK);
                    }
                    PostingActivity.dialog.dismiss();
                }
            });
        }
    }
    public DialogRecyclerAdapter(ArrayList<String> list) {
        mData = list;
    }

    @Override
    public DialogRecyclerAdapter.ViewHolder onCreateViewHolder (ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.recyclerview_topic_item, parent, false);
        DialogRecyclerAdapter.ViewHolder vh = new DialogRecyclerAdapter.ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(DialogRecyclerAdapter.ViewHolder holder, int position) {
        String text = mData.get(position);
        holder.textView.setText(text);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }
}
