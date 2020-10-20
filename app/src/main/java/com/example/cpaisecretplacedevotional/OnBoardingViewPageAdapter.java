package com.example.cpaisecretplacedevotional;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class OnBoardingViewPageAdapter extends RecyclerView.Adapter<OnBoardingViewPageAdapter.OnBoardingViewHolder> {

    private List<DevotionalOnboardingModel> onBoardingItems;

    public OnBoardingViewPageAdapter(List<DevotionalOnboardingModel> onBoardingItems) {
        this.onBoardingItems = onBoardingItems;
    }
    Context context;
    ArrayList<DevotionalOnboardingModel> arrayListOfOnBoardingItems;

    public OnBoardingViewPageAdapter(Context context, ArrayList<DevotionalOnboardingModel> arrayListOfOnBoardingItems) {
        this.arrayListOfOnBoardingItems = arrayListOfOnBoardingItems;
    }

    @NonNull
    @Override
    public OnBoardingViewPageAdapter.OnBoardingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new OnBoardingViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.onboarding_screen_layout, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull OnBoardingViewPageAdapter.OnBoardingViewHolder holder, int position) {
        holder.setOnBoardingData(onBoardingItems.get(position));
    }

    @Override
    public int getItemCount() {
        return onBoardingItems.size();
    }

    class OnBoardingViewHolder extends RecyclerView.ViewHolder {
        private TextView titleTextView;
        private TextView descriptionTextView;
        private ImageView onBoardingImage;

        public OnBoardingViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.titleId);
            descriptionTextView = itemView.findViewById(R.id.descriptionId);
            onBoardingImage = itemView.findViewById(R.id.onBoardingImageID);
        }
        void setOnBoardingData(DevotionalOnboardingModel introModel) {
            titleTextView.setText(introModel.getOnBoardingTitle());
            descriptionTextView.setText(introModel.getOnBoardingDescription());
            onBoardingImage.setImageResource(introModel.getOnBoardingImage());

        }
    }
}
