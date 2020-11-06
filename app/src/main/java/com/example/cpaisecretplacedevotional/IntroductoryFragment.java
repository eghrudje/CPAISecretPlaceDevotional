 package com.example.cpaisecretplacedevotional;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import ui.DevotionalSettingsActivity;
import ui.MainActivity;

import static androidx.core.content.ContextCompat.getSystemService;


 /**
 * A simple {@link Fragment} subclass.
 */
public class IntroductoryFragment extends Fragment {
     private static final String TAG = "Introductory Fragment";
     Button introButton, proceedButton;
     TextView introTextView;
     Activity activity;
     ImageView imageActive, imageInactive;
     Boolean isModeSet = false;

    public IntroductoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        activity = getActivity();

        View view = inflater.inflate(R.layout.fragment_introductory, container, false);
        introButton = view.findViewById(R.id.setUpID);
        introTextView = view.findViewById(R.id.introTextID);
        imageInactive = view.findViewById(R.id.imageOneID);
        imageActive = view.findViewById(R.id.imageTwoID);
        proceedButton = view.findViewById(R.id.proceedID);

        introButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isModeSet = true;
                NotificationManager notificationManager = (NotificationManager) activity.getSystemService(getActivity().NOTIFICATION_SERVICE);
                if (!notificationManager.isNotificationPolicyAccessGranted()) {
                    Intent intent = new Intent(android.provider.Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS);
                    startActivityForResult(intent, DevotionalSettingsActivity.RESULT_OK);
                } else {
                    Toast.makeText(getActivity(), "Do not disturb feature already set", Toast.LENGTH_LONG).show();
                }
            }
        });
        return view;
    }

     @Override
     public void onResume() {
         super.onResume();
         Log.i(TAG, "onResume: Hey Work na");
         NotificationManager notificationManager = (NotificationManager) activity.getSystemService(getActivity().NOTIFICATION_SERVICE);
         if (notificationManager.isNotificationPolicyAccessGranted()) {
             AudioManager audioManager = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);
             audioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);

             introButton.setText("Proceed");
             introTextView.setText("You can now proceed. Enjoy your time with the Lord.");
             imageActive.setVisibility(View.GONE);
             imageInactive.setVisibility(View.GONE);

             introButton.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View v) {
                     Intent intent = new Intent(getActivity(), MainActivity.class);
                     startActivity(intent);
                     introButton.setEnabled(false);
                     //finish();
                 }
             });
         } else {
             Log.i(TAG, "onResume: MAIN ELSE");
             if (isModeSet == true) {
                 Log.i(TAG, "onResume: SECOND IF");
                 introButton.setText("Proceed");
                 introTextView.setText("You can now proceed. Enjoy your time with the Lord.");
                 imageActive.setVisibility(View.GONE);
                 imageInactive.setVisibility(View.GONE);

                 introButton.setOnClickListener(new View.OnClickListener() {
                     @Override
                     public void onClick(View v) {
                         Intent intent = new Intent(getActivity(), MainActivity.class);
                         startActivity(intent);
                         introButton.setEnabled(false);
                     }
                 });
             }
         }

     }


 }
