package com.example.visualmed;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

public class AlertReceiver extends BroadcastReceiver {
    MediaPlayer mediaPlayer;
    public void onReceive (Context context , Intent intent) {
        String medicine_name = intent.getStringExtra("medicine_name");
        mediaPlayer = MediaPlayer.create(context,R.raw.beep);
        mediaPlayer.start();

        Intent resultIntent = new Intent(context, ReadMedicine.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addNextIntentWithParentStack(resultIntent);
        PendingIntent navPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.logo)
                .setTicker("Medicine alert")
                .setContentTitle("VisualMed")
                .setContentText("Time to take your medicine "+medicine_name.substring(0, 1).toUpperCase() + medicine_name.substring(1))
                .setAutoCancel(true)
                .setContentIntent(navPendingIntent);


        NotificationManager notificationmanager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        notificationmanager.notify((int) System.currentTimeMillis(), builder.build());

        Toast.makeText(context,"ALarm Triggered",Toast.LENGTH_LONG).show();
    }
}
