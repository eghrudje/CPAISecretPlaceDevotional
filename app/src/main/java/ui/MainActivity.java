package ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.cpaisecretplacedevotional.DevotionalDebug;
import com.example.cpaisecretplacedevotional.ListOfDevotionalNotesActivity;
import com.example.cpaisecretplacedevotional.R;
import com.example.cpaisecretplacedevotional.YearDevotional;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;
import service.Devotional;
import service.DevotionalInterface;
import service.DevotionalRealmHelper;
import service.MonthDevotional;

public class MainActivity extends AppCompatActivity implements DevotionalInterface {

    RecyclerView devRecyclerView;
    DevotionalAdapter myAdapter;
    ProgressBar progressBar;
    Realm realm;
    DevotionalRealmHelper devotionalRealmHelper;
    RealmChangeListener realmChangeListener;
    RealmResults<MonthDevotional> realmDevotional;
    public static final String TAG = "MainActivity";
    DatabaseReference reference;
    ArrayList<Devotional> devotionalsFromServer;
    ArrayList<DevotionalDebug> devotionalDebugArrayList;
    ArrayList<Object> homePageItems;
    String todaysDatePassed;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        devRecyclerView = findViewById(R.id.recyclerView);
        progressBar = findViewById(R.id.progressbarId);
        devRecyclerView.setLayoutManager(new LinearLayoutManager(this));


        devotionalRealmHelper = DevotionalRealmHelper.getInstance(this);
        devotionalRealmHelper.attachDataChangeListener(this);
        devotionalsFromServer = new ArrayList<>();
        devotionalDebugArrayList = new ArrayList<>();

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        todaysDatePassed = dateFormat.format(cal.getTime());

        DateFormat dateFormat1 = new SimpleDateFormat("MMMMyyyy");
        Date month = new Date();
        String currentMonthYearName = dateFormat1.format(month);
        Log.i(TAG, "onCreate: " + currentMonthYearName);


        final Devotional devotional = devotionalRealmHelper.fetchDayDevotionalFromDatabase(todaysDatePassed);
        Log.i(TAG, "onCreate: " + devotional);

        if (devotional != null) {
            progressBar.setVisibility(View.GONE);
            try {
                homePageItems = devotionalRealmHelper.fetchHomePageItems(devotional);
                myAdapter = new DevotionalAdapter(this, homePageItems);
                devRecyclerView.setAdapter(myAdapter);

            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else {
            progressBar.setVisibility(View.VISIBLE);
            reference = FirebaseDatabase.getInstance().getReference().child("devotionals");

            DatabaseReference ref = reference.child(currentMonthYearName);

            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()) {
                        Devotional devotionalFromServer = dataSnapshot1.getValue(Devotional.class);
                        Log.i(TAG, "onDataChange(dataSnapshot1): " + dataSnapshot1);
                        Log.i(TAG, "onDataChange(devotionalFromServer): " + devotionalFromServer);
                        devotionalsFromServer.add(devotionalFromServer);
                        devotionalRealmHelper.savedDevotional(devotionalFromServer);

                        Log.i(TAG, "onDataChange(devotionalsFromServer): " + devotionalsFromServer);
                        Log.i(TAG, "onDataChange(devotionalRealmHelper): " + devotionalRealmHelper);
                    }

                    for (Devotional dev: devotionalsFromServer) {
                        String devotionalDate = dev.getDate();
                        Log.i(TAG, "onDataChange(dev): " + dev);
                        if (devotionalDate.contentEquals(todaysDatePassed)) {
                            try {
                                homePageItems = devotionalRealmHelper.fetchHomePageItems(dev);
                                myAdapter = new DevotionalAdapter(MainActivity.this, homePageItems);
                                devRecyclerView.setAdapter(myAdapter);
                                progressBar.setVisibility(View.GONE);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            Log.i(TAG, "onDataChange(homePageItems): " + homePageItems);
                            break;
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.settings) {
            Intent intent = new Intent(MainActivity.this, DevotionalSettingsActivity.class);
            startActivity(intent);
        }
        if (id == R.id.viewNotesID) {
            Intent intent1 = new Intent(MainActivity.this, ListOfDevotionalNotesActivity.class);
            startActivity(intent1);
        }
        return true;
    }

    @Override
    public void onDevotionalListener() {

    }
}