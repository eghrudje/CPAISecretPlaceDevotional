package com.example.cpaisecretplacedevotional;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import service.ItemClickListener;

public class ListOfDevotionalNotesHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    private long mLastClickTime = System.currentTimeMillis();
    private static final long CLICK_TIME_INTERVAL =300;
    LinearLayout devotionalNoteLinearLayout;
    ItemClickListener itemClickListener;
    TextView devotionalTitleTextView, devotionalCreatedTextView, devotionalUpdatedTextView;



    public ListOfDevotionalNotesHolder(@NonNull View itemView) {
        super(itemView);

        this.devotionalTitleTextView = itemView.findViewById(R.id.titleOfDevId);
        this.devotionalCreatedTextView = itemView.findViewById(R.id.dateDevotionalCreatedID);
        this.devotionalUpdatedTextView = itemView.findViewById(R.id.dateDevotionalUpdatedID);
        this.devotionalNoteLinearLayout = itemView.findViewById(R.id.linearLayoutId);

        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        long now = System.currentTimeMillis();
        if(now - mLastClickTime < CLICK_TIME_INTERVAL) {
            return;
        }
        mLastClickTime = now;
        this.itemClickListener.onItemClickListener(v, getLayoutPosition());
    }
    public void setItemClickListener(ItemClickListener ic){
        this.itemClickListener = ic;
    }
}
