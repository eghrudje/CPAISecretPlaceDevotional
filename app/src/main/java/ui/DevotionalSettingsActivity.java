package ui;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.cpaisecretplacedevotional.R;

import java.text.DateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import receiver.DevotionalBroadcastReceiver;

import static android.widget.Toast.LENGTH_LONG;

public class DevotionalSettingsActivity extends AppCompatActivity {

    private Spinner spinner;
    final List<String> languages = Arrays.asList("Select Language", "English", "French");
    private Switch switchDailyRemind;
    private EditText editText;
    TimePickerDialog timePickerDialog;
    Calendar calendar;
    AlarmManager alarmManager;
    int setHour;
    int setMinute;
    String ampm;
    SharedPreferences sharedPreferences;
    public static final String MyPreferences = "MyPref";
    public static final String alarmMessage = "alarmMessageKey";
    public static final String TAG = "SettingsActivity";
    String selectedLanguage = null;
    String selectedAlarmTime = null;
    Button devotionalSettingsDone, devotionalSettingsSkip;
    ToggleButton airPlaneBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_devotional_settings);


        spinner = findViewById(R.id.languageSpinner);
        devotionalSettingsDone = findViewById(R.id.done);
        devotionalSettingsSkip = findViewById(R.id.skip);

        airPlaneBtn = findViewById(R.id.btnAirPlaneId);
        initAirPlaneModeBtn();

        editText = findViewById(R.id.editTime);
        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar = Calendar.getInstance();

                timePickerDialog = new TimePickerDialog(DevotionalSettingsActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        setHour = hourOfDay;
                        setMinute = minute;
                        calendar = Calendar.getInstance();

                        calendar.set(Calendar.HOUR_OF_DAY, setHour);
                        calendar.set(Calendar.MINUTE, setMinute);
                        calendar.set(Calendar.SECOND, 0);

                        String alarmText = hourOfDay + ":" + minute;
                        selectedAlarmTime = alarmText;

                        if (hourOfDay >= 12) {
                            ampm = "PM";
                        } else {
                            ampm = "AM";
                        }
                        editText.setText(String.format("%02d:%02d", hourOfDay, minute) + (hourOfDay >= 12 ? "PM" : "AM"));
                        //editText.setText(hourOfDay + ":" + minute + ampm);
                        devotionalSettingsDone.setVisibility(View.VISIBLE);
                        devotionalSettingsSkip.setVisibility(View.VISIBLE);
                    }
                }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false);

                timePickerDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        Log.i("Devotional", "onCancel: Called");
                        cancelAlarm();
                    }
                });
                timePickerDialog.show();
            }
        });
        //Seems to be working ok. The right code block is being ran. Hmm. Are you there? Yes. The buttons show once i enter the settings page.
        // Oh. I was asking to check that the funtionality was working as it was before, I wanted to make sure we didn;t break anything. Ok.
        //TeYou can test that.. I have to for for some minutes now.
        devotionalSettingsDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkPreferenceSelectionChanged("language")){
                    Log.i(TAG, "Selected language is not null: ");

                    setLocale(selectedLanguage);

                } else {
                    Log.i(TAG, "Selected language is null: ");
                }

                if (checkPreferenceSelectionChanged("alarm")){
                    Log.i(TAG, "Selected alarm is not null: ");

                    sharedPreferences = getSharedPreferences(MyPreferences, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(alarmMessage, selectedAlarmTime);
                    editor.apply();

                    setAlarm(calendar.getTimeInMillis());

                } else {
                    Log.i(TAG, "Selected alarm is null: ");
                }
                Toast.makeText(getApplicationContext(), "Settings Recognized", Toast.LENGTH_SHORT).show();
            }
        });
        devotionalSettingsSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent skipSettingsPageIntent = new Intent(getApplicationContext(), MainActivity.class);
//                skipSettingsPageIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(skipSettingsPageIntent);
                finish();
            }
        });

        switchDailyRemind = findViewById(R.id.switchDailyReminder);

        switchDailyRemind.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    editText.setEnabled(true);
                    editText.setFocusable(true);
                    editText.setCursorVisible(true);
                } else {
                    editText.setEnabled(false);
                    editText.setFocusable(false);
                    editText.setCursorVisible(false);
                    editText.setKeyListener(null);
                    editText.setBackgroundColor(Color.TRANSPARENT);
                }
            }
        });
        getSupportActionBar().setTitle(getResources().getString(R.string.settings_title));
        //getSupportActionBar().


        ArrayAdapter<String> dataAdapter;
        dataAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, languages);

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (parent.getItemAtPosition(position).equals("Select Language")) {

                } else {
                    String language = parent.getItemAtPosition(position).toString();
                    if (language.equals("French")) {
                        selectedLanguage = "fr";
                        if (checkPreferenceSelectionChanged("language")){
                            devotionalSettingsDone.setVisibility(View.VISIBLE);
                            devotionalSettingsSkip.setVisibility(View.VISIBLE);
                        }
                    }else if (language.equals("English")) {
                        selectedLanguage = "en";
                        if (checkPreferenceSelectionChanged("language")){
                            devotionalSettingsDone.setVisibility(View.VISIBLE);
                            devotionalSettingsSkip.setVisibility(View.VISIBLE);
                        }

                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        settingsPageUpdate();
    }

    private void setLocale(String lang) {
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
        SharedPreferences.Editor editor = getSharedPreferences("langSetting", MODE_PRIVATE).edit();
        editor.putString("My_Lang", lang);
        editor.apply();
    }
    public void loadLocale() {
        SharedPreferences prefs = getSharedPreferences("langSetting", Activity.MODE_PRIVATE);
        String dialet = prefs.getString("My_Lang", "en");
        selectedLanguage = dialet;
        Log.i("Devotional", dialet);
        Locale locale = new Locale(dialet);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
        String fullLengthLanguage = convertLanguageToFullLength(dialet);
        int index = languages.indexOf(fullLengthLanguage);
        spinner.setSelection(index);
    }

    private String convertLanguageToFullLength(String shortForm) {
        switch (shortForm) {
            case "fr": {
                return "French";
            }
            case "en": {
                return "English";
            }


        }
        return "Select Language";
    }

    public void settingsPageUpdate() {
        updateAlarmText();
        loadLocale();
    }
    private void setAlarm(long timeInMillis) {
        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent alarm_intent = new Intent(getApplicationContext(), DevotionalBroadcastReceiver.class);
        alarm_intent.putExtra("ACTION_TYPE", "alarm");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, alarm_intent, 0);
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, timeInMillis, AlarmManager.INTERVAL_DAY, pendingIntent);


    }
    public void updateAlarmText(){
        SharedPreferences sharedPreferences = getSharedPreferences(MyPreferences, MODE_PRIVATE);
        String savedTime = sharedPreferences.getString(alarmMessage, "No Alarm Set");
        selectedAlarmTime = savedTime;
        Calendar calendar = Calendar.getInstance();
        if (!savedTime.contentEquals("No Alarm Set")) {
            String hourString = savedTime.split(":")[0];
            String minuteString = savedTime.split(":")[1];
            int hour = Integer.valueOf(hourString);
            int min = Integer.valueOf(minuteString);

            calendar.set(
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH),
                    hour,
                    min,
                    0
            );
            editText.setText(DateFormat.getTimeInstance(DateFormat.SHORT).format(calendar.getTime()));
        } else {
            editText.setText("Select Time");
        }
    }
    private void cancelAlarm() {
        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, DevotionalBroadcastReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
        alarmManager.cancel(pendingIntent);

        editText.setText("Select Time");
        Toast.makeText(this, "Alarm Cancelled", LENGTH_LONG).show();

    }
    //Select fr ench and then click done. Ok
    private boolean checkPreferenceSelectionChanged(String key) {
        if (key.contentEquals("language")) {
            SharedPreferences prefs = getSharedPreferences("langSetting", Activity.MODE_PRIVATE);
            String dialet = prefs.getString("My_Lang", "en");
            return !selectedLanguage.contentEquals(dialet);
        }
        if (key.contentEquals("alarm")){
            SharedPreferences sharedPreferences = getSharedPreferences(MyPreferences, MODE_PRIVATE);
            String savedTime = sharedPreferences.getString(alarmMessage, "No Alarm Set");
            return !selectedAlarmTime.contentEquals(savedTime);
        }

        return false;
    }

    private void initAirPlaneModeBtn() {
        airPlaneBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (Build.VERSION.SDK_INT < 17) {
                    try {
                        boolean isEnabled = Settings.System.getInt(
                                getContentResolver(),
                                Settings.System.AIRPLANE_MODE_ON, 0) == 1;
                        Settings.System.putInt(
                                getContentResolver(),
                                Settings.System.AIRPLANE_MODE_ON, isEnabled ? 0 : 1);

                        Intent intent = new Intent((Intent.ACTION_AIRPLANE_MODE_CHANGED));
                        intent.putExtra("state", !isEnabled);
                        sendBroadcast(intent);
                    } catch (ActivityNotFoundException e) {
                        Log.e(TAG, "onCheckedChanged: "+ e.getMessage());
                    }
                }else {
                    try {
                        Intent intent = new Intent(Settings.ACTION_AIRPLANE_MODE_SETTINGS);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }catch (ActivityNotFoundException e) {
                        try {
                            Intent intent = new Intent("android.settings.WIRELESS_SETTINGS");
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }catch (ActivityNotFoundException ex) {
                            Toast.makeText(buttonView.getContext(), "bbbbbb", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });
    }
}
