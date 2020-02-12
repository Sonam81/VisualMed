package com.example.visualmed;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.widget.Toast;


public class SplashActivity extends Activity {

    Handler handler;
    boolean mBounded;
    TtsService ttsService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent=new Intent(SplashActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        },3000);
    }

    ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceDisconnected(ComponentName name) {
            Toast.makeText(SplashActivity.this, "Service is disconnected in splash", Toast.LENGTH_LONG).show();
            mBounded = false;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Toast.makeText(SplashActivity.this, "Service is connected in splash", Toast.LENGTH_LONG).show();
            mBounded = true;
            TtsService.LocalBinder mLocalBinder = (TtsService.LocalBinder)service;
            ttsService = mLocalBinder.getServerInstance();
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        Intent mIntent = new Intent(this, TtsService.class);
        bindService(mIntent, mConnection, BIND_AUTO_CREATE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mBounded) {
            unbindService(mConnection);
            mBounded = false;
            Toast.makeText(SplashActivity.this,"Service is disconnected due to destroy",Toast.LENGTH_LONG).show();
        }
    }

}