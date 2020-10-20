package application;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class BaseApplication extends Application {
    public static final String CHANNEL_1_ID = "alarmChannel";
    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel();
        Realm.init(this);
        RealmConfiguration configuration = new RealmConfiguration.Builder().name("DevotionalData.realm").build();
        Realm.setDefaultConfiguration(configuration);
    }

    private void createNotificationChannel() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel1 = new NotificationChannel(
                    CHANNEL_1_ID,
                    "Channel 1",
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel1.setDescription("This is the alarm channel");
             channel1.enableVibration(true);
             channel1.shouldVibrate();

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel1);
        }
    }
}
