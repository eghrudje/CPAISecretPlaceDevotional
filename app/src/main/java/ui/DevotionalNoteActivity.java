package ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import service.DevotionalNote;

import com.example.cpaisecretplacedevotional.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import io.realm.Realm;
import service.DevotionalRealmHelper;

import static android.widget.Toast.LENGTH_LONG;

public class DevotionalNoteActivity extends AppCompatActivity {

    private static final String TAG = "DevotionalNoteActivity";
    private final int REQ_CODE = 10;
    EditText devotionalJottingEditText, devotionalJottingTitleEditText;
    TextView devotionalHeadingTextView;
    Button saveDevotionalNoteButton, viewDevotionalNotesButton;
    String title, date, jottingTitle, jottingContent;
    int devotionalID;
    String createdDate, createdTime, updatedDate, updatedTime;
    Calendar cal = Calendar.getInstance();
    DevotionalRealmHelper devotionalRealmHelper;
    Context context;
    Realm realm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_devotional_note);

        realm = Realm.getDefaultInstance();
        getSupportActionBar().setTitle(getResources().getString(R.string.note));
        devotionalRealmHelper = DevotionalRealmHelper.getInstance(context);

        devotionalJottingEditText = findViewById(R.id.noteEditText);
        devotionalJottingTitleEditText = findViewById(R.id.noteEditTextTitle);
        devotionalHeadingTextView = findViewById(R.id.noteHeading);
        saveDevotionalNoteButton = findViewById(R.id.saveNote);
        viewDevotionalNotesButton = findViewById(R.id.prevEntries);

        Intent intent = getIntent();

        title = intent.getStringExtra("Title");
        date = intent.getStringExtra("Date");
        Log.i(TAG, "onCreate: " + date);
        devotionalID = intent.getIntExtra("Id", 1);

        loadDevotionalActivityItems();

        devotionalHeadingTextView.setText(title + " | " + date);

        saveDevotionalNoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DevotionalNote devNote = devotionalRealmHelper.fetchDevotionalNoteFromDatabase(date);
                if (devNote == null) {
                    DevotionalNote devotionalNote = new DevotionalNote();
                    devotionalNote.setDevDate(date);

                    jottingTitle = devotionalJottingTitleEditText.getText().toString();
                    devotionalNote.setDevotionalNoteTitle(jottingTitle);

                    jottingContent = devotionalJottingEditText.getText().toString();
                    devotionalNote.setDevotionalNoteContent(jottingContent);

                    SimpleDateFormat dateFormat1 = new SimpleDateFormat("dd.MM.yyyy");
                    createdDate = dateFormat1.format(cal.getTime());
                    devotionalNote.setNoteCreatedAtDate(createdDate);
                    devotionalNote.setNoteUpdatedAtDate("");

                    SimpleDateFormat dateFormat2 = new SimpleDateFormat("HH:mm");
                    createdTime = dateFormat2.format(cal.getTime());
                    devotionalNote.setNoteCreatedAtTime(createdTime);
                    devotionalNote.setNoteUpdatedAtTime("");

                    updatedDate = createdDate;
                    devotionalNote.setNoteUpdatedAtDate(updatedDate);
                    updatedTime = createdTime;
                    devotionalNote.setNoteUpdatedAtTime(updatedTime);

                    Log.i(TAG, "onClick(Create): " + devotionalNote.toString());
                    devotionalRealmHelper.saveDevotionalNote(devotionalNote);
                    Toast.makeText(DevotionalNoteActivity.this, "New Note Saved", LENGTH_LONG).show();


                } else {
                    realm.beginTransaction();
                    devNote.setDevotionalNoteTitle(devotionalJottingTitleEditText.getText().toString());
                    devNote.setDevotionalNoteContent(devotionalJottingEditText.getText().toString());

                    SimpleDateFormat dateFormat1 = new SimpleDateFormat("dd.MM.yyyy");
                    updatedDate = dateFormat1.format(cal.getTime());
                    devNote.setNoteUpdatedAtDate(updatedDate);

                    SimpleDateFormat dateFormat2 = new SimpleDateFormat("HH:mm");
                    updatedTime = dateFormat2.format(cal.getTime());
                    devNote.setNoteUpdatedAtTime(updatedTime);

                    //I am to update database now
                    //OR
                    realm.commitTransaction();
                   // devotionalRealmHelper.updateDevotionalNote(devNote);
                    Toast.makeText(DevotionalNoteActivity.this, "Note Updated", LENGTH_LONG).show();
                    Log.i(TAG, "onClick(Update): " + devNote);


                }

            }
        });

        viewDevotionalNotesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent listOfDevotionalNotesIntent = new Intent(DevotionalNoteActivity.this, ListOfDevotionalNotesActivity.class);
//                listOfDevotionalNotesIntent.putExtra("title", jottingTitle);
//                listOfDevotionalNotesIntent.putExtra("dateCreate", createdDate);
//                listOfDevotionalNotesIntent.putExtra("timeCreate", createdTime);
//                listOfDevotionalNotesIntent.putExtra("dateUpdate", updatedDate);
//                listOfDevotionalNotesIntent.putExtra("timeUpdate", updatedTime);
                startActivity(listOfDevotionalNotesIntent);
            }
        });


    }
    public void loadDevotionalActivityItems () {
        Log.i(TAG, "loadDevotionalActivityItems.....Date: " + date);
        Log.i(TAG, "loadDevotionalActivityItems: " + devotionalRealmHelper);

        DevotionalNote devNote = devotionalRealmHelper.fetchDevotionalNoteFromDatabase(date);
        Log.i(TAG, "loadDevotionalActivityItems: " + devNote);

        if (devNote != null) {
            devotionalJottingTitleEditText.setText(devNote.getDevotionalNoteTitle());
            devotionalJottingEditText.setText(devNote.getDevotionalNoteContent());
        } else {
           devotionalJottingTitleEditText.setText("");
           devotionalJottingEditText.setText("");
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_devotional_note, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.convertSpeechToText){
            Intent speechToTextIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            speechToTextIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            speechToTextIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
            speechToTextIntent.putExtra(RecognizerIntent.EXTRA_PROMPT, getString(R.string.take_note));

            try {
                startActivityForResult(speechToTextIntent, REQ_CODE);
            } catch (ActivityNotFoundException a){
                Toast.makeText(getApplicationContext(), getString(R.string.speech_error), Toast.LENGTH_SHORT).show();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case REQ_CODE: {
                if (resultCode == RESULT_OK && data != null){
                    ArrayList<String> speechResult = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    String initialEditTextContent = devotionalJottingEditText.getText().toString();
                    String textFromSpeech = speechResult.get(0);
                    devotionalJottingEditText.setText(initialEditTextContent + " " + textFromSpeech);
                }
                break;
            }
        }
    }
}
