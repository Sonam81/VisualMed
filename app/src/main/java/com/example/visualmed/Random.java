package com.example.visualmed;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Random extends AppCompatActivity {

    private Button bb;
    boolean bounded;
    private TextView text;
    TtsService ttsService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_random);
        bb = findViewById(R.id.bb);
        text = findViewById(R.id.tvtv);

        bb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //text.setText(ttsService.getTime());

                ttsService.speak("Maa Man");
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        Intent mIntent = new Intent(this, TtsService.class);
        bindService(mIntent, mConnection, BIND_AUTO_CREATE);
    }

    ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceDisconnected(ComponentName name) {
            Toast.makeText(Random.this, "Service is disconnected", Toast.LENGTH_LONG).show();
            bounded = false;
            ttsService = null;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Toast.makeText(Random.this, "Service is CConnected", Toast.LENGTH_LONG).show();
            bounded = true;
            TtsService.LocalBinder mLocalBinder = (TtsService.LocalBinder)service;
            ttsService = mLocalBinder.getServerInstance();
        }
    };
}
