package com.example.cpaisecretplacedevotional;

import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import service.ItemClickListener;

public class MonthsDevotionalInAYearHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    private long mLastClickTime = System.currentTimeMillis();
    private static final long CLICK_TIME_INTERVAL =300;
    ImageView monthImageView;
    TextView monthNameTextView;
    ImageButton downloadMonthDevotionalImageButton;
    ItemClickListener itemClickListener;
    CardView monthsInAYearCardView;


    public MonthsDevotionalInAYearHolder(@NonNull View itemView) {
        super(itemView);

        this.monthImageView = itemView.findViewById(R.id.monthImageId);
        this.monthNameTextView = itemView.findViewById(R.id.monthNameId);
        this.downloadMonthDevotionalImageButton = itemView.findViewById(R.id.downloadMonthDevotionalId2);
        this.monthsInAYearCardView = itemView.findViewById(R.id.monthsPerYearCardView);


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
