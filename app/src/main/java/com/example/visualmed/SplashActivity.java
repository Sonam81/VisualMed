package com.example.visualmed;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;


public class SplashActivity extends Activity {

    Handler handler;
    Calendar calendar;
    TextView hr;
    TextView min;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        min = findViewById(R.id.t2);
        hr = findViewById(R.id.t1);
        Button btn = findViewById(R.id.b1);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int h = Integer.parseInt(hr.getText().toString());
                int m = Integer.parseInt(min.getText().toString());
                alarm(h,m);
            }
        });



    }

    public void alarm(int hr ,int min){

        calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, hr);
        calendar.set(Calendar.MINUTE, min);

        int currentTime = (int) (System.currentTimeMillis() % Integer.MAX_VALUE) + 1;

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent intent = new Intent(this, AlertReciever.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this,currentTime, intent, 0);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
        Toast.makeText(this, "Alarm added", Toast.LENGTH_SHORT).show();
        //adb shell dumpsys alarm
    }
}

