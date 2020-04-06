package com.example.visualmed;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.util.Log;
import android.widget.Toast;

public class AlertReciever extends BroadcastReceiver {
    MediaPlayer mediaPlayer;
    public void onReceive (Context context , Intent intent) {
        mediaPlayer = MediaPlayer.create(context,R.raw.jungle);
        mediaPlayer.start();
        Log.i("Triggerd","Triggered");
        Toast.makeText(context,"ALarm Triggered",Toast.LENGTH_LONG).show();
    }
}
