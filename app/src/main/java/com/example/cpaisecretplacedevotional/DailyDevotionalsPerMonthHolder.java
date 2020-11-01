package com.example.cpaisecretplacedevotional;

import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import service.ItemClickListener;

public class DailyDevotionalsPerMonthHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    private static final String TAG = "DailyHolder";
    private long mLastClickTime = System.currentTimeMillis();
    private static final long CLICK_TIME_INTERVAL =300;
    TextView devotionalDate, devotionalTitle;
    RelativeLayout listOfDaysInMonthLayout;
    ItemClickListener itemClickListener;


    public DailyDevotionalsPerMonthHolder(@NonNull View itemView) {
        super(itemView);

        this.devotionalDate = itemView.findViewById(R.id.dateId);
        this.devotionalTitle = itemView.findViewById(R.id.devotionalTitle);
        this.listOfDaysInMonthLayout = itemView.findViewById(R.id.linearLayoutId);


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
