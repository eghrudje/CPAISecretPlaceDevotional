package com.example.cpaisecretplacedevotional;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import service.ItemClickListener;
import ui.DevotionalNoteActivity;

public class ListOfDevotionalNotesAdapter  extends RecyclerView.Adapter<ListOfDevotionalNotesHolder> {
    private static final String TAG = "ListOfNotesAdapter";
    Context c;
    ArrayList<DevotionalNote> arrayListOfDevotionalNotes;

    public ListOfDevotionalNotesAdapter(Context c, ArrayList<DevotionalNote> arrayListOfDevotionalNotes) {
        this.c = c;
        this.arrayListOfDevotionalNotes = arrayListOfDevotionalNotes;
        Log.i(TAG, "List Of Notes: " + arrayListOfDevotionalNotes.toString());
    }

    public void DevotionalNoteSearchMethod (ArrayList<DevotionalNote> arrayListOfDevotionalNotes) {
        this.arrayListOfDevotionalNotes = arrayListOfDevotionalNotes;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ListOfDevotionalNotesHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rowlistofdevotionalnotes, null);
        return new ListOfDevotionalNotesHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListOfDevotionalNotesHolder holder, int position) {
        final String dateCreate = arrayListOfDevotionalNotes.get(position).getNoteCreatedAtDate();
        final String timeCreate = arrayListOfDevotionalNotes.get(position).getNoteCreatedAtTime();
        final String dateUpdate = arrayListOfDevotionalNotes.get(position).getNoteUpdatedAtDate();
        final String timeUpdate = arrayListOfDevotionalNotes.get(position).getNoteUpdatedAtTime();
        final String devotionalDate = arrayListOfDevotionalNotes.get(position).getDevDate();
        final String devNoteTitle = arrayListOfDevotionalNotes.get(position).getDevotionalNoteTitle();
        final String devTitle = arrayListOfDevotionalNotes.get(position).getDevTitle();


        holder.devotionalTitleTextView.setText(devNoteTitle);
        holder.devotionalCreatedTextView.setText("Created at: " + dateCreate + " " + timeCreate);
        holder.devotionalUpdatedTextView.setText("Updated at: " + dateUpdate + " " + timeUpdate);

        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onItemClickListener(View v, int position) {
                Log.i(TAG, "onItemClickListener(DevNote): ");

                Intent devotionalNoteIntent = new Intent(c, DevotionalNoteActivity.class);
                devotionalNoteIntent.putExtra("Date", devotionalDate);
                devotionalNoteIntent.putExtra("Title", devTitle);

                c.startActivity(devotionalNoteIntent);
            }
        });
    }
    @Override
    public int getItemCount() {
        return arrayListOfDevotionalNotes.size();
    }
}
