package com.example.cpaisecretplacedevotional;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

import service.Devotional;
import service.DevotionalRealmHelper;

public class DailyDevotionalsPerMonthActivity extends AppCompatActivity {
    private static final String TAG = "DailyDevotionals";
    DevotionalRealmHelper devotionalRealmHelper;
    RecyclerView listOfDailyDevotionalsRecyclerView;
    ImageView monthImageView;
    TextView monthTitleTextView;
    ProgressBar loadingMonthDevotionalProgressBar;
    DailyDevotionalsPerMonthAdapter dailyDevotionalsPerMonthAdapter;
    Context context;
    DatabaseReference referenceDT;
    ArrayList<Devotional> devotionalsInAMonthFromServer;
    TextView errorMessageTextView;
    Button retryButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_devotionals_per_month);

        ActionBar actionBar = getSupportActionBar();
        devotionalRealmHelper = DevotionalRealmHelper.getInstance(context);
        devotionalsInAMonthFromServer = new ArrayList<>();
        retryButton = findViewById(R.id.buttonRetryID);
        errorMessageTextView = findViewById(R.id.errorMessageID);

        listOfDailyDevotionalsRecyclerView = findViewById(R.id.recyclerViewDailyDevotionalsPerMonth);

        monthImageView = findViewById(R.id.monthImage);
        monthTitleTextView = findViewById(R.id.monthTitleId);
        loadingMonthDevotionalProgressBar = findViewById(R.id.progressBarID);

        Intent intent = getIntent();
        final String keyMonthIdentifier = intent.getStringExtra("MonthIdentity");
        String monthName = intent.getStringExtra("monthName");
        String monthPic = intent.getStringExtra("monthImage");

        int res1 = getResources().getIdentifier(monthPic.toLowerCase() , "drawable", getPackageName());

        monthTitleTextView.setText(monthName);
        Glide.with(this).load(res1).into(monthImageView);


        final ArrayList<Devotional> dailyDevotionalListPerMonth = devotionalRealmHelper.fetchListOfDevotionalsPerMonth(keyMonthIdentifier);
        Log.i(TAG, "onCreate(keyMonthIdentifier): " + keyMonthIdentifier);
        Log.i(TAG, "onCreate(DailyDevFromDatabase....List): " + dailyDevotionalListPerMonth);
        if (dailyDevotionalListPerMonth.isEmpty()) {
            loadingMonthDevotionalProgressBar.setVisibility(View.VISIBLE);
            listOfDailyDevotionalsRecyclerView.setVisibility(View.GONE);
            retryButton.setVisibility(View.GONE);
            errorMessageTextView.setVisibility(View.GONE);

            referenceDT = FirebaseDatabase.getInstance().getReference().child("devotionals");

            DatabaseReference databaseReference = referenceDT.child(keyMonthIdentifier);

            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()) {
                        Log.i(TAG, "onDataChange(dataSnapshot): " + dataSnapshot);
                        Devotional devotionalForThisMonth = dataSnapshot1.getValue(Devotional.class);
                        Log.i(TAG, "onDataChange(devotionalForThisMonth): " + devotionalForThisMonth);
                        dailyDevotionalListPerMonth.add(devotionalForThisMonth);
                        devotionalRealmHelper.savedDevotional(devotionalForThisMonth);

                        Log.i(TAG, "onDataChange(dataSnapshot1): " + dataSnapshot1);

                    }
                    if (!dailyDevotionalListPerMonth.isEmpty()) {
                        loadingMonthDevotionalProgressBar.setVisibility(View.GONE);
                        listOfDailyDevotionalsRecyclerView.setVisibility(View.VISIBLE);

                        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(DailyDevotionalsPerMonthActivity.this, DividerItemDecoration.VERTICAL);
                        dividerItemDecoration.setDrawable(ContextCompat.getDrawable(DailyDevotionalsPerMonthActivity.this, R.drawable.recyclerview_divider));
                        listOfDailyDevotionalsRecyclerView.addItemDecoration(dividerItemDecoration);

                        Collections.sort(dailyDevotionalListPerMonth, Devotional.devotionalComparatorDate);
                        listOfDailyDevotionalsRecyclerView.setLayoutManager(new LinearLayoutManager(DailyDevotionalsPerMonthActivity.this));
                        dailyDevotionalsPerMonthAdapter = new DailyDevotionalsPerMonthAdapter(DailyDevotionalsPerMonthActivity.this, dailyDevotionalListPerMonth);
                        listOfDailyDevotionalsRecyclerView.setAdapter(dailyDevotionalsPerMonthAdapter);

                    }else{
                        loadingMonthDevotionalProgressBar.setVisibility(View.GONE);
                        errorMessageTextView.setVisibility(View.VISIBLE);
                        retryButton.setVisibility(View.VISIBLE);
                    }


                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            Log.i(TAG, "onCreate(DailyDevDatabase): " + dailyDevotionalListPerMonth);
        } else {
            listOfDailyDevotionalsRecyclerView.setVisibility(View.VISIBLE);
            loadingMonthDevotionalProgressBar.setVisibility(View.GONE);

            DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(DailyDevotionalsPerMonthActivity.this, DividerItemDecoration.VERTICAL);
            dividerItemDecoration.setDrawable(ContextCompat.getDrawable(DailyDevotionalsPerMonthActivity.this, R.drawable.recyclerview_divider));
            listOfDailyDevotionalsRecyclerView.addItemDecoration(dividerItemDecoration);

            Collections.sort(dailyDevotionalListPerMonth, Devotional.devotionalComparatorDate);
            listOfDailyDevotionalsRecyclerView.setLayoutManager(new LinearLayoutManager(DailyDevotionalsPerMonthActivity.this));
            dailyDevotionalsPerMonthAdapter = new DailyDevotionalsPerMonthAdapter(DailyDevotionalsPerMonthActivity.this, dailyDevotionalListPerMonth);
            listOfDailyDevotionalsRecyclerView.setAdapter(dailyDevotionalsPerMonthAdapter);
        }


//        Devotional dev = new Devotional();
//        if (dev.getMonthYearName() == keyMonthIdentifier){
//            devotionalRealmHelper.fetchMonthDevotionalFromDatabase(keyMonthIdentifier);
//        }
        retryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingMonthDevotionalProgressBar.setVisibility(View.VISIBLE);

                final ArrayList<Devotional> dailyDevotionalListPerMonth = devotionalRealmHelper.fetchListOfDevotionalsPerMonth(keyMonthIdentifier);
                Log.i(TAG, "onCreate(keyMonthIdentifier): " + keyMonthIdentifier);
                Log.i(TAG, "onCreate(DailyDevFromDatabase....List): " + dailyDevotionalListPerMonth);
                if (dailyDevotionalListPerMonth.isEmpty()) {
                    loadingMonthDevotionalProgressBar.setVisibility(View.VISIBLE);
                    listOfDailyDevotionalsRecyclerView.setVisibility(View.GONE);
                    retryButton.setVisibility(View.GONE);
                    errorMessageTextView.setVisibility(View.GONE);

                    referenceDT = FirebaseDatabase.getInstance().getReference().child("devotionals");

                    DatabaseReference databaseReference = referenceDT.child(keyMonthIdentifier);

                    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()) {
                                Log.i(TAG, "onDataChange(dataSnapshot): " + dataSnapshot);
                                Devotional devotionalForThisMonth = dataSnapshot1.getValue(Devotional.class);
                                Log.i(TAG, "onDataChange(devotionalForThisMonth): " + devotionalForThisMonth);
                                dailyDevotionalListPerMonth.add(devotionalForThisMonth);
                                devotionalRealmHelper.savedDevotional(devotionalForThisMonth);

                                Log.i(TAG, "onDataChange(dataSnapshot1): " + dataSnapshot1);

                            }
                            if (!dailyDevotionalListPerMonth.isEmpty()) {
                                loadingMonthDevotionalProgressBar.setVisibility(View.GONE);
                                listOfDailyDevotionalsRecyclerView.setVisibility(View.VISIBLE);

                                DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(DailyDevotionalsPerMonthActivity.this, DividerItemDecoration.VERTICAL);
                                dividerItemDecoration.setDrawable(ContextCompat.getDrawable(DailyDevotionalsPerMonthActivity.this, R.drawable.recyclerview_divider));
                                listOfDailyDevotionalsRecyclerView.addItemDecoration(dividerItemDecoration);

                                Collections.sort(dailyDevotionalListPerMonth, Devotional.devotionalComparatorDate);
                                listOfDailyDevotionalsRecyclerView.setLayoutManager(new LinearLayoutManager(DailyDevotionalsPerMonthActivity.this));
                                dailyDevotionalsPerMonthAdapter = new DailyDevotionalsPerMonthAdapter(DailyDevotionalsPerMonthActivity.this, dailyDevotionalListPerMonth);
                                listOfDailyDevotionalsRecyclerView.setAdapter(dailyDevotionalsPerMonthAdapter);

                            }else{
                                loadingMonthDevotionalProgressBar.setVisibility(View.GONE);
                                errorMessageTextView.setVisibility(View.VISIBLE);
                                retryButton.setVisibility(View.VISIBLE);
                            }


                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                    Log.i(TAG, "onCreate(DailyDevDatabase): " + dailyDevotionalListPerMonth);
                } else {
                    listOfDailyDevotionalsRecyclerView.setVisibility(View.VISIBLE);
                    loadingMonthDevotionalProgressBar.setVisibility(View.GONE);

                    DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(DailyDevotionalsPerMonthActivity.this, DividerItemDecoration.VERTICAL);
                    dividerItemDecoration.setDrawable(ContextCompat.getDrawable(DailyDevotionalsPerMonthActivity.this, R.drawable.recyclerview_divider));
                    listOfDailyDevotionalsRecyclerView.addItemDecoration(dividerItemDecoration);

                    Collections.sort(dailyDevotionalListPerMonth, Devotional.devotionalComparatorDate);
                    listOfDailyDevotionalsRecyclerView.setLayoutManager(new LinearLayoutManager(DailyDevotionalsPerMonthActivity.this));
                    dailyDevotionalsPerMonthAdapter = new DailyDevotionalsPerMonthAdapter(DailyDevotionalsPerMonthActivity.this, dailyDevotionalListPerMonth);
                    listOfDailyDevotionalsRecyclerView.setAdapter(dailyDevotionalsPerMonthAdapter);
                }
            }
        });
    }
}
