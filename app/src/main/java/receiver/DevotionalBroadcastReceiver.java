package receiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Vibrator;
import android.provider.Settings;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.example.cpaisecretplacedevotional.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import ui.DevotionalActivity;

import static android.content.Context.MODE_PRIVATE;
import static application.BaseApplication.CHANNEL_1_ID;

public class DevotionalBroadcastReceiver extends BroadcastReceiver {
    static NotificationManager notificationManager;
    static MediaPlayer mediaPlayer;
    static Vibrator vibrator;
    public static final String TAG = "DevotionalBroadcast";

    @Override
    public void onReceive(Context context, Intent intent) {

        Log.i(TAG, "onReceive: yeah.....");
        if (intent != null) {
            String broadcast_info = intent.getStringExtra("ACTION_TYPE");
            if (broadcast_info != null) {
                if (broadcast_info.contentEquals("alarm")) {
                    Log.i(TAG, "onReceive: yeah");
                    showStartAlarmAndNotification(context);
                    firstDurationTimer();
                    secondDurationTimer(context);
                    thirdDurationTimer(context);

                } else if (broadcast_info.contentEquals("snooze")) {
                    Log.i(TAG, "onReceive: Snooze devotion notification");
                    cancelAlarmAndNotification();
                    startDurationTimerForSnooze(context);
                }
            }
        }
    }

    private void firstDurationTimer() {
        final Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Log.i(TAG, "start duration timer");
                cancelAlarmAndNotification();
                timer.cancel();
            }
        }, 60000, 60000);
    }
    private void secondDurationTimer(final Context context) {
        final Timer secondAlarm = new Timer();
        secondAlarm.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                showStartAlarmAndNotification(context);
                firstDurationTimer();
                secondAlarm.cancel();
            }
        }, 2 * 60000, 60000 );
    }

    private void thirdDurationTimer(final Context context) {
        final Timer thirdAlarm = new Timer();
        thirdAlarm.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                showStartAlarmAndNotification(context);
                firstDurationTimer();
                thirdAlarm.cancel();
            }
        }, 4 * 60000, 60000 );
    }
    private void startDurationTimerForSnooze(final Context context){
        final Timer snoozeTimer = new Timer();
        snoozeTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Log.i(TAG, "startDurationTimerForSnooze: ");
                showStartAlarmAndNotification(context);
                firstDurationTimer();
                snoozeTimer.cancel();
            }
        }, 60000, 60000);
    }

    public void cancelAlarmAndNotification() {
        Log.i(TAG, "cancelAlarmAndNotification");
        if (notificationManager != null) {
            Log.i(TAG, "cancelAlarmAndNotification: cancel notificationManager");
            notificationManager.cancel(1);
        }
//        if (mediaPlayer != null) {
//            Log.i(TAG, "cancelAlarmAndNotification: cancel mediaPlayer");
//            mediaPlayer.stop();
//        }
//        if (vibrator != null) {
//            Log.i(TAG, "cancelAlarmAndNotification: cancel vibration");
//            vibrator.cancel();
//        }

    }

    private void showStartAlarmAndNotification(Context context) {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        String currentDate = dateFormat.format(cal.getTime());

        SharedPreferences sharedPreferences =  context.getSharedPreferences("Devotional_Tracker", MODE_PRIVATE);
        String retrievedDate =  sharedPreferences.getString("last_opened", "");
        Log.i(TAG, "Retrieved Date1");
        Log.i(TAG, currentDate);
        Log.i(TAG, retrievedDate);
        if (!retrievedDate.contentEquals(currentDate)){
            Log.i(TAG, "Retrieved Date2");
            //startAlarm(context);
            showInsistentNotification(context);
        }
    }

    private void showInsistentNotification(Context context) {
        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        Intent activity_intent = new Intent(context.getApplicationContext(), DevotionalActivity.class);
        activity_intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        activity_intent.putExtra("ACTION_TYPE", "openHomePage");
        PendingIntent pendingIntent = PendingIntent.getActivity(context.getApplicationContext(), 0, activity_intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent snoozeIntent = new Intent(context.getApplicationContext(), DevotionalBroadcastReceiver.class);
        snoozeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        snoozeIntent.putExtra("ACTION_TYPE", "snooze");
        PendingIntent snoozePendingIntent = PendingIntent.getBroadcast(context.getApplicationContext(), 0, snoozeIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Ringtone ringtone = RingtoneManager.getRingtone(context, alarmSound);
        ringtone.play();
        //mediaPlayer.start();
        long[] vibratePattern = new long[]{0, 400, 200, 400, 200};
        Notification notification = new NotificationCompat.Builder(context, CHANNEL_1_ID)
                .setContentIntent(pendingIntent)
                .setFullScreenIntent(pendingIntent, true)
                .setSmallIcon(R.drawable.ic_alarm)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_alarm))
                .setContentTitle("Secret Place Alarm")
                .setContentText("Its time for devotion!")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_ALARM)
                .addAction(R.drawable.ic_snooze, "Snooze", snoozePendingIntent) //snooze? really?
                .setColor(Color.BLUE)
                .setSound(alarmSound)
                .setOngoing(true)
                .setVibrate(vibratePattern)
                .setAutoCancel(true)
                .setLights(Color.RED, 3000, 3000)
                .build();
        notification.flags = Notification.FLAG_INSISTENT;


        if (notificationManager != null) {
            notificationManager.notify(1, notification);
        }

    }

//    private void startAlarm(Context context) {
//        mediaPlayer = MediaPlayer.create(context.getApplicationContext(), Settings.System.DEFAULT_ALARM_ALERT_URI);
//        mediaPlayer.setLooping(true);
//        mediaPlayer.start();
//        vibrator = (Vibrator) context.getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
//        long[] vibratePattern = new long[]{0, 400, 200, 400, 200};
//        vibrator.vibrate(vibratePattern, 0);
//    }
}
