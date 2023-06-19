package com.example.hgym;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

//setting receiver for the alarm.
public class AlarmReceiver extends BroadcastReceiver {
    //in charge of whats happens when receiving a notification.
    @Override
    public void onReceive(Context context, Intent intent){
        Intent intent1 = new Intent(context, DestinationActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context,0,intent1,0);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context,"nhksd")
                .setSmallIcon(R.drawable.noam)
                .setContentTitle("you have a practice!")
                .setContentText("you have a training practice soon, tap to see more.")
                .setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent);
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        notificationManagerCompat.notify(123,builder.build());
    }
}
