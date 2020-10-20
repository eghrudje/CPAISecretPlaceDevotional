package com.example.cpaisecretplacedevotional;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import service.Devotional;
import service.DevotionalRealmHelper;
import service.ItemClickListener;
import service.MonthDevotional;
import ui.DevotionalHolder;

public class MonthsDevotionalInAYearAdapter extends RecyclerView.Adapter<MonthsDevotionalInAYearHolder> {
    private static final String TAG = "MonthsPerYearAdapter";
    Context c;
    ArrayList<MonthDevotional> arrayListOfMonths;
    DevotionalRealmHelper devotionalRealmHelper = DevotionalRealmHelper.getInstance(c);

    public MonthsDevotionalInAYearAdapter(Context c, ArrayList<MonthDevotional> arrayListOfMonths) {
        this.c = c;
        this.arrayListOfMonths = arrayListOfMonths;
        Log.i(TAG, "MonthsDevotionalInAYearAdapter: " + arrayListOfMonths.toString());
    }

    @NonNull
    @Override
    public MonthsDevotionalInAYearHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rowmonthlistperyear, null);
        return new MonthsDevotionalInAYearHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull final MonthsDevotionalInAYearHolder holder, int position) {

        holder.monthNameTextView.setText(arrayListOfMonths.get(position).getMonthName());


        final String monthId =  arrayListOfMonths.get(position).getMonthDevotional_id();
        final String monthNames = arrayListOfMonths.get(position).getMonthName();
        final String Image =  arrayListOfMonths.get(position).getMonthImage();

        final int res = c.getResources().getIdentifier(Image.toLowerCase(), "drawable", c.getPackageName());

        Glide.with(c).load(res).into(holder.monthImageView);
        Log.i(TAG, "onBindViewHolder(MonthDevotionalInAYear): " + res);
        Log.i(TAG, "onBindViewHolder(MonthDevotionalInAYear): " + monthId);
        Log.i(TAG, "onBindViewHolder(MonthDevotionalInAYear): " + monthNames);
        Log.i(TAG, "onBindViewHolder(MonthDevotionalInAYear): " + Image);

        final ArrayList<Devotional> devotionalsPerMonth = devotionalRealmHelper.fetchListOfDevotionalsPerMonth(monthId);

        if(devotionalsPerMonth.isEmpty()) {
            holder.downloadMonthDevotionalImageButton.setVisibility(View.VISIBLE);
        } else {
            holder.downloadMonthDevotionalImageButton.setVisibility(View.GONE);
        }

        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onItemClickListener(View v, int position) {
                Intent monthsDevotionalInYearIntent = new Intent(c, DailyDevotionalsPerMonthActivity.class);
                monthsDevotionalInYearIntent.putExtra("MonthIdentity", monthId);
                monthsDevotionalInYearIntent.putExtra("monthName", monthNames);
                monthsDevotionalInYearIntent.putExtra("monthImage", Image);
                c.startActivity(monthsDevotionalInYearIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayListOfMonths.size();
    }
}
