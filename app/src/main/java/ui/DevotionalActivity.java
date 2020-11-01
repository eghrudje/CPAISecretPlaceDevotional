package ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.cpaisecretplacedevotional.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import receiver.DevotionalBroadcastReceiver;
import service.Devotional;
import service.DevotionalRealmHelper;

public class DevotionalActivity extends AppCompatActivity {

    String date, author, title, memoryVerse, memoryVersePassage, fullText, fullPassage, bibleInAYear, image;
    int devotionalID;
    TextView devotionalDate, devotionalTitle, devotionalAuthor, dMemoryVerse, devotionalMemoryVersePassage, devotionalFullText, devotionalFullPassage, dBibleInAYear,
            devotionalErrorMessage, tagRead, tagBibleInYear;
    ImageView devotionalImage;
    Button devotionalButtonRetry;
    ImageButton buttonMoveRight, buttonMoveLeft;

    DevotionalRealmHelper devotionalRealmHelper;
    DevotionalBroadcastReceiver devotionalBroadcastReceiver;
    private long mLastClickTime;


    public static final String TAG = "DevotionalActivity";
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_devotional);

        devotionalRealmHelper = DevotionalRealmHelper.getInstance(context);
        devotionalBroadcastReceiver = new DevotionalBroadcastReceiver();
        devotionalBroadcastReceiver.cancelAlarmAndNotification();
        Log.i(TAG, "onCreate: Stop alarm.....");

        devotionalDate = findViewById(R.id.dateDev);
        devotionalAuthor = findViewById(R.id.authorDev);
        devotionalTitle = findViewById(R.id.title);
        dMemoryVerse = findViewById(R.id.memoryVerseDev);
        devotionalMemoryVersePassage = findViewById(R.id.memoryVersePassageDev);
        devotionalFullPassage = findViewById(R.id.fullPassageDev);
        devotionalFullText = findViewById(R.id.fullTextDev);
        dBibleInAYear = findViewById(R.id.bibleInAYearDev);
        devotionalImage = findViewById(R.id.devImage);
        devotionalErrorMessage = findViewById(R.id.errorMessage);
        devotionalButtonRetry = findViewById(R.id.buttonRetry);
        //buttonMoveLeft = findViewById(R.id.iconLeft);
        //buttonMoveRight = findViewById(R.id.iconRight);
        tagBibleInYear = findViewById(R.id.read);
        tagRead = findViewById(R.id.inAYear);

        context = this;

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat dateFormat1 = new SimpleDateFormat("dd.MM.yyyy");
        final String todaysDate = dateFormat1.format(cal.getTime());

        devotionalButtonRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Devotional devotionalToday = devotionalRealmHelper.fetchDayDevotionalFromDatabase(todaysDate);

                if (devotionalToday != null) {
                    Log.i(TAG, "DevotionalActivity: Inner If Block Is True");

                    updateViewFromFetchedData(devotionalToday);
                    trackOpenedDevotional();
                } else {
                    Log.i(TAG, "DevotionalActivity: Inner If Block Is False");

                    devotionalErrorMessage.setVisibility(View.VISIBLE);
                    devotionalButtonRetry.setVisibility(View.VISIBLE);
                    devotionalImage.setVisibility(View.INVISIBLE);
                    devotionalDate.setVisibility(View.INVISIBLE);
                    devotionalAuthor.setVisibility(View.INVISIBLE);
                    devotionalFullPassage.setVisibility(View.INVISIBLE);
                    devotionalMemoryVersePassage.setVisibility(View.INVISIBLE);
                    devotionalFullText.setVisibility(View.INVISIBLE);
                    devotionalTitle.setVisibility(View.INVISIBLE);
                    dMemoryVerse.setVisibility(View.INVISIBLE);
                    dBibleInAYear.setVisibility(View.INVISIBLE);
                    buttonMoveRight.setVisibility(View.INVISIBLE);
                    buttonMoveLeft.setVisibility(View.INVISIBLE);
                    tagRead.setVisibility(View.INVISIBLE);
                    tagBibleInYear.setVisibility(View.INVISIBLE);
                }
            }
        });

        Intent intent = getIntent();

        if (intent != null && intent.getStringExtra("Title") != null && intent.getStringExtra("Date") != null) {
            Log.i(TAG, "DevotionalActivity: Bigger If Block Is True");

            title = intent.getStringExtra("Title");
            date = intent.getStringExtra("Date");
            author = intent.getStringExtra("Author");
            memoryVerse = intent.getStringExtra("MemoryVerse");
            memoryVersePassage = intent.getStringExtra("MemoryVersePassage");
            fullText = intent.getStringExtra("FullText");
            fullPassage = intent.getStringExtra("FullPassage");
            bibleInAYear = intent.getStringExtra("BibleInAYear");
            image = getIntent().getStringExtra("Image");
            devotionalID = getIntent().getIntExtra("Id", 1);

            updateView();
            trackOpenedDevotional();

        } else{
            Log.i(TAG, "DevotionalActivity: Bigger If Block Is False");
            Log.i(TAG, "onCreate: " + todaysDate);
            Devotional devotionalToday = devotionalRealmHelper.fetchDayDevotionalFromDatabase(todaysDate);

            if (devotionalToday != null) {
                Log.i(TAG, "DevotionalActivity: Inner If Block Is True");

                updateViewFromFetchedData(devotionalToday);
                trackOpenedDevotional();
            } else {
                Log.i(TAG, "DevotionalActivity: Inner If Block Is False");

                devotionalErrorMessage.setVisibility(View.VISIBLE);
                devotionalButtonRetry.setVisibility(View.VISIBLE);
                devotionalImage.setVisibility(View.INVISIBLE);
                devotionalDate.setVisibility(View.INVISIBLE);
                devotionalAuthor.setVisibility(View.INVISIBLE);
                devotionalFullPassage.setVisibility(View.INVISIBLE);
                devotionalMemoryVersePassage.setVisibility(View.INVISIBLE);
                devotionalFullText.setVisibility(View.INVISIBLE);
                devotionalTitle.setVisibility(View.INVISIBLE);
                dMemoryVerse.setVisibility(View.INVISIBLE);
                dBibleInAYear.setVisibility(View.INVISIBLE);
                buttonMoveRight.setVisibility(View.INVISIBLE);
                buttonMoveLeft.setVisibility(View.INVISIBLE);
                tagRead.setVisibility(View.INVISIBLE);
                tagBibleInYear.setVisibility(View.INVISIBLE);
            }
        }
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(title);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_devotional_activity, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000 ) {
            return true;
        }
        mLastClickTime = SystemClock.elapsedRealtime();

        if(id == R.id.takeNote){
            Intent devotionalNoteActivityIntent = new Intent(DevotionalActivity.this, DevotionalNoteActivity.class);
            devotionalNoteActivityIntent.putExtra("Title", title);
            devotionalNoteActivityIntent.putExtra("Date", date);
            devotionalNoteActivityIntent.putExtra("Id", devotionalID);
            startActivity(devotionalNoteActivityIntent);
        }
        return super.onOptionsItemSelected(item);
    }

    private void trackOpenedDevotional() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        String currentDate = dateFormat.format(cal.getTime());

        SharedPreferences sp = getSharedPreferences("Devotional_Tracker", MODE_PRIVATE);
        Log.i(TAG, "date" + date);
        Log.i(TAG, "currentDate" + currentDate);
        Log.i(TAG, "sp.get..." + sp.getString("last_opened", ""));

        if (currentDate.contentEquals(date) &&  !currentDate.contentEquals(sp.getString("last_opened", ""))) {
            SharedPreferences.Editor editor = sp.edit();
            editor.putString("last_opened", date);
            Log.i(TAG, "DevotionalActivity: Tracking date2");
            editor.apply();
        }
    }
    private void updateView(){
        Glide.with(this).load(image).into(devotionalImage);
        devotionalDate.setText(date);
        devotionalTitle.setText(title);
        dMemoryVerse.setText(memoryVerse);
        devotionalMemoryVersePassage.setText(memoryVersePassage);
        devotionalFullText.setText(fullText);
        devotionalFullPassage.setText(fullPassage);
        dBibleInAYear.setText(bibleInAYear);
        devotionalAuthor.setText(author);
    }
    private void updateViewFromFetchedData(Devotional devotionalToday){
        title = devotionalToday.getTitle();
        author = devotionalToday.getAuthor();
        memoryVerse = devotionalToday.getMemoryVerse();
        date = devotionalToday.getDate();
        memoryVersePassage = devotionalToday.getMemoryVersePassage();
        fullPassage = devotionalToday.getFullPassage();
        fullText = devotionalToday.getFullText();
        bibleInAYear = devotionalToday.getBibleInAYear();
        image = devotionalToday.getImage();

        updateView();
    }

}
