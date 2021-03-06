package ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager2.widget.ViewPager2;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cpaisecretplacedevotional.DevotionalOnboardingModel;
import com.example.cpaisecretplacedevotional.IntroductoryFragment;
import com.example.cpaisecretplacedevotional.R;

import java.util.ArrayList;
import java.util.List;

import ui.MainActivity;
import ui.OnBoardingViewPageAdapter;

public class DevotionalOnBoardingActivity extends AppCompatActivity {
    private static final String TAG = "Onboarding Activity";
    OnBoardingViewPageAdapter onBoardingViewPageAdapter;
    LinearLayout layoutOnBoardingIndicators;
    ImageButton onBoardingActionButton;
    ImageButton onBoardingPrevButton;
    FrameLayout fragmentFrameLayout;
    public static FragmentManager fragmentManager;
    Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_devotional_on_boarding);
        context = this;

        layoutOnBoardingIndicators = findViewById(R.id.onBoardingIndicatorsId);
        onBoardingActionButton = findViewById(R.id.btnNextId);
        onBoardingPrevButton = findViewById(R.id.btnPrev);
        fragmentFrameLayout = findViewById(R.id.fragmentFrameLayoutID);


        fragmentManager = getSupportFragmentManager();

        setUpOnBoardingItems();

        final ViewPager2 onBoardingViewPager = findViewById(R.id.viewPagerId);
        onBoardingViewPager.setAdapter(onBoardingViewPageAdapter);

        setUpOnBoardingIndicators();

        setCurrentOnBoardingIndicator(0);
        Log.i(TAG, "onCreate: " + onBoardingViewPager.getCurrentItem());
        Log.i(TAG, "onCreate: " + onBoardingViewPageAdapter.getItemCount());


        onBoardingViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                setCurrentOnBoardingIndicator(position);

                if (onBoardingViewPager.getCurrentItem() > 0) {
                    onBoardingPrevButton.setVisibility(View.VISIBLE);
                } else {
                    onBoardingPrevButton.setVisibility(View.GONE);
                }
            }
        });

        onBoardingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "onClick: " );
                if (onBoardingViewPager.getCurrentItem()+1 < onBoardingViewPageAdapter.getItemCount()) {
                    onBoardingViewPager.setCurrentItem(onBoardingViewPager.getCurrentItem()+1);
                }else{
                    onBoardingViewPager.setVisibility(View.GONE);
                    layoutOnBoardingIndicators.setVisibility(View.GONE);
                    onBoardingActionButton.setVisibility(View.GONE);
                    onBoardingPrevButton.setVisibility(View.GONE);
                    fragmentFrameLayout.setVisibility(View.VISIBLE);

                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    IntroductoryFragment introductoryFragment = new IntroductoryFragment();
                    fragmentTransaction.add(R.id.fragmentFrameLayoutID, introductoryFragment, null);
                    fragmentTransaction.commit();



                }
            }
        });

        onBoardingPrevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBoardingViewPager.setCurrentItem(onBoardingViewPager.getCurrentItem()-1);
            }
        });

    }


    private void setUpOnBoardingItems () {
        List<DevotionalOnboardingModel> onBoardingItems = new ArrayList<>();

        DevotionalOnboardingModel introModelStudy = new DevotionalOnboardingModel();
        introModelStudy.setOnBoardingTitle("Study");
        introModelStudy.setOnBoardingDescription("Study to show yourself approved");
        introModelStudy.setOnBoardingImage(R.drawable.study);
        onBoardingItems.add(introModelStudy);

        DevotionalOnboardingModel introModelPray = new DevotionalOnboardingModel();
        introModelPray.setOnBoardingTitle("Pray");
        introModelPray.setOnBoardingDescription("Pray without ceasing. The fervent prayer of the righteous makes tremendous power available.");
        introModelPray.setOnBoardingImage(R.drawable.pray);
        onBoardingItems.add(introModelPray);

        DevotionalOnboardingModel introModelWithoutDistractions = new DevotionalOnboardingModel();
        introModelWithoutDistractions.setOnBoardingTitle("Without Distractions");
        introModelWithoutDistractions.setOnBoardingDescription("Avoid Distractions while you focus on God in the course on your devotion");
        introModelWithoutDistractions.setOnBoardingImage(R.drawable.without_distractions);
        onBoardingItems.add(introModelWithoutDistractions);

        onBoardingViewPageAdapter = new OnBoardingViewPageAdapter(onBoardingItems);
    }
    private void setUpOnBoardingIndicators() {
        ImageView[] indicators = new ImageView[onBoardingViewPageAdapter.getItemCount()];
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(8, 0, 8, 0);
        for (int i = 0; i<indicators.length; i++) {
            indicators[i] = new ImageView(getApplicationContext());
            indicators[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.onboarding_indicator_inactive));
            indicators[i].setLayoutParams(layoutParams);

            layoutOnBoardingIndicators.addView(indicators[i]);
        }
    }
    private void setCurrentOnBoardingIndicator (int index) {
        int childCount = layoutOnBoardingIndicators.getChildCount();

        for(int i = 0; i < childCount; i++) {
            ImageView imageView = (ImageView) layoutOnBoardingIndicators.getChildAt(i);

            if(i == index) {
                imageView.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.onboarding_indicator_active));
            } else {
                imageView.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.onboarding_indicator_inactive));
            }
        }
//        if (index == introAdapter.getItemCount()-1) {
//            onBoardingActionButton.setText("Start");
//        } else {
//            onBoardingActionButton.setText("Next");
//        }
    }
}
