package com.example.hgym;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.os.IBinder;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
//intent for the service, not in use.
public class MyService extends Service {

    private static final String CHANNEL_ID = "12345";
    NotificationManager manager;
    //empty builder.
    public MyService(){

    }
    //binding
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    //starting the service.
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("Noam", "Success");

        ChannelCreation();
        Log.d("Noam", "message 1");
        CreateNotification();

        Log.d("Noam", "message 2");

        return START_STICKY;
    }
    //creating the channel.
    public void ChannelCreation(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,"My Notification", NotificationManager.IMPORTANCE_DEFAULT);
            manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);

        }
    }

    //creating a notification.
    public void CreateNotification(){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID);
        builder.setSmallIcon(R.drawable.ic_launcher_background);
        builder.setContentTitle("My notification");
        builder.setContentText("Hello World!");
        builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);
        builder.setAutoCancel(true);




        // notificationId is a unique int for each notification that you must define
        manager.notify(1, builder.build());

    }


}