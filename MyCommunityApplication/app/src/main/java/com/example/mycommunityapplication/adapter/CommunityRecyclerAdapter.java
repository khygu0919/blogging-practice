package com.example.mycommunityapplication.adapter;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mycommunityapplication.R;

import org.jetbrains.annotations.NotNull;

public class CommunityRecyclerAdapter extends RecyclerView.Adapter<CommunityRecyclerAdapter.CommunityViewHolder> {

    @NotNull
    @Override
    public CommunityRecyclerAdapter.CommunityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.recyclerview_post_item, parent, false);
        CommunityRecyclerAdapter.CommunityViewHolder vh = new CommunityRecyclerAdapter.CommunityViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull CommunityViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
    public class CommunityViewHolder extends RecyclerView.ViewHolder {
        EditText editTextContentItem;
        ImageView imageViewDeleteContentItem, imageViewMoveContentItem, imageViewContentItem;

        CommunityViewHolder(final View itemView) {
            super(itemView);

        }
    }


}
