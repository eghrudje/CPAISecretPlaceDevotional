package ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.cpaisecretplacedevotional.BuildConfig;
import com.example.cpaisecretplacedevotional.DevotionalDebug;
import com.example.cpaisecretplacedevotional.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import service.Devotional;
import service.DevotionalInterface;
import service.DevotionalRealmHelper;

public class MainActivity extends AppCompatActivity implements DevotionalInterface {

    RecyclerView devRecyclerView;
    DevotionalAdapter myAdapter;
    ProgressBar progressBar;
    Realm realm;
    DevotionalRealmHelper devotionalRealmHelper;
    RealmChangeListener realmChangeListener;
    public static final String TAG = "MainActivity";
    DatabaseReference reference;
    ArrayList<Devotional> devotionalsFromServer;
    ArrayList<DevotionalDebug> devotionalDebugArrayList;
    ArrayList<Object> homePageItems;
    String todaysDatePassed;
    TextView errorMessageTextView;
    Button retryButton, displayDbDevs, searchDbDev;
    private long mLastClickTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        realm = Realm.getDefaultInstance();

        devRecyclerView = findViewById(R.id.recyclerView);
        progressBar = findViewById(R.id.progressbarId);
        errorMessageTextView = findViewById(R.id.errorMessage);
        retryButton = findViewById(R.id.buttonRetry);
//        displayDbDevs = findViewById(R.id.printDatabaseDevotionalsID);
//        searchDbDev = findViewById(R.id.searchDatabaseForDevotionalID);

        //    devRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        devRecyclerView.setLayoutManager(llm);


        devotionalRealmHelper = DevotionalRealmHelper.getInstance(this);
        devotionalRealmHelper.attachDataChangeListener(this);
        devotionalsFromServer = new ArrayList<>();
        devotionalDebugArrayList = new ArrayList<>();

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        todaysDatePassed = dateFormat.format(cal.getTime());

        DateFormat dateFormat1 = new SimpleDateFormat("MMMMyyyy");
        Date month = new Date();
        final String currentMonthYearName = dateFormat1.format(month);
        Log.i(TAG, "onCreate: " + currentMonthYearName);


        final Devotional devotional = devotionalRealmHelper.fetchDayDevotionalFromDatabase(todaysDatePassed);
        Log.i(TAG, "onCreate: " + devotional);

        if (devotional != null) {
            progressBar.setVisibility(View.GONE);
            devRecyclerView.setVisibility(View.VISIBLE);

            try {
                homePageItems = devotionalRealmHelper.fetchHomePageItems(devotional);
                myAdapter = new DevotionalAdapter(this, homePageItems);
                devRecyclerView.setAdapter(myAdapter);

            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else {
            devRecyclerView.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
            errorMessageTextView.setVisibility(View.VISIBLE);
            SharedPreferences sp = getSharedPreferences("App_Launch_Tracker", MODE_PRIVATE);

            if (sp.getString("loaderState", "") != "not_first") {
                errorMessageTextView.setText("Setting up devotional over the internet. Enable Connection.");
                SharedPreferences.Editor editor = sp.edit();
                editor.putString("loaderState", "not_first");
                editor.commit();

            } else {
                errorMessageTextView.setText("Unable to get Today's Devotional. Check Internet Connection....");
            }
            //retryButton.setVisibility(View.VISIBLE);
            reference = FirebaseDatabase.getInstance().getReference().child("devotionals");

            fetchDevotionalsFromServer();

        }
//        searchDbDev.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                findDevotionalFromDatabase("24.10.2020");
//                fetchDevotionalsFromServer();
//            }
//        });
//        displayDbDevs.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                RealmResults<Devotional> dev = realm.where(Devotional.class).findAll();
//                Log.i(TAG, "onClick: " + dev);
//
//            }
//        });
        retryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressBar.setVisibility(View.VISIBLE);
                errorMessageTextView.setVisibility(View.GONE);
                retryButton.setVisibility(View.GONE);
//devRecyclerView.setVisibility(View.GONE);
                reference = FirebaseDatabase.getInstance().getReference().child("devotionals");

                fetchDevotionalsFromServer();
            }
        });
    }

    private void findDevotionalFromDatabase(String devDate) {
        devotionalRealmHelper.fetchDayDevotionalFromDatabase(devDate);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000 ) {
            return true;
        }
        mLastClickTime = SystemClock.elapsedRealtime();

        if (id == R.id.settings) {
            Intent intent = new Intent(MainActivity.this, DevotionalSettingsActivity.class);
            startActivity(intent);
        }
        if (id == R.id.viewNotesID) {
            Intent intent1 = new Intent(MainActivity.this, ListOfDevotionalNotesActivity.class);
            startActivity(intent1);
        }
        if (id == R.id.aboutID) {

        }
        if (id == R.id.share) {
            try {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Secret Place");
                String shareMessage = "\nLet me recommend you this application\n\n";
                shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID + "\n\n";
                shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                startActivity(Intent.createChooser(shareIntent, "Select.."));
            } catch (Exception e) {

            }
        }
        if (id == R.id.exit) {
            finish();
        }
        return true;
    }

    public void fetchDevotionalsFromServer() {
        DateFormat dateFormat1 = new SimpleDateFormat("MMMMyyyy");
        Date month = new Date();
        final String currentMonthYearName = dateFormat1.format(month);
        Log.i(TAG, "onCreate: " + currentMonthYearName);

        DatabaseReference ref = reference.child(currentMonthYearName);

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    Devotional devotionalFromServer = dataSnapshot1.getValue(Devotional.class);
                    Log.i(TAG, "onDataChange(dataSnapshot1): " + dataSnapshot1);
                    Log.i(TAG, "onDataChange(devotionalFromServer): " + devotionalFromServer);

                    devotionalsFromServer.add(devotionalFromServer);
                    Log.i(TAG, "onDataChange: " + devotionalFromServer.getDate());
                    Log.i(TAG, "onDataChange: " + realm);
                    Devotional devotional = realm.where(Devotional.class).equalTo("date", devotionalFromServer.getDate())
                            .findFirst();
                    Log.i(TAG, "onDataChange(DDDDD): " + devotional);
                    if (devotional == null) {
                        devotionalRealmHelper.savedDevotional(devotionalFromServer);
                    }

                    Log.i(TAG, "onDataChange(devotionalsFromServer): " + devotionalsFromServer);
                    Log.i(TAG, "onDataChange(devotionalRealmHelper): " + devotionalRealmHelper);

                }
                for (Devotional dev : devotionalsFromServer) {
                    String devotionalDate = dev.getDate();
                    Log.i(TAG, "onDataChange(dev): " + dev);
                    if (devotionalDate.contentEquals(todaysDatePassed)) {
                        try {
                            homePageItems = devotionalRealmHelper.fetchHomePageItems(dev);
                            myAdapter = new DevotionalAdapter(MainActivity.this, homePageItems);
                            devRecyclerView.setAdapter(myAdapter);
                            progressBar.setVisibility(View.GONE);
                            errorMessageTextView.setVisibility(View.GONE);
                            retryButton.setVisibility(View.GONE);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        Log.i(TAG, "onDataChange(homePageItems): " + homePageItems);
                        break;
                    } else {
                        errorMessageTextView.setVisibility(View.VISIBLE);
                        errorMessageTextView.setText("Today's devotional not available");
                        retryButton.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);
                        devRecyclerView.setVisibility(View.GONE);

                    }
                }
                devRecyclerView.setVisibility(View.VISIBLE);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onDevotionalListener() {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}