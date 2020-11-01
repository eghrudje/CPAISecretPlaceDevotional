package ui;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.cpaisecretplacedevotional.ListOfDevotionalNotesAdapter;
import com.example.cpaisecretplacedevotional.R;

import java.util.ArrayList;
import java.util.Collections;

import io.realm.Realm;
import io.realm.RealmResults;
import service.DevotionalNote;
import service.DevotionalRealmHelper;

public class ListOfDevotionalNotesActivity extends AppCompatActivity {
    private static final String TAG = "ListOfDevNotesActivity";
    Realm realm;
    Context context;
    DevotionalRealmHelper devotionalRealmHelper;
    RecyclerView listOfDevotionalNotesRecyclerView;
    ListOfDevotionalNotesAdapter listOfDevotionalNotesAdapter;
    EditText selectNoteFromList;
    TextView infoTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_devotional_notes);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Devotional Notes");

        realm = Realm.getDefaultInstance();
        devotionalRealmHelper = DevotionalRealmHelper.getInstance(context);

        listOfDevotionalNotesRecyclerView = findViewById(R.id.recyclerViewOfDevotionalNotesId);
        selectNoteFromList = findViewById(R.id.searchDevotionalNoteId);
        infoTextView = findViewById(R.id.noteInfoID);

        selectNoteFromList.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    searchDevotionalNote(s.toString());
                } catch (Exception e) {}
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        ArrayList<DevotionalNote> devotionalNoteArrayList = new ArrayList<>();
        Log.i(TAG, "onCreate: ...............");
        RealmResults<DevotionalNote> devotionalNoteRealmResults  = realm.where(DevotionalNote.class).findAll();

        if (devotionalNoteRealmResults.isEmpty()) {
            Log.i(TAG, "onCreate: .............." );
            infoTextView.setVisibility(View.VISIBLE);
            listOfDevotionalNotesRecyclerView.setVisibility(View.GONE);

        } else {
            Log.i(TAG, "onCreate: " + devotionalNoteRealmResults);
            for (DevotionalNote devNote: devotionalNoteRealmResults) {
                devotionalNoteArrayList.add(devNote);
            }
            Collections.sort(devotionalNoteArrayList, DevotionalNote.devotionalNoteComparatorDate);

            listOfDevotionalNotesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            listOfDevotionalNotesAdapter = new ListOfDevotionalNotesAdapter(this, devotionalNoteArrayList);
            listOfDevotionalNotesRecyclerView.setAdapter(listOfDevotionalNotesAdapter);
        }

    }
    private void searchDevotionalNote (String noteTitle) {
        ArrayList<DevotionalNote> notes = new ArrayList<>();
        for(DevotionalNote note: realm.where(DevotionalNote.class).findAll()){
            Log.i(TAG, note.getDevotionalNoteTitle());
            Log.i(TAG, noteTitle);
            if(note.getDevotionalNoteTitle().contains(noteTitle)){
                Log.i(TAG, "If statement working.....");
                notes.add(note);
            }
        }
        Log.i(TAG, String.valueOf(notes));
        listOfDevotionalNotesAdapter.DevotionalNoteSearchMethod(notes);
    }
}
