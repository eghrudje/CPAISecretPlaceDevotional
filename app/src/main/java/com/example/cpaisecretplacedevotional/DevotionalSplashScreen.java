package com.example.cpaisecretplacedevotional;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import ui.MainActivity;

public class DevotionalSplashScreen extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sp = getSharedPreferences("App_Launch_Tracker", MODE_PRIVATE);
        if (sp.getString("true", "FirstTime") == "FirstTime") {
            SharedPreferences.Editor editor = sp.edit();
            editor.putString("true", "notFirstTime");
            editor.commit();

            Intent intent = new Intent(this, DevotionalOnBoardingActivity.class);
            startActivity(intent);
        } else {

            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }

    }
}
