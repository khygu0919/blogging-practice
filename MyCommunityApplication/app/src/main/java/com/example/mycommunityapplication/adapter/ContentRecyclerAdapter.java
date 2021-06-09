package com.example.mycommunityapplication.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mycommunityapplication.R;

import java.util.ArrayList;
import java.util.Collections;

public class ContentRecyclerAdapter extends RecyclerView.Adapter<ContentRecyclerAdapter.PostingViewHolder> implements ItemTouchHelperCallback.ItemTouchHelperListener {
    Boolean needFocus = true;
    private ArrayList<String> mData;
    private Context mContext;
    private OnStartDragListener mStartDragListener;

    public ContentRecyclerAdapter(Context context, OnStartDragListener startDragListener, ArrayList<String> list) {
        mData = list;
        mContext = context;
        mStartDragListener = startDragListener;
    }

    @NonNull
    @Override
    public PostingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.recyclerview_content_item, parent, false);
        PostingViewHolder vh = new PostingViewHolder(view);
        return vh;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onBindViewHolder(@NonNull final PostingViewHolder holder, int position) {
        final Boolean foo = holder.editTextContentItem.requestFocus();
        if (RecyclerView.NO_POSITION != position) {
            holder.editTextContentItem.setText(mData.get(position));
            if (needFocus.equals(true)) {
                holder.editTextContentItem.post(new Runnable() {
                    @Override
                    public void run() {
                        if (foo) {
                            InputMethodManager imm = (InputMethodManager)
                                    holder.editTextContentItem.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.showSoftInput(holder.editTextContentItem, InputMethodManager.SHOW_IMPLICIT);
                        }
                    }
                });
                needFocus = false;
            }
        }
        holder.imageViewMoveContentItem.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getActionMasked() == MotionEvent.ACTION_DOWN) {
                    mStartDragListener.onStartDrag(holder);
                }
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void removeItem(int position) {
        mData.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        Collections.swap(mData, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
        return true;
    }

    public interface OnStartDragListener {
        void onStartDrag(PostingViewHolder holder);
    }

    public class PostingViewHolder extends RecyclerView.ViewHolder {
        EditText editTextContentItem;
        ImageView imageViewDeleteContentItem, imageViewMoveContentItem, imageViewContentItem;

        PostingViewHolder(final View itemView) {
            super(itemView);
            editTextContentItem = itemView.findViewById(R.id.editTextContentItem);
            imageViewContentItem = itemView.findViewById(R.id.imageViewContentItem);
            imageViewMoveContentItem = itemView.findViewById(R.id.imageViewMoveContentItem);
            editTextContentItem.requestFocus();
            editTextContentItem.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (!s.toString().equals("")) {
                        mData.set(getAdapterPosition(), s.toString());
                    }
                    Log.d("DATA:", mData.toString());
                }
            });

            imageViewDeleteContentItem = itemView.findViewById(R.id.imageViewDeleteContentItem);
            imageViewDeleteContentItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION) {
                        removeItem(pos);
                    }
                }
            });
        }
    }


}
