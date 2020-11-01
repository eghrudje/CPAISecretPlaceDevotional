package ui;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.cpaisecretplacedevotional.MonthsDevotionalInAYearAdapter;
import com.example.cpaisecretplacedevotional.R;

import java.util.ArrayList;

import service.MonthDevotional;

public class MonthDevotionalsPerYearActivity extends AppCompatActivity {

    RecyclerView listOfCardViewMonthsRecyclerView;
    MonthsDevotionalInAYearAdapter monthsDevotionalInAYearAdapter;
    Context context;
    String year;
    public static final String TAG = "MonthDevotionalsPerYear";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_month_devotionals_per_year);

        ActionBar actionBar = getSupportActionBar();
        Log.i(TAG, "onCreate: Months per year...");

        Intent intent  = getIntent();
        year = intent.getStringExtra("yearName");

        listOfCardViewMonthsRecyclerView = findViewById(R.id.recyclerViewMonthlyDevotionalsPerYear);
        listOfCardViewMonthsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        monthsDevotionalInAYearAdapter = new MonthsDevotionalInAYearAdapter(this, getMyListOfMonthsInAYear());
        listOfCardViewMonthsRecyclerView.setAdapter(monthsDevotionalInAYearAdapter);
    }

    private ArrayList<MonthDevotional> getMyListOfMonthsInAYear() {

        ArrayList<MonthDevotional> arrayListOfMonths = new ArrayList<>();

        MonthDevotional monthDevotional1 = new MonthDevotional();
        monthDevotional1.setMonthName("January");
        monthDevotional1.setMonthImage("january" + year);
        monthDevotional1.setMonthDevotional_id("January" + year);
        Log.i(TAG, "getMyListOfMonthsInAYear(January): " + monthDevotional1);
        arrayListOfMonths.add(monthDevotional1);

        MonthDevotional monthDevotional2 = new MonthDevotional();
        monthDevotional2.setMonthName("February");
        monthDevotional2.setMonthImage("february" + year);
        monthDevotional2.setMonthDevotional_id("February" + year);
        Log.i(TAG, "getMyListOfMonthsInAYear(February): " + monthDevotional2);
        arrayListOfMonths.add(monthDevotional2);

        MonthDevotional monthDevotional3 = new MonthDevotional();
        monthDevotional3.setMonthName("March");
        monthDevotional3.setMonthImage("march" + year);
        monthDevotional3.setMonthDevotional_id("March" + year);
        Log.i(TAG, "getMyListOfMonthsInAYear(March): " + monthDevotional1);
        arrayListOfMonths.add(monthDevotional3);

        MonthDevotional monthDevotional4 = new MonthDevotional();
        monthDevotional4.setMonthName("April");
        monthDevotional4.setMonthImage("april" + year);
        monthDevotional4.setMonthDevotional_id("April" + year);
        arrayListOfMonths.add(monthDevotional4);

        MonthDevotional monthDevotional5 = new MonthDevotional();
        monthDevotional5.setMonthName("May");
        monthDevotional5.setMonthImage("may" + year);
        monthDevotional5.setMonthDevotional_id("May" + year);
        arrayListOfMonths.add(monthDevotional5);

        MonthDevotional monthDevotional6 = new MonthDevotional();
        monthDevotional6.setMonthName("June");
        monthDevotional6.setMonthImage("june" + year);
        monthDevotional6.setMonthDevotional_id("June" + year);
        arrayListOfMonths.add(monthDevotional6);

        MonthDevotional monthDevotional7 = new MonthDevotional();
        monthDevotional7.setMonthName("July");
        monthDevotional7.setMonthImage("july" + year);
        monthDevotional6.setMonthDevotional_id("July" + year);
        arrayListOfMonths.add(monthDevotional7);

        MonthDevotional monthDevotional8 = new MonthDevotional();
        monthDevotional8.setMonthName("August");
        monthDevotional8.setMonthImage("august" + year);
        monthDevotional8.setMonthDevotional_id("August" + year);
        arrayListOfMonths.add(monthDevotional8);

        MonthDevotional monthDevotional9 = new MonthDevotional();
        monthDevotional9.setMonthName("September");
        monthDevotional9.setMonthImage("september" + year);
        monthDevotional9.setMonthDevotional_id("September" + year);
        arrayListOfMonths.add(monthDevotional9);

        MonthDevotional monthDevotional10 = new MonthDevotional();
        monthDevotional10.setMonthName("October");
        monthDevotional10.setMonthImage("october" + year);
        monthDevotional10.setMonthDevotional_id("October" + year);
        arrayListOfMonths.add(monthDevotional10);

        MonthDevotional monthDevotional11 = new MonthDevotional();
        monthDevotional11.setMonthName("November");
        monthDevotional11.setMonthImage("november" + year);
        monthDevotional11.setMonthDevotional_id("November" + year);
        arrayListOfMonths.add(monthDevotional11);

        MonthDevotional monthDevotional12 = new MonthDevotional();
        monthDevotional12.setMonthName("December");
        monthDevotional12.setMonthImage("december" + year);
        monthDevotional12.setMonthDevotional_id("December" + year);
        arrayListOfMonths.add(monthDevotional12);

        return arrayListOfMonths;
    }
}
