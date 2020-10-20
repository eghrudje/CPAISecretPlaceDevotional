package com.example.cpaisecretplacedevotional;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import service.Devotional;
import service.ItemClickListener;
import ui.DevotionalActivity;

public class DailyDevotionalsPerMonthAdapter extends RecyclerView.Adapter<DailyDevotionalsPerMonthHolder> {
    private static final String TAG = "DaysPerMonthAdapter";
    Context c;
    ArrayList<Devotional> arrayListOfDays;

    public DailyDevotionalsPerMonthAdapter(Context c, ArrayList<Devotional> arrayListOfDays) {
        this.c = c;
        this.arrayListOfDays = arrayListOfDays;
        Log.e(TAG, "DailyDevotionalsPerMonthAdapter: " + arrayListOfDays.toString());
    }
//Open the devotionals page for september>? Again ok. Ok.
    @NonNull
    @Override
    public DailyDevotionalsPerMonthHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rowdaylistpermonth, null);
        return new DailyDevotionalsPerMonthHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final DailyDevotionalsPerMonthHolder holder, int position) {
        Log.i(TAG, "onBindViewHolder: Where are you????");

        Log.i(TAG, "onBindViewHolder(DailyDate): " + holder.devotionalDate);
        Log.i(TAG, "onBindViewHolder(DailyTitle): " + holder.devotionalTitle);

        holder.devotionalDate.setText(arrayListOfDays.get(position).getDate());
        holder.devotionalTitle.setText(arrayListOfDays.get(position).getTitle());

        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onItemClickListener(View v, int position) {
                String Title = arrayListOfDays.get(position).getTitle();
                String Date = arrayListOfDays.get(position).getDate();
                String Image = arrayListOfDays.get(position).getImage();
                String Text = arrayListOfDays.get(position).getText();
                String Author = arrayListOfDays.get(position).getAuthor();
                String MemoryVerse = arrayListOfDays.get(position).getMemoryVerse();
                String MemoryVersePassage = arrayListOfDays.get(position).getMemoryVersePassage();
                String BibleInAYear = arrayListOfDays.get(position).getBibleInAYear();
                String FullPassage = arrayListOfDays.get(position).getFullPassage();
                String FullText = arrayListOfDays.get(position).getFullText();
                int DevotionalId = arrayListOfDays.get(position).getDevotional_id();

                Intent devotionalActivityIntent = new Intent(c, DevotionalActivity.class);
                devotionalActivityIntent.putExtra("Id", DevotionalId);
                devotionalActivityIntent.putExtra("Title", Title);
                devotionalActivityIntent.putExtra("MemoryVerse", MemoryVerse);
                devotionalActivityIntent.putExtra("Author", Author);
                devotionalActivityIntent.putExtra("Text", Text);
                devotionalActivityIntent.putExtra("MemoryVersePassage", MemoryVersePassage);
                devotionalActivityIntent.putExtra("FullPassage", FullPassage);
                devotionalActivityIntent.putExtra("FullText", FullText);
                devotionalActivityIntent.putExtra("BibleInAYear", BibleInAYear);
                devotionalActivityIntent.putExtra("Date", Date);
                devotionalActivityIntent.putExtra("Image", Image);
                c.startActivity(devotionalActivityIntent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return arrayListOfDays.size();
    }
}
