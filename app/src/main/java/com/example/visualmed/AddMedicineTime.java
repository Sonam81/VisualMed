package com.example.visualmed;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

public class AddMedicineTime extends AppCompatActivity {

    private String medicine_name = getIntent().getExtras().getString("medicine_name");
    private String medicine_dose = getIntent().getExtras().getString("medicine_time");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_medicine_time);
        Log.i("medicine_name", medicine_name);
        Log.i("medicine_dose", medicine_dose);
    }
}
