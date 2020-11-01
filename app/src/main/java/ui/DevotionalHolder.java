package ui;

import android.os.SystemClock;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cpaisecretplacedevotional.R;

import service.ItemClickListener;

public class DevotionalHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private long mLastClickTime;
    private static final long CLICK_TIME_INTERVAL =300;
    public ImageView imageView, monthImageView, yearImageView, downloadMonthDevotionalImageButton;
    public TextView date, text, title, todayText, monthTitle, yearTitle;
    public ItemClickListener itemClickListener;
    public CardView monthCardview, yearCardview, devotionalCardview;

    public DevotionalHolder(@NonNull View itemView) {
        super(itemView);

        this.imageView = itemView.findViewById(R.id.bibleImage);
        this.monthImageView = itemView.findViewById(R.id.monthDevotionalImage);
        this.yearImageView = itemView.findViewById(R.id.yearDevotionalImage);
        this.date = itemView.findViewById(R.id.date);
        this.text = itemView.findViewById(R.id.shortTextDev);
        this.title = itemView.findViewById(R.id.titleDev);
        this.todayText = itemView.findViewById(R.id.today);
        this.monthTitle = itemView.findViewById(R.id.monthTitleId);
        this.yearTitle = itemView.findViewById(R.id.yearTitleId);
        this.monthCardview = itemView.findViewById(R.id.monthCardviewId);
        this.yearCardview = itemView.findViewById(R.id.yearCardviewId);
        this.devotionalCardview = itemView.findViewById(R.id.devotionalCardviewId);
        this.downloadMonthDevotionalImageButton = itemView.findViewById(R.id.downloadMonthDevotionalId);


        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(SystemClock.elapsedRealtime() - mLastClickTime < 1000 ) {
            return;
        }
        mLastClickTime = SystemClock.elapsedRealtime();

        this.itemClickListener.onItemClickListener(v, getLayoutPosition());
    }
    public void setItemClickListener(ItemClickListener ic){
        this.itemClickListener = ic;
    }
}
