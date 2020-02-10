package com.example.visualmed;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.example.visualmed.TtsService.LocalBinder;

public class ManageMedicineActivity extends Activity {

    boolean mBounded;
    TtsService mServer;
    TextView text;
    Button button;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_medicine);

        text = (TextView)findViewById(R.id.tv);
        button = (Button) findViewById(R.id.b);
        button.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                mServer.speak("Started form the buttom");
                text.setText(mServer.getTime());
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
            Toast.makeText(ManageMedicineActivity.this, "Service is disconnected", Toast.LENGTH_LONG).show();
            mBounded = false;
            mServer = null;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Toast.makeText(ManageMedicineActivity.this, "Service is connected", Toast.LENGTH_LONG).show();
            mBounded = true;
            LocalBinder mLocalBinder = (LocalBinder)service;
            mServer = mLocalBinder.getServerInstance();
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mBounded) {
            unbindService(mConnection);
            mBounded = false;
        }
    }
}