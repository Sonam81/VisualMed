package com.example.visualmed;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Calendar;
import java.util.List;

public class Alarm extends AppCompatActivity {

    Button start;
    private AlarmManager alarmManager;
    private PendingIntent pendingIntent;
    Calendar calendar;
    Intent intent;
    public static final int REQUEST_CODE=101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);


//
//        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
//        intent = new Intent(this, AlertReciever.class);
//        pendingIntent = PendingIntent.getBroadcast(this, REQUEST_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//
//        calendar = Calendar.getInstance();
//        calendar.setTimeInMillis(System.currentTimeMillis());
//        calendar.set(Calendar.HOUR_OF_DAY, 9);
//        calendar.set(Calendar.MINUTE, 55);
//
//        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_FIFTEEN_MINUTES, pendingIntent);

        //get name of medicine in string and time in List<MedicineTime>
//        List<MedicineWithTime> medicineWithTimes;
//        medicineWithTimes = ManageMedicineActivity.myAppDatabase.medicineDAO().getAll();
//        for (int i = 0; i < medicineWithTimes.size();i++) {
//            String medTimes;
//            for (int j = 0; j < medicineWithTimes.get(i).getMedicineTimes().size(); j++){
//                medTimes = medicineWithTimes.get(i).getMedicineTimes().get(j).getMedicineTime()+"\n";
//                System.out.println(medTimes);
//            }
//
//        }

        start= findViewById(R.id.button);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startAlert();
            }
        });
    }

    public void startAlert(){
        long i = calendar.getTimeInMillis();
        Log.i("Alarm",Long.toString(i));
    }
}
