package ui;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.cpaisecretplacedevotional.DailyDevotionalsPerMonthActivity;
import com.example.cpaisecretplacedevotional.MonthDevotionalsPerYearActivity;
import com.example.cpaisecretplacedevotional.R;
import com.example.cpaisecretplacedevotional.YearDevotional;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import service.Devotional;
import service.DevotionalRealmHelper;
import service.ItemClickListener;
import service.MonthDevotional;

public class DevotionalAdapter extends RecyclerView.Adapter<DevotionalHolder> {

    public Context c;
    public ArrayList<Object> models;

    DevotionalRealmHelper devotionalRealmHelper = DevotionalRealmHelper.getInstance(c);

    private final String TAG = DevotionalAdapter.class.getSimpleName();

    public DevotionalAdapter(Context c, ArrayList<Object> models) {
        this.c = c;
        this.models = models;
        Log.d(TAG, "models: " + models);
    }

    @NonNull
    @Override
    public DevotionalHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row, null);
        return new DevotionalHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final DevotionalHolder holder, int position) {
        Object object = models.get(position);

        if (object instanceof Devotional) {
            final Devotional objDevotional = (Devotional) object;
            Log.i(TAG, "onBindViewHolder: " + objDevotional);
            holder.title.setText(objDevotional.getTitle());
            holder.date.setText(objDevotional.getDate());
            holder.text.setText(objDevotional.getText());
            Log.i(TAG, "onBindViewHolder: " + holder.title + holder.date + holder.text);

            holder.devotionalCardview.setVisibility(View.VISIBLE);
            holder.monthCardview.setVisibility(View.GONE);
            holder.yearCardview.setVisibility(View.GONE);

            Calendar cal = Calendar.getInstance();
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
            String currentDate = dateFormat.format(cal.getTime());

            if (currentDate.contentEquals(objDevotional.getDate())) {
                holder.todayText.setVisibility(View.VISIBLE);
                holder.date.setVisibility(View.INVISIBLE);
            } else {
                holder.todayText.setVisibility(View.INVISIBLE);
                holder.date.setVisibility(View.VISIBLE);
            }


            Glide.with(c).load(objDevotional.getImage()).into(holder.imageView);

            holder.setItemClickListener(new ItemClickListener() {
                @Override
                public void onItemClickListener(View v, int position) {
                    String Title = objDevotional.getTitle();
                    String Date = objDevotional.getDate();
                    String Image = objDevotional.getImage();
                    String Text = objDevotional.getText();
                    String Author = objDevotional.getAuthor();
                    String MemoryVerse = objDevotional.getMemoryVerse();
                    String MemoryVersePassage = objDevotional.getMemoryVersePassage();
                    String BibleInAYear = objDevotional.getBibleInAYear();
                    String FullPassage = objDevotional.getFullPassage();
                    String FullText = objDevotional.getFullText();
                    int DevotionalId = objDevotional.getDevotional_id();

                    Glide.with(c).load(Image).into(holder.imageView);

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

        } else if (object instanceof MonthDevotional) {
            final MonthDevotional objMonthDevotional = (MonthDevotional) object;
            Log.i(TAG, "onBindViewHolder: " + objMonthDevotional);

            final String monthName = objMonthDevotional.getMonthName();
            final String monthPic = objMonthDevotional.getMonthImage();
            final String monthId = objMonthDevotional.getMonthDevotional_id();

            Log.i(TAG, "onBindViewHolder(MonthDevotionalOnHOmePage): " + monthPic);
            final int res = c.getResources().getIdentifier(monthPic.toLowerCase(), "drawable", c.getPackageName());
            Log.i(TAG, "onBindViewHolder(MonthDevotionalOnHOmePage): " + res);

            //String monthNameString = dateString.substring(0, dateString.length() - 4);
            holder.monthTitle.setText(monthName);

            holder.devotionalCardview.setVisibility(View.GONE);
            holder.monthCardview.setVisibility(View.VISIBLE);
            holder.yearCardview.setVisibility(View.GONE);

            Glide.with(c).load(res).into(holder.monthImageView);


            Log.i(TAG, "onBindViewHolder(MonthDevotionalOnHOmePage): " + monthId);
            Log.i(TAG, "onBindViewHolder(MonthDevotionalOnHOmePage): " + monthName);
            //Log.i(TAG, "onBindViewHolder(MonthDevotional): " + devotionalRealmHelper);
            final ArrayList<Devotional> devotionalsPerMonth = devotionalRealmHelper.fetchListOfDevotionalsPerMonth(monthId);

            if(devotionalsPerMonth.isEmpty()) {
                holder.downloadMonthDevotionalImageButton.setVisibility(View.VISIBLE);
            } else {
                holder.downloadMonthDevotionalImageButton.setVisibility(View.GONE);
            }

            holder.setItemClickListener(new ItemClickListener() {
                @Override
                public void onItemClickListener(View v, int position) {
                    //holder.downloadMonthDevotionalImageButton.setVisibility(View.GONE);

                    Log.i(TAG, "onItemClickListener: Tapped MonthDevotional On MainActivity" );

                    Intent daysPerMonthActivityIntent = new Intent(c, DailyDevotionalsPerMonthActivity.class);
                    daysPerMonthActivityIntent.putExtra("MonthIdentity", monthId);
                    daysPerMonthActivityIntent.putExtra("monthImage", monthPic);
                    daysPerMonthActivityIntent.putExtra("monthName", monthName);
                    c.startActivity(daysPerMonthActivityIntent);
                }
            });

        } else if (object instanceof YearDevotional) {
            final YearDevotional objYearDevotional = (YearDevotional) object;
            Log.i(TAG, "onBindViewHolder: " + objYearDevotional);
            holder.yearTitle.setText(objYearDevotional.getYearName());

            holder.devotionalCardview.setVisibility(View.GONE);
            holder.monthCardview.setVisibility(View.GONE);
            holder.yearCardview.setVisibility(View.VISIBLE);

            final String year = objYearDevotional.getYearName();
            String yearImg = objYearDevotional.getYearImage();

            final int res = c.getResources().getIdentifier(yearImg, "drawable", c.getPackageName());

            Log.i(TAG, "onBindViewHolder(YearDevotionalOnHOmePage): " + res);
            Log.i(TAG, "onBindViewHolder(YearDevotionalOnHOmePage): " + yearImg);

            Glide.with(c).load(res).into(holder.yearImageView);

            holder.setItemClickListener(new ItemClickListener() {
                @Override
                public void onItemClickListener(View v, int position) {


                    Intent monthDevotionalsPerYearActivityIntent = new Intent(c, MonthDevotionalsPerYearActivity.class);
                    monthDevotionalsPerYearActivityIntent.putExtra("yearName", year);
                    //monthDevotionalsPerMonthActivityIntent.putExtra("Months", listOfMonths);
                    c.startActivity(monthDevotionalsPerYearActivityIntent);
                }
            });

        }
    }
    @Override
    public int getItemCount() {
        return models.size();
    }
}
